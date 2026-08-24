import type { ProblemDetail, ProblemSummary, Tag } from '@tracker/shared'

const tag = (t: { id: string; name: string; slug: string }): Tag => ({
  id: t.id, name: t.name, slug: t.slug,
})

export function toSummary(p: any): ProblemSummary {
  return {
    id: p.id,
    slug: p.slug,
    title: p.title,
    folderPath: p.folderPath,
    source: p.source,
    judgeUrl: p.judgeUrl,
    difficulty: p.difficulty,
    status: p.status,
    topics: (p.topics ?? []).map((x: any) => tag(x.topic)),
    patterns: (p.patterns ?? []).map((x: any) => tag(x.pattern)),
    hasNote: Boolean(
      p.note && (p.note.framework || p.note.notes || p.note.mistakes),
    ),
    loc: (p.solutions ?? []).reduce((a: number, s: any) => a + (s.loc ?? 0), 0),
  }
}

export function toDetail(p: any): ProblemDetail {
  return {
    ...toSummary(p),
    createdAt: p.createdAt?.toISOString?.() ?? String(p.createdAt),
    firstSolvedAt: p.firstSolvedAt?.toISOString?.() ?? null,
    note: p.note
      ? {
          framework: p.note.framework,
          notes: p.note.notes,
          mistakes: p.note.mistakes,
          updatedAt: p.note.updatedAt?.toISOString?.(),
        }
      : null,
    solutions: (p.solutions ?? []).map((s: any) => ({
      id: s.id, language: s.language, code: s.code,
      filePath: s.filePath, loc: s.loc,
      syncedAt: s.syncedAt?.toISOString?.() ?? String(s.syncedAt),
    })),
    testcases: (p.testcases ?? []).map((t: any) => ({
      id: t.id, input: t.input, expected: t.expected, ord: t.ord,
    })),
  }
}

export const DETAIL_INCLUDE = {
  topics: { include: { topic: true } },
  patterns: { include: { pattern: true } },
  solutions: true,
  testcases: { orderBy: { ord: 'asc' } },
  note: true,
} as const

export const SUMMARY_INCLUDE = {
  topics: { include: { topic: true } },
  patterns: { include: { pattern: true } },
  solutions: { select: { loc: true } },
  note: { select: { framework: true, notes: true, mistakes: true } },
} as const
