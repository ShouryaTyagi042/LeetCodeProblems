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
import { humanize, slugify } from '@tracker/shared'
import { ALGO_ROOT, TOPICS_DIR, TOPIC_DISPLAY, assertLayout } from './paths.js'

const prisma = getPrisma()

interface ScannedProblem {
  folderPath: string
  slug: string
  title: string
  topicName: string
  status: 'solved' | 'unsolved'
  javaFiles: { filePath: string; code: string; loc: number }[]
  input: string
  expected: string
}

function readIfExists(p: string): string {
  try { return fs.readFileSync(p, 'utf8') } catch { return '' }
}

export function scanRepo(): ScannedProblem[] {
  assertLayout()
  const out: ScannedProblem[] = []
  for (const topicDir of fs.readdirSync(TOPICS_DIR).sort()) {
    const topicPath = path.join(TOPICS_DIR, topicDir)
    if (!fs.statSync(topicPath).isDirectory()) continue
    const topicName = TOPIC_DISPLAY[topicDir] ?? humanize(topicDir)

    for (const probDir of fs.readdirSync(topicPath).sort()) {
      const probPath = path.join(topicPath, probDir)
      if (!fs.statSync(probPath).isDirectory()) continue

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

      const input = readIfExists(path.join(probPath, 'input.txt'))
      const expected = readIfExists(path.join(probPath, 'expected.txt'))

      out.push({
        folderPath: `topics/${topicDir}/${probDir}`,
        slug: `${slugify(topicDir)}/${slugify(probDir)}`,
        title: humanize(probDir),
        topicName,
        // expected.txt is the only signal the repo carries for "did I finish this"
        status: expected.trim() ? 'solved' : 'unsolved',
        javaFiles,
        input,
        expected,
      })
    }
  }
  return out
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
    select: { id: true, slug: true, folderPath: true, status: true },
  })
  const byPath = new Map(existing.map((p) => [p.folderPath, p]))
  const bySlug = new Map(existing.map((p) => [p.slug, p]))
  // Folder basenames are unique across topics, which makes them the only
  // key that survives a move BETWEEN topics — where folderPath and slug
  // both change. Without this, reclassifying a problem would orphan its
  // row and silently lose its notes and review history.
  const byBase = new Map(existing.map((p) => [p.folderPath.split('/').pop()!, p]))

  let created = 0, updated = 0, moved = 0
  const seen = new Set<string>()

  for (const s of scanned) {
    const topic = await upsertTopic(s.topicName)
    const base = s.folderPath.split('/').pop()!
    let row = byPath.get(s.folderPath) ?? bySlug.get(s.slug) ?? byBase.get(base)

    if (!row) {
      const p = await prisma.problem.create({
        data: {
          slug: s.slug, title: s.title, folderPath: s.folderPath, status: s.status,
        },
      })
      row = { id: p.id, slug: p.slug, folderPath: p.folderPath, status: p.status }
      created++
    } else {
      if (row.folderPath !== s.folderPath) {
        log(`  moved: ${row.folderPath} -> ${s.folderPath} (history preserved)`)
        moved++
        // The old topic link is stale after a move between topics.
        await prisma.problemTopic.deleteMany({ where: { problemId: row.id } })
      }
      // Never downgrade a manually-set status back to unsolved.
      const status = row.status === 'needs_review' ? row.status : s.status
      await prisma.problem.update({
        where: { id: row.id },
        data: { slug: s.slug, folderPath: s.folderPath, title: s.title, status },
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

  log(`\ncreated ${created} | updated ${updated} | moved ${moved} | orphaned ${orphans.length}`)
  return { created, updated, removed: orphans.length }
}

const isMain = process.argv[1] && import.meta.url.endsWith(path.basename(process.argv[1]))
if (isMain) {
  sync()
    .then(() => prisma.$disconnect())
    .catch(async (e) => { console.error(e); await prisma.$disconnect(); process.exit(1) })
}
