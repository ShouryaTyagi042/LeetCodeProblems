import type { FastifyInstance } from 'fastify'
import { getPrisma } from '@tracker/db'
import {
  UNKNOWN_CREATED_AT, difficultyRank,
  type Dashboard, type Difficulty, type ProblemOfTheDay,
} from '@tracker/shared'
import { localDay } from './review.js'
import { SUMMARY_INCLUDE, toSummary } from './serialize.js'

const prisma = getPrisma()

/** Days of history behind the calendar: a full 53-week grid. */
const HISTORY_DAYS = 371

function addDays(d: Date, n: number): Date {
  // setDate rather than adding 86400000 ms, so a DST change cannot push a
  // step onto the wrong calendar day.
  const out = new Date(d)
  out.setDate(out.getDate() + n)
  return out
}

/** Longest run of consecutive active days, given days sorted ascending. */
function longestRun(days: string[]): number {
  let best = 0
  let run = 0
  let prev: Date | null = null
  for (const key of days) {
    const d = new Date(`${key}T00:00:00`)
    run = prev && localDay(addDays(prev, 1)) === key ? run + 1 : 1
    best = Math.max(best, run)
    prev = d
  }
  return best
}

const POTD_QUERY = `query {
  activeDailyCodingChallengeQuestion {
    date
    link
    question { title titleSlug difficulty questionFrontendId topicTags { name } }
  }
}`

/** LeetCode's daily question only changes once a day; keep it for the day. */
let potdCache: { day: string; data: Omit<ProblemOfTheDay, 'tracked'> } | null = null

async function fetchDaily(): Promise<Omit<ProblemOfTheDay, 'tracked'>> {
  const today = localDay(new Date())
  if (potdCache?.day === today) return potdCache.data

  const res = await fetch('https://leetcode.com/graphql', {
    method: 'POST',
    headers: { 'content-type': 'application/json', referer: 'https://leetcode.com' },
    body: JSON.stringify({ query: POTD_QUERY }),
    signal: AbortSignal.timeout(8000),
  })
  if (!res.ok) throw new Error(`LeetCode answered ${res.status}`)
  const body = (await res.json()) as any
  const d = body?.data?.activeDailyCodingChallengeQuestion
  if (!d?.question) throw new Error('LeetCode returned no daily question')

  const q = d.question
  const data = {
    date: d.date,
    title: q.title,
    titleSlug: q.titleSlug,
    frontendId: String(q.questionFrontendId),
    difficulty: difficultyRank(q.difficulty) ? (q.difficulty as Difficulty) : null,
    url: `https://leetcode.com${d.link}`,
    tags: (q.topicTags ?? []).map((t: any) => t.name),
  }
  potdCache = { day: today, data }
  return data
}

export function registerDashboardRoutes(app: FastifyInstance) {
  app.get('/api/dashboard', async (): Promise<Dashboard> => {
    const today = new Date()
    today.setHours(0, 0, 0, 0)
    const start = addDays(today, -(HISTORY_DAYS - 1))

    // The streak needs all of history, not just the calendar's year. Problems
    // carrying the unknown-date sentinel were never really added that day,
    // so they would plant one huge fake day in the grid.
    const rows = await prisma.problem.findMany({
      where: { createdAt: { not: new Date(UNKNOWN_CREATED_AT) } },
      select: { createdAt: true },
    })

    const perDay = new Map<string, number>()
    for (const r of rows) {
      const key = localDay(r.createdAt)
      perDay.set(key, (perDay.get(key) ?? 0) + 1)
    }

    const activity = []
    for (let i = 0; i < HISTORY_DAYS; i++) {
      const key = localDay(addDays(start, i))
      activity.push({ date: key, added: perDay.get(key) ?? 0 })
    }

    // Walk back from today; an empty today only means "not yet", so start
    // counting from yesterday in that case.
    let cursor = perDay.has(localDay(today)) ? today : addDays(today, -1)
    let currentStreak = 0
    while (perDay.has(localDay(cursor))) {
      currentStreak++
      cursor = addDays(cursor, -1)
    }

    const sum = (from: Date) =>
      rows.filter((r) => r.createdAt >= from).length
    // Monday-start week, the way most people count "this week".
    const weekStart = addDays(today, -((today.getDay() + 6) % 7))

    return {
      activity,
      currentStreak,
      longestStreak: longestRun([...perDay.keys()].sort()),
      addedToday: perDay.get(localDay(today)) ?? 0,
      addedThisWeek: sum(weekStart),
      addedThisYear: sum(new Date(today.getFullYear(), 0, 1)),
    }
  })

  app.get('/api/potd', async (_req, reply) => {
    let daily
    try {
      daily = await fetchDaily()
    } catch (e) {
      return reply.code(502).send({ message: `Could not reach LeetCode: ${(e as Error).message}` })
    }
    // Problems are matched on their judge URL, which for a LeetCode problem
    // always carries /problems/<slug>/ whatever query string follows it.
    const tracked = await prisma.problem.findFirst({
      where: { judgeUrl: { contains: `leetcode.com/problems/${daily.titleSlug}/` } },
      include: SUMMARY_INCLUDE as any,
    })
    return { ...daily, tracked: tracked ? toSummary(tracked) : null }
  })
}
