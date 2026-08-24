/**
 * import — one-time seed of Notion metadata into the database.
 *
 * Reads the Notion CSV export plus notion_folder_map.csv (the join key
 * produced when the two systems were reconciled) and enriches the
 * problems that sync() already created from the filesystem.
 *
 * Run AFTER sync: the filesystem defines which problems exist, Notion
 * only adds metadata to the ones it knows about.
 */
import fs from 'node:fs'
import path from 'node:path'
import { parse } from 'csv-parse/sync'
import { getPrisma } from '@tracker/db'
import { difficultyRank, seedLadder, slugify } from '@tracker/shared'
import { ALGO_ROOT } from './paths.js'

const prisma = getPrisma()

const NOTION_CSV = path.join(
  ALGO_ROOT,
  'notion_updated/Questions Database/Questions 340565e68b8880079f97fbb7a4c7d2c9_all.csv',
)
const MAP_CSV = path.join(ALGO_ROOT, 'notion_folder_map.csv')

/** Notion's topic vocabulary -> the topic names sync() creates from folders. */
const TOPIC_ALIAS: Record<string, string> = {
  Graphs: 'Graphs',
  Arrays: 'Arrays',
  'Binary Search': 'Binary Search',
  'Two Pointers': 'Two Pointers',
  DP: 'Dynamic Programming',
  'Backtracking LCCM': 'Backtracking',
  HashMap: 'Maps',
  Greedy: 'Greedy & Sweep Line',
  Queues: 'Stack & Queues',
  Stacks: 'Stack & Queues',
  Recursion: 'Recursion',
  'Priority Queue': 'Priority Queue',
  Heaps: 'Priority Queue',
  Sets: 'Sets',
  MultiSets: 'MultiSet',
  Deque: 'Deque & Ordered Set',
  'Indexed Set': 'Deque & Ordered Set',
  PrefixSum: 'Prefix Sums',
  Maths: 'Number Theory',
  Permuations: 'Combinatorics',
}

/** Minimal typo fixes in the Form vocabulary. Everything else is kept verbatim. */
const PATTERN_FIX: Record<string, string> = {
  'MniMax DP': 'MiniMax DP',
  'Game Dp': 'Game DP',
  'Bottom Up Dp': 'Bottom-Up DP',
  PrefixSUM: 'Prefix Sum',
  Dijkstras: "Dijkstra's",
}

function readCsv(p: string): Record<string, string>[] {
  const raw = fs.readFileSync(p, 'utf8').replace(/^﻿/, '')
  return parse(raw, { columns: true, skip_empty_lines: true, relax_column_count: true })
}

function parseDate(s: string): Date | null {
  const t = s?.trim()
  if (!t) return null
  const d = new Date(t)
  return Number.isNaN(d.getTime()) ? null : d
}

const splitTags = (s: string) =>
  (s ?? '').split(',').map((x) => x.trim()).filter(Boolean)

