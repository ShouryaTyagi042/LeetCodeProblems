import Fastify from 'fastify'
import cors from '@fastify/cors'
import { getPrisma } from '@tracker/db'
import {
  DEFAULT_SORT, difficultyRank, parseSort, slugify,
  type Facets, type SortSpec, type Stats,
} from '@tracker/shared'
import { DETAIL_INCLUDE, SUMMARY_INCLUDE, toDetail, toSummary } from './serialize.js'
import { scaffoldFolder } from './scaffold.js'
import { registerReviewRoutes } from './review.js'

const prisma = getPrisma()
const app = Fastify({ logger: { level: process.env.LOG_LEVEL ?? 'info' } })
await app.register(cors, { origin: true })

/** Single user: an optional shared token, off by default for local use. */
const TOKEN = process.env.API_TOKEN
app.addHook('onRequest', async (req, reply) => {
  if (!TOKEN) return
  if (req.method === 'OPTIONS') return
  if (req.headers.authorization !== `Bearer ${TOKEN}`) {
    return reply.code(401).send({ message: 'unauthorized' })
  }
})

app.get('/api/health', async () => ({ ok: true }))

registerReviewRoutes(app)

// ---------- Phase 1: read ----------

/**
 * Multi-key ordering from 'field:dir,field:dir'. Keys apply in the order
 * given, so 'difficulty:asc,title:asc' is difficulty first, title as the
 * tie-break. A stable final key keeps pagination from repeating or
 * dropping rows when many rows tie.
 */
function buildOrderBy(raw?: string): any[] {
  const parsed = parseSort(raw)
  const specs: SortSpec[] = parsed.length ? parsed : [...DEFAULT_SORT]
  // Unset values go last in both directions. SQLite would otherwise sort
  // NULL first, so ascending by "next review" would lead with the problems
  // that have no review scheduled at all.
  const nl = (dir: string) => ({ sort: dir, nulls: 'last' })
  const mapped: any[] = specs.map((s): any => {
    switch (s.field) {
      // Sort on the numeric rank, not the string: alphabetically Easy <
      // Hard < Medium, which is not the order anyone means.
      case 'difficulty': return { difficultyRank: nl(s.dir) }
      case 'created': return { createdAt: s.dir }
      case 'source': return { source: nl(s.dir) }
      default: return { [s.field]: s.dir }
    }
  })
  // Title is a readable tie-break — without it the 126 problems sharing the
  // unknown-date sentinel would come back in arbitrary order.
  if (!specs.some((s) => s.field === 'title')) mapped.push({ title: 'asc' })
  mapped.push({ id: 'asc' })
  return mapped
}

app.get('/api/problems', async (req) => {
  const q = req.query as Record<string, string | undefined>
  const page = Math.max(1, Number(q.page ?? 1))
  const perPage = Math.min(200, Math.max(1, Number(q.perPage ?? 50)))
  const term = q.q?.trim()

  const where: any = { AND: [] as any[] }
  if (q.difficulty) where.AND.push({ difficulty: q.difficulty })
  if (q.source) where.AND.push({ source: q.source })
  if (q.topic) where.AND.push({ topics: { some: { topic: { slug: q.topic } } } })
  if (q.pattern) where.AND.push({ patterns: { some: { pattern: { slug: q.pattern } } } })
  if (term) {
    where.AND.push({
      OR: [
        { title: { contains: term } },
        { folderPath: { contains: term } },
        { note: { framework: { contains: term } } },
        { note: { notes: { contains: term } } },
        { note: { mistakes: { contains: term } } },
      ],
    })
  }
  if (!where.AND.length) delete where.AND

  const orderBy = buildOrderBy(q.sort)

  const [items, total] = await Promise.all([
    prisma.problem.findMany({
      where, orderBy, skip: (page - 1) * perPage, take: perPage,
      include: SUMMARY_INCLUDE as any,
    }),
    prisma.problem.count({ where }),
  ])
  return { items: items.map(toSummary), total, page, perPage }
})

async function findProblem(key: string) {
  return prisma.problem.findFirst({
    where: { OR: [{ id: key }, { slug: key }] },
    include: DETAIL_INCLUDE as any,
  })
}

