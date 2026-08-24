import type { FastifyInstance } from 'fastify'
import { getPrisma } from '@tracker/db'
import {
  RATINGS, RATING_NAME, RATING_VALUE, grade, newCard, preview,
  type CardInfo, type RatingName, type SrsCard,
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

function toSrs(c: any): SrsCard {
  return {
    due: c.due.toISOString(),
    stability: c.stability,
    difficulty: c.difficulty,
    elapsedDays: c.elapsedDays,
    scheduledDays: c.scheduledDays,
    reps: c.reps,
    lapses: c.lapses,
    state: c.state,
    lastReview: c.lastReview ? c.lastReview.toISOString() : null,
  }
}

function toInfo(c: any, now: Date): CardInfo {
  return {
    ...toSrs(c),
    suspended: c.suspended,
    overdueDays: Math.floor((now.getTime() - c.due.getTime()) / DAY),
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

    const base = { suspended: false }
    const [due, dueTotal, newTotal] = await Promise.all([
      prisma.card.findMany({
        where: { ...base, due: { lte: now } },
        orderBy: [{ due: 'asc' }],
        take: limit,
        include: { problem: { include: SUMMARY_INCLUDE as any } },
      }),
      prisma.card.count({ where: { ...base, due: { lte: now } } }),
      prisma.card.count({ where: { ...base, state: 'new' } }),
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
        prisma.card.count({ where: { suspended: false, state: 'new' } }),
        prisma.review.count({
          where: { rating: { not: null }, reviewedAt: { gte: startOfToday } },
        }),
        prisma.card.count({ where: { suspended: true } }),
        prisma.review.findMany({
          where: { rating: { not: null }, reviewedAt: { gte: monthAgo } },
          select: { rating: true },
        }),
      ])

    // "Correct" = anything but Again, the usual retention definition.
    const retention30d = recent.length
      ? recent.filter((r) => (r.rating ?? 0) > 1).length / recent.length
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
    const body = req.body as { rating?: string | number; durationMs?: number }

    const name: RatingName | undefined =
      typeof body.rating === 'number'
        ? RATING_NAME[body.rating]
        : (RATINGS as readonly string[]).includes(String(body.rating))
          ? (body.rating as RatingName)
          : undefined
    if (!name) {
      return reply.code(400).send({ message: `rating must be one of ${RATINGS.join(', ')} or 1-4` })
    }

    const problem = await prisma.problem.findUnique({ where: { id: problemId } })
    if (!problem) return reply.code(404).send({ message: 'problem not found' })

    const now = new Date()
    let card = await prisma.card.findUnique({ where: { problemId } })
    if (!card) {
      const fresh = newCard(now)
      card = await prisma.card.create({
        data: {
          problemId,
          due: new Date(fresh.due), stability: fresh.stability,
          difficulty: fresh.difficulty, elapsedDays: fresh.elapsedDays,
          scheduledDays: fresh.scheduledDays, reps: fresh.reps,
          lapses: fresh.lapses, state: fresh.state, lastReview: null,
        },
      })
    }

    // Scheduling is computed server-side so it stays authoritative; the
    // client's previews are only a hint.
    const next = grade(toSrs(card), name, now)

    const updated = await prisma.card.update({
      where: { problemId },
      data: {
        due: new Date(next.due), stability: next.stability,
        difficulty: next.difficulty, elapsedDays: next.elapsedDays,
        scheduledDays: next.scheduledDays, reps: next.reps,
        lapses: next.lapses, state: next.state,
        lastReview: next.lastReview ? new Date(next.lastReview) : now,
      },
    })

    await prisma.review.create({
      data: {
        problemId,
        rating: RATING_VALUE[name],
        state: next.state,
        stability: next.stability,
        difficulty: next.difficulty,
        elapsedDays: next.elapsedDays,
        scheduledDays: next.scheduledDays,
        dueAt: new Date(next.due),
        durationMs: body.durationMs ?? null,
        reviewedAt: now,
        seeded: false,
      },
    })

    return { card: toInfo(updated, now), intervals: preview(next, now) }
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
