/**
 * sync — index the AlgoZenith repo into the database.
 *
 * This is the load-bearing job: solving a problem and committing it is the
 * act of registering it. Run from a git post-commit hook or on demand.
 *
 * Keyed on folderPath. A folder that moved (topic reclassification) is
 * matched by slug and updated in place, so review history survives a
 * `git mv`. Problems in the DB whose folder has vanished are reported,
 * never silently deleted.
 */
import fs from 'node:fs'
import path from 'node:path'
import { getPrisma } from '@tracker/db'
import { UNKNOWN_CREATED_AT, humanize, newLadderState, slugify } from '@tracker/shared'
import { ALGO_ROOT, TOPICS_DIR, TOPIC_DISPLAY, assertLayout } from './paths.js'

const prisma = getPrisma()

interface ScannedProblem {
  folderPath: string
  slug: string
  title: string
  topicName: string
  javaFiles: { filePath: string; code: string; loc: number }[]
  input: string
  expected: string
}

function readIfExists(p: string): string {
  try { return fs.readFileSync(p, 'utf8') } catch { return '' }
}

/** Read one problem folder. Returns null if it is not a directory. */
export function scanProblemFolder(topicDir: string, probDir: string): ScannedProblem | null {
  const probPath = path.join(TOPICS_DIR, topicDir, probDir)
  let stat: fs.Stats
  try { stat = fs.statSync(probPath) } catch { return null }
  if (!stat.isDirectory()) return null

  const javaFiles = fs
    .readdirSync(probPath)
    .filter((f) => f.endsWith('.java'))
    .sort()
    .map((f) => {
      const code = readIfExists(path.join(probPath, f))
      return {
        filePath: path.relative(ALGO_ROOT, path.join(probPath, f)),
        code,
        loc: code ? code.replace(/\n$/, '').split('\n').length : 0,
      }
    })

  return {
    folderPath: `topics/${topicDir}/${probDir}`,
    slug: `${slugify(topicDir)}/${slugify(probDir)}`,
    title: humanize(probDir),
    topicName: TOPIC_DISPLAY[topicDir] ?? humanize(topicDir),
    javaFiles,
    input: readIfExists(path.join(probPath, 'input.txt')),
    expected: readIfExists(path.join(probPath, 'expected.txt')),
  }
}

export function scanRepo(): ScannedProblem[] {
  assertLayout()
  const out: ScannedProblem[] = []
  for (const topicDir of fs.readdirSync(TOPICS_DIR).sort()) {
    const topicPath = path.join(TOPICS_DIR, topicDir)
    if (!fs.statSync(topicPath).isDirectory()) continue
    for (const probDir of fs.readdirSync(topicPath).sort()) {
      const p = scanProblemFolder(topicDir, probDir)
      if (p) out.push(p)
    }
  }
  return out
}

/**
 * Re-read one problem's folder and update its code and test data.
 *
 * Deliberately narrower than a full sync: it does not touch the title,
 * topics or scheduling, only what the files on disk actually say. That
 * makes it safe to press right after editing a solution, without undoing
 * anything you have changed in the app.
 */
export async function syncProblem(problemId: string) {
  assertLayout()
  const problem = await prisma.problem.findUnique({
    where: { id: problemId },
    select: { id: true, folderPath: true },
  })
  if (!problem) return { ok: false as const, reason: 'not-found' as const }

  const parts = problem.folderPath.split('/')
  if (parts.length !== 3 || parts[0] !== 'topics') {
    return { ok: false as const, reason: 'bad-path' as const, folderPath: problem.folderPath }
  }
  const scanned = scanProblemFolder(parts[1], parts[2])
  if (!scanned) {
    return { ok: false as const, reason: 'missing-folder' as const, folderPath: problem.folderPath }
  }

  let files = 0
  for (const f of scanned.javaFiles) {
    await prisma.solution.upsert({
      where: { problemId_filePath: { problemId: problem.id, filePath: f.filePath } },
      update: { code: f.code, loc: f.loc, syncedAt: new Date() },
      create: {
        problemId: problem.id, filePath: f.filePath, code: f.code,
        loc: f.loc, language: 'java',
      },
    })
    files++
  }
  const removed = await prisma.solution.deleteMany({
    where: { problemId: problem.id, filePath: { notIn: scanned.javaFiles.map((f) => f.filePath) } },
  })

  let testcases = 0
  if (scanned.input.trim() || scanned.expected.trim()) {
    await prisma.testcase.upsert({
      where: { problemId_ord: { problemId: problem.id, ord: 0 } },
      update: { input: scanned.input, expected: scanned.expected },
      create: { problemId: problem.id, ord: 0, input: scanned.input, expected: scanned.expected },
    })
    testcases = 1
  } else {
    // Both files empty — drop a stale testcase rather than keeping old data.
    await prisma.testcase.deleteMany({ where: { problemId: problem.id, ord: 0 } })
  }

  // A problem is reviewable whether or not it has test data.
  const card = await prisma.card.findUnique({ where: { problemId: problem.id } })
  if (!card) {
    await prisma.card.create({
      data: { problemId: problem.id, due: new Date(), step: 0, reps: 0 },
    })
  }

  return {
    ok: true as const,
    folderPath: problem.folderPath,
    files,
    removedFiles: removed.count,
    testcases,
    loc: scanned.javaFiles.reduce((a, f) => a + f.loc, 0),
    cardCreated: !card,
  }
}