// Slugs contain a slash ('graphs/dijkstras'). Accept it as two path
// segments as well as one encoded segment: %2F is rejected outright by
// nginx and several other proxies, so the two-segment form is the one
// that will keep working once this is hosted for the mobile app.
app.get('/api/problems/:topic/:name', async (req, reply) => {
  const { topic, name } = req.params as { topic: string; name: string }
  const p = await findProblem(`${topic}/${name}`)
  if (!p) return reply.code(404).send({ message: 'not found' })
  return toDetail(p)
})

app.get('/api/problems/:key', async (req, reply) => {
  const { key } = req.params as { key: string }
  const p = await findProblem(key)
  if (!p) return reply.code(404).send({ message: 'not found' })
  return toDetail(p)
})

app.get('/api/facets', async (): Promise<Facets> => {
  const [topics, patterns, sources, difficulties] = await Promise.all([
    prisma.topic.findMany({ include: { _count: { select: { problems: true } } }, orderBy: { name: 'asc' } }),
    prisma.pattern.findMany({ include: { _count: { select: { problems: true } } }, orderBy: { name: 'asc' } }),
    prisma.problem.groupBy({ by: ['source'], _count: true }),
    prisma.problem.groupBy({ by: ['difficulty'], _count: true }),
  ])
  const clean = (rows: any[], key: string) =>
    rows.filter((r) => r[key]).map((r) => ({ name: r[key] as string, count: r._count as number }))
        .sort((a, b) => b.count - a.count)
  return {
    topics: topics.map((t) => ({ id: t.id, name: t.name, slug: t.slug, count: t._count.problems })),
    patterns: patterns.map((p) => ({ id: p.id, name: p.name, slug: p.slug, count: p._count.problems })),
    sources: clean(sources, 'source'),
    difficulties: clean(difficulties, 'difficulty'),
  }
})

app.get('/api/stats', async (): Promise<Stats> => {
  const [problems, withNotes, withMistakes, topics, patterns, loc] = await Promise.all([
    prisma.problem.count(),
    prisma.note.count(),
    prisma.note.count({ where: { NOT: { mistakes: null } } }),
    prisma.topic.count(),
    prisma.pattern.count(),
    prisma.solution.aggregate({ _sum: { loc: true } }),
  ])
  return {
    problems, withNotes, withMistakes,
    topics, patterns, totalLoc: loc._sum.loc ?? 0,
  }
})

app.get('/api/topics', async () => {
  const t = await prisma.topic.findMany({
    include: { _count: { select: { problems: true } } }, orderBy: { name: 'asc' },
  })
  return t.map((x) => ({ id: x.id, name: x.name, slug: x.slug, count: x._count.problems }))
})

app.get('/api/patterns', async () => {
  const t = await prisma.pattern.findMany({
    include: { _count: { select: { problems: true } } }, orderBy: { name: 'asc' },
  })
  return t.map((x) => ({ id: x.id, name: x.name, slug: x.slug, count: x._count.problems }))
})

// ---------- Phase 2: write ----------

async function setTags(
  problemId: string,
  names: string[] | undefined,
  kind: 'topic' | 'pattern',
) {
  if (!names) return
  const model: any = kind === 'topic' ? prisma.topic : prisma.pattern
  const join: any = kind === 'topic' ? prisma.problemTopic : prisma.problemPattern
  const idField = kind === 'topic' ? 'topicId' : 'patternId'

  const ids: string[] = []
  for (const raw of names.map((n) => n.trim()).filter(Boolean)) {
    const row = await model.upsert({
      where: { slug: slugify(raw) },
      update: {},
      create: { name: raw, slug: slugify(raw) },
    })
    ids.push(row.id)
  }
  await join.deleteMany({ where: { problemId, [idField]: { notIn: ids } } })
  for (const id of ids) {
    await join.upsert({
      where: { [`problemId_${idField}`]: { problemId, [idField]: id } },
      update: {},
      create: { problemId, [idField]: id },
    })
  }
}

