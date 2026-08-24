import type { FastifyInstance } from 'fastify'
import { getPrisma } from '@tracker/db'
import {
  OUTCOMES, advance, intervalLabel, newLadderState,
  type CardInfo, type LadderState, type Outcome,
} from '@tracker/shared'
import { SUMMARY_INCLUDE, toSummary } from './serialize.js'

const prisma = getPrisma()

const DAY = 86400000

/**
 * Local YYYY-MM-DD. Not toISOString(): that converts to UTC first, so at
 * any positive offset (IST is +5:30) local midnight falls on the previous
 * UTC day and every forecast bucket is labelled a day early.
 */
function localDay(d: Date): string {
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${y}-${m}-${day}`
}

function toLadder(c: any): LadderState {
  return {
    step: c.step,
    reps: c.reps,
    due: c.due.toISOString(),
    lastReviewedAt: c.lastReview ? c.lastReview.toISOString() : null,
  }
}

function toInfo(c: any, now: Date): CardInfo {
  return {
    due: c.due.toISOString(),
    step: c.step,
    reps: c.reps,
    lapses: c.lapses,
    lastReview: c.lastReview ? c.lastReview.toISOString() : null,
    suspended: c.suspended,
    overdueDays: Math.floor((now.getTime() - c.due.getTime()) / DAY),
    nextIntervalLabel: intervalLabel(c.step),
  }
}

export function registerReviewRoutes(app: FastifyInstance) {
  /**
   * The due queue, oldest first so a backlog drains in the order it built
   * up. New cards come after due ones: revisiting something you already
   * half-know is worth more than starting something fresh.
   */
  app.get('/api/review/queue', async (req) => {
    const q = req.query as Record<string, string | undefined>
    const limit = Math.min(100, Math.max(1, Number(q.limit ?? 30)))
    const now = new Date()

    // A topic is only a filter over this queue — it has no schedule of its own.
    const base: any = { suspended: false }
    if (q.topic) base.problem = { topics: { some: { topic: { slug: q.topic } } } }
    const [due, dueTotal, newTotal] = await Promise.all([
      prisma.card.findMany({
        where: { ...base, due: { lte: now } },
        orderBy: [{ due: 'asc' }],
        take: limit,
        include: { problem: { include: SUMMARY_INCLUDE as any } },
      }),
      prisma.card.count({ where: { ...base, due: { lte: now } } }),
      prisma.card.count({ where: { ...base, reps: 0 } }),
    ])

    return {
      items: due.map((c) => ({
        problem: toSummary(c.problem),
        card: toInfo(c, now),
      })),
      dueTotal,
      newTotal,
    }
  })

  app.get('/api/review/stats', async () => {
    const now = new Date()
    const startOfToday = new Date(now)
    startOfToday.setHours(0, 0, 0, 0)
    const monthAgo = new Date(now.getTime() - 30 * DAY)

    const [totalCards, dueCount, backlog, newCards, reviewedToday, suspended, recent] =
      await Promise.all([
        prisma.card.count(),
        prisma.card.count({ where: { suspended: false, due: { lte: now } } }),
        prisma.card.count({
          where: { suspended: false, due: { lt: new Date(now.getTime() - DAY) } },
        }),
        prisma.card.count({ where: { suspended: false, reps: 0 } }),
        prisma.review.count({
          where: { outcome: { not: null }, reviewedAt: { gte: startOfToday } },
        }),
        prisma.card.count({ where: { suspended: true } }),
        prisma.review.findMany({
          where: { outcome: { not: null }, reviewedAt: { gte: monthAgo } },
          select: { outcome: true },
        }),
      ])

    const retention30d = recent.length
      ? recent.filter((r) => r.outcome === 'good').length / recent.length
      : null

    return {
      due: dueCount, backlog, newCards, reviewedToday,
      totalCards, suspended, retention30d,
    }
  })

  app.get('/api/review/forecast', async (req) => {
    const q = req.query as Record<string, string | undefined>
    const days = Math.min(60, Math.max(1, Number(q.days ?? 14)))
    const now = new Date()
    const start = new Date(now)
    start.setHours(0, 0, 0, 0)

    const cards = await prisma.card.findMany({
      where: { suspended: false, due: { lt: new Date(start.getTime() + days * DAY) } },
      select: { due: true },
    })

    const buckets = new Map<string, number>()
    for (let i = 0; i < days; i++) {
      buckets.set(localDay(new Date(start.getTime() + i * DAY)), 0)
    }
    for (const c of cards) {
      // Everything overdue lands on today rather than vanishing off the chart.
      const day = c.due < start ? start : c.due
      const key = localDay(day)
      if (buckets.has(key)) buckets.set(key, (buckets.get(key) ?? 0) + 1)
    }
    return [...buckets.entries()].map(([date, count]) => ({ date, count }))
  })

  app.post('/api/review/:problemId/grade', async (req, reply) => {
    const { problemId } = req.params as { problemId: string }
    const body = req.body as { outcome?: string; rating?: string; durationMs?: number }

    // Accept `rating` too — it was the field name before problems moved onto
    // the shared ladder.
    const raw = String(body.outcome ?? body.rating ?? '')
    if (!(OUTCOMES as readonly string[]).includes(raw)) {
      return reply.code(400).send({ message: `outcome must be one of ${OUTCOMES.join(', ')}` })
    }
    const outcome = raw as Outcome

    const problem = await prisma.problem.findUnique({ where: { id: problemId } })
    if (!problem) return reply.code(404).send({ message: 'problem not found' })

    const now = new Date()
    let card = await prisma.card.findUnique({ where: { problemId } })
    if (!card) {
      const fresh = newLadderState(now)
      card = await prisma.card.create({
        data: {
          problemId, due: new Date(fresh.due), step: fresh.step, reps: fresh.reps,
        },
      })
    }

    const next = advance(toLadder(card), outcome, now)

    const updated = await prisma.card.update({
      where: { problemId },
      data: {
        due: new Date(next.due),
        step: next.step,
        reps: next.reps,
        lapses: outcome === 'again' ? card.lapses + 1 : card.lapses,
        lastReview: now,
      },
    })

    await prisma.review.create({
      data: {
        problemId,
        outcome,
        step: next.step,
        dueAt: new Date(next.due),
        durationMs: body.durationMs ?? null,
        reviewedAt: now,
        seeded: false,
      },
    })

    return { card: toInfo(updated, now) }
  })

  // ---------- topics as a lens on the queue ----------

  /**
   * Per-topic counts over the problem queue. Topics carry no schedule of
   * their own: whether a technique needs work is answered by how many of
   * its problems are due, not by a separate judgement about the topic.
   */
  app.get('/api/revision/topics', async () => {
    const now = new Date()
    const topics = await prisma.topic.findMany({
      include: { _count: { select: { problems: true } } },
      orderBy: { name: 'asc' },
    })

    // One pass over the join rows rather than a query per topic.
    const links = await prisma.problemTopic.findMany({
      select: {
        topicId: true,
        problem: { select: { card: { select: { due: true, reps: true, suspended: true } } } },
      },
    })

    const agg = new Map<string, { due: number; fresh: number; nextDue: Date | null }>()
    for (const l of links) {
      const card = l.problem.card
      if (!card || card.suspended) continue
      const a = agg.get(l.topicId) ?? { due: 0, fresh: 0, nextDue: null }
      if (card.due <= now) a.due++
      else if (!a.nextDue || card.due < a.nextDue) a.nextDue = card.due
      if (card.reps === 0) a.fresh++
      agg.set(l.topicId, a)
    }

    return topics
      .filter((t) => t._count.problems > 0)
      .map((t) => {
        const a = agg.get(t.id) ?? { due: 0, fresh: 0, nextDue: null }
        return {
          topic: { id: t.id, name: t.name, slug: t.slug, count: t._count.problems },
          problems: t._count.problems,
          due: a.due,
          fresh: a.fresh,
          nextDue: a.nextDue ? a.nextDue.toISOString() : null,
        }
      })
      .sort((a, b) => b.due - a.due || a.topic.name.localeCompare(b.topic.name))
  })

  app.post('/api/review/:problemId/suspend', async (req, reply) => {
    const { problemId } = req.params as { problemId: string }
    const { suspended } = req.body as { suspended?: boolean }
    const card = await prisma.card.findUnique({ where: { problemId } })
    if (!card) return reply.code(404).send({ message: 'card not found' })
    const updated = await prisma.card.update({
      where: { problemId },
      data: { suspended: Boolean(suspended) },
    })
    return toInfo(updated, new Date())
  })
}