async function upsertTopic(name: string) {
  const slug = slugify(name)
  return prisma.topic.upsert({
    where: { slug },
    update: { name },
    create: { name, slug },
  })
}

export async function sync(opts: { quiet?: boolean } = {}) {
  const log = (...a: unknown[]) => { if (!opts.quiet) console.log(...a) }
  const scanned = scanRepo()
  log(`scanned ${scanned.length} problem folders under ${TOPICS_DIR}`)

  const existing = await prisma.problem.findMany({
    select: { id: true, slug: true, folderPath: true },
  })
  const byPath = new Map(existing.map((p) => [p.folderPath, p]))
  const bySlug = new Map(existing.map((p) => [p.slug, p]))
  // Folder basenames are unique across topics, which makes them the only
  // key that survives a move BETWEEN topics — where folderPath and slug
  // both change. Without this, reclassifying a problem would orphan its
  // row and silently lose its notes and review history.
  const byBase = new Map(existing.map((p) => [p.folderPath.split('/').pop()!, p]))

  let created = 0, updated = 0, moved = 0, cards = 0
  const seen = new Set<string>()

  for (const s of scanned) {
    const topic = await upsertTopic(s.topicName)
    const base = s.folderPath.split('/').pop()!
    let row = byPath.get(s.folderPath) ?? bySlug.get(s.slug) ?? byBase.get(base)

    if (!row) {
      const p = await prisma.problem.create({
        data: {
          slug: s.slug, title: s.title, folderPath: s.folderPath,
          // The filesystem carries no creation date. import() overwrites
          // this for the problems Notion knows about; the rest keep the
          // sentinel rather than pretending they were created today.
          createdAt: new Date(UNKNOWN_CREATED_AT),
        },
      })
      row = { id: p.id, slug: p.slug, folderPath: p.folderPath }
      created++
    } else {
      if (row.folderPath !== s.folderPath) {
        log(`  moved: ${row.folderPath} -> ${s.folderPath} (history preserved)`)
        moved++
        // The old topic link is stale after a move between topics.
        await prisma.problemTopic.deleteMany({ where: { problemId: row.id } })
      }
      await prisma.problem.update({
        where: { id: row.id },
        data: { slug: s.slug, folderPath: s.folderPath, title: s.title },
      })
      updated++
    }
    seen.add(row.id)

    // Topic membership follows the folder tree, which is authoritative
    // for *where the file lives*. Extra topics added in the app are kept.
    await prisma.problemTopic.upsert({
      where: { problemId_topicId: { problemId: row.id, topicId: topic.id } },
      update: {},
      create: { problemId: row.id, topicId: topic.id },
    })

    for (const f of s.javaFiles) {
      await prisma.solution.upsert({
        where: { problemId_filePath: { problemId: row.id, filePath: f.filePath } },
        update: { code: f.code, loc: f.loc, syncedAt: new Date() },
        create: {
          problemId: row.id, filePath: f.filePath, code: f.code,
          loc: f.loc, language: 'java',
        },
      })
    }
    // Drop rows for files that no longer exist. Without this a move leaves
    // the old path behind and every LOC total is inflated.
    await prisma.solution.deleteMany({
      where: { problemId: row.id, filePath: { notIn: s.javaFiles.map((f) => f.filePath) } },
    })

    // Everything in the tree is solved by definition — it would not be
    // committed otherwise — so every problem is reviewable. Never touch a
    // card that already exists: that would reset real review history on an
    // ordinary re-sync.
    {
      const existing = await prisma.card.findUnique({ where: { problemId: row.id } })
      if (!existing) {
        const c = newLadderState(new Date())
        await prisma.card.create({
          data: {
            problemId: row.id,
            due: new Date(c.due),
            step: c.step,
            reps: c.reps,
            lastReview: null,
          },
        })
        cards++
      }
    }

    if (s.input.trim() || s.expected.trim()) {
      await prisma.testcase.upsert({
        where: { problemId_ord: { problemId: row.id, ord: 0 } },
        update: { input: s.input, expected: s.expected },
        create: { problemId: row.id, ord: 0, input: s.input, expected: s.expected },
      })
    }
  }

  const orphans = existing.filter((p) => !seen.has(p.id))
  if (orphans.length) {
    log(`\n  ${orphans.length} problem(s) in the DB have no folder on disk:`)
    for (const o of orphans) log(`    ! ${o.folderPath}`)
    log('  Not deleted. Re-run after a rename, or remove them in the app.')
  }

  log(`\ncreated ${created} | updated ${updated} | moved ${moved} | new cards ${cards} | orphaned ${orphans.length}`)
  return { created, updated, removed: orphans.length, cards }
}

const isMain = process.argv[1] && import.meta.url.endsWith(path.basename(process.argv[1]))
if (isMain) {
  sync()
    .then(() => prisma.$disconnect())
    .catch(async (e) => { console.error(e); await prisma.$disconnect(); process.exit(1) })
}