app.patch('/api/problems/:id', async (req, reply) => {
  const { id } = req.params as { id: string }
  const b = req.body as any
  const exists = await prisma.problem.findUnique({ where: { id } })
  if (!exists) return reply.code(404).send({ message: 'not found' })

  const data: any = {}
  for (const k of ['title', 'source', 'judgeUrl', 'difficulty'] as const) {
    if (k in b) data[k] = b[k]
  }
  if ('difficulty' in b) data.difficultyRank = difficultyRank(b.difficulty)
  if (Object.keys(data).length) await prisma.problem.update({ where: { id }, data })
  await setTags(id, b.topics, 'topic')
  await setTags(id, b.patterns, 'pattern')

  const p = await prisma.problem.findUnique({ where: { id }, include: DETAIL_INCLUDE as any })
  return toDetail(p)
})

app.put('/api/problems/:id/note', async (req, reply) => {
  const { id } = req.params as { id: string }
  const b = req.body as any
  const exists = await prisma.problem.findUnique({ where: { id } })
  if (!exists) return reply.code(404).send({ message: 'not found' })

  const payload = {
    framework: b.framework ?? null,
    notes: b.notes ?? null,
    mistakes: b.mistakes ?? null,
  }
  await prisma.note.upsert({
    where: { problemId: id },
    update: payload,
    create: { problemId: id, ...payload },
  })
  const p = await prisma.problem.findUnique({ where: { id }, include: DETAIL_INCLUDE as any })
  return toDetail(p)
})

app.post('/api/problems', async (req, reply) => {
  const b = req.body as any
  if (!b?.title?.trim()) return reply.code(400).send({ message: 'title is required' })
  if (!b?.topic?.trim()) return reply.code(400).send({ message: 'topic is required' })

  const folderName: string =
    (b.folderName?.trim() || b.title.trim().replace(/[^A-Za-z0-9]+/g, ''))
  if (!/^[A-Za-z0-9]+$/.test(folderName)) {
    return reply.code(400).send({ message: 'folderName must be alphanumeric' })
  }

  const topicRow = await prisma.topic.upsert({
    where: { slug: slugify(b.topic) },
    update: {},
    create: { name: b.topic.trim(), slug: slugify(b.topic) },
  })

  // Folder name on disk mirrors the topic's display name without spaces.
  const topicDir = b.topic.trim().replace(/[^A-Za-z0-9]+/g, '')
  const folderPath = `topics/${topicDir}/${folderName}`

  if (await prisma.problem.findUnique({ where: { folderPath } })) {
    return reply.code(409).send({ message: `${folderPath} already exists` })
  }

  if (b.scaffold !== false) {
    try {
      scaffoldFolder(topicDir, folderName)
    } catch (e: any) {
      return reply.code(500).send({ message: `scaffold failed: ${e.message}` })
    }
  }

  const created = await prisma.problem.create({
    data: {
      title: b.title.trim(),
      slug: `${slugify(topicDir)}/${slugify(folderName)}`,
      folderPath,
      difficulty: b.difficulty ?? null,
      difficultyRank: difficultyRank(b.difficulty),
      source: b.source ?? null,
      judgeUrl: b.judgeUrl ?? null,
      topics: { create: { topicId: topicRow.id } },
      // Every problem is reviewable, so give it a card here too. Without
      // this a problem created in the app stays out of the review queue
      // until the next sync happens to run.
      card: { create: { due: new Date(), step: 0, reps: 0 } },
    },
  })
  const p = await prisma.problem.findUnique({
    where: { id: created.id }, include: DETAIL_INCLUDE as any,
  })
  return reply.code(201).send(toDetail(p))
})

app.delete('/api/problems/:id', async (req, reply) => {
  const { id } = req.params as { id: string }
  const exists = await prisma.problem.findUnique({ where: { id } })
  if (!exists) return reply.code(404).send({ message: 'not found' })
  // Removes the DB row only. Files on disk are never touched.
  await prisma.problem.delete({ where: { id } })
  return reply.code(204).send()
})

app.post('/api/sync', async () => {
  const { sync } = await import('@tracker/tools/sync')
  return sync({ quiet: true })
})

const port = Number(process.env.PORT ?? 5174)
await app.listen({ port, host: '127.0.0.1' })