export async function importNotion(opts: { quiet?: boolean } = {}) {
  const log = (...a: unknown[]) => { if (!opts.quiet) console.log(...a) }

  for (const f of [NOTION_CSV, MAP_CSV]) {
    if (!fs.existsSync(f)) throw new Error(`missing ${f}`)
  }

  const notion = readCsv(NOTION_CSV)
  const map = readCsv(MAP_CSV)
  const nameToFolder = new Map(
    map.filter((r) => r.Folder).map((r) => [r.Name.trim(), r.Folder.trim()]),
  )
  log(`notion rows: ${notion.length} | join map: ${nameToFolder.size}`)

  const problems = await prisma.problem.findMany({ select: { id: true, folderPath: true } })
  const byPath = new Map(problems.map((p) => [p.folderPath, p.id]))

  let enriched = 0, unmatched: string[] = [], notes = 0, reviews = 0
  const patternsSeen = new Set<string>()

  for (const row of notion) {
    const name = (row.Name ?? '').trim()
    const folder = nameToFolder.get(name)
    const problemId = folder ? byPath.get(folder) : undefined
    if (!problemId) { unmatched.push(name); continue }

    await prisma.problem.update({
      where: { id: problemId },
      data: {
        difficulty: row.Difficulty?.trim() || null,
        difficultyRank: difficultyRank(row.Difficulty?.trim()),
        source: row.Source?.trim() || null,
        judgeUrl: row.Link?.trim() || null,
        // Notion's title is more readable than the folder name
        title: name,
        createdAt: parseDate(row['Created time']) ?? undefined,
      },
    })
    enriched++

    for (const raw of splitTags(row.Topics)) {
      const display = TOPIC_ALIAS[raw] ?? raw
      const topic = await prisma.topic.upsert({
        where: { slug: slugify(display) },
        update: {},
        create: { name: display, slug: slugify(display) },
      })
      await prisma.problemTopic.upsert({
        where: { problemId_topicId: { problemId, topicId: topic.id } },
        update: {},
        create: { problemId, topicId: topic.id },
      })
    }

    for (const raw of splitTags(row.Form)) {
      const display = PATTERN_FIX[raw] ?? raw
      patternsSeen.add(display)
      const pat = await prisma.pattern.upsert({
        where: { slug: slugify(display) },
        update: {},
        create: { name: display, slug: slugify(display) },
      })
      await prisma.problemPattern.upsert({
        where: { problemId_patternId: { problemId, patternId: pat.id } },
        update: {},
        create: { problemId, patternId: pat.id },
      })
    }

    const framework = row.Framework?.trim() || null
    const body = row.Notes?.trim() || null
    const mistakes = row.Mistakes?.trim() || null
    if (framework || body || mistakes) {
      await prisma.note.upsert({
        where: { problemId },
        update: { framework, notes: body, mistakes },
        create: { problemId, framework, notes: body, mistakes },
      })
      notes++
    }

    // Seed scheduling from Notion's Due At so Phase 3 does not restart at
    // zero. Only ever seeds a card that has never been graded here —
    // a real review in this app always wins over the imported date.
    const due = parseDate(row['Due At'])
    if (due) {
      // "Already graded here" means a rating exists — that is the actual
      // definition, and unlike the `seeded` flag it cannot be wrong for
      // rows that predate the flag.
      const graded = await prisma.review.findFirst({
        where: { problemId, outcome: { not: null } },
      })
      if (!graded) {
        const created = parseDate(row['Created time'])
        const c = seedLadder({ due, createdAt: created })
        const fields = {
          due: new Date(c.due),
          step: c.step,
          reps: c.reps,
          lastReview: c.lastReviewedAt ? new Date(c.lastReviewedAt) : null,
        }
        await prisma.card.upsert({
          where: { problemId },
          update: fields,
          create: { problemId, ...fields },
        })
        // Drop any previous un-graded placeholder so re-running the import
        // does not accumulate duplicates.
        await prisma.review.deleteMany({ where: { problemId, outcome: null } })
        await prisma.review.create({
          data: {
            problemId,
            dueAt: due,
            seeded: true,
            step: c.step,
            reviewedAt: c.lastReviewedAt ? new Date(c.lastReviewedAt) : new Date(),
          },
        })
        reviews++
      }
    }
  }

  log(`enriched ${enriched} problems | notes ${notes} | seeded reviews ${reviews}`)
  log(`patterns discovered: ${patternsSeen.size}`)
  if (unmatched.length) {
    log(`\n  ${unmatched.length} Notion row(s) did not resolve to a problem:`)
    for (const u of unmatched) log(`    ! ${u}`)
  }
  return { enriched, notes, reviews, unmatched }
}

const isMain = process.argv[1] && import.meta.url.endsWith(path.basename(process.argv[1]))
if (isMain) {
  importNotion()
    .then(() => prisma.$disconnect())
    .catch(async (e) => { console.error(e); await prisma.$disconnect(); process.exit(1) })
}
