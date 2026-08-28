// Domain types shared by the API, the web app, and (later) React Native.
// Deliberately independent of Prisma so clients never import the ORM.

export const DIFFICULTIES = ['Easy', 'Medium', 'Hard'] as const
export type Difficulty = (typeof DIFFICULTIES)[number]

/**
 * Stand-in creation date for problems whose real one is unknown — anything
 * the filesystem scan finds that has no Notion row. Without it these rows
 * take `now()`, which makes every unknown problem look like it was created
 * the day the database was built and swamps the top of a Created sort.
 * A fixed date in the past groups them honestly instead.
 */
export const UNKNOWN_CREATED_AT = '2026-01-01T00:00:00.000Z'

/** Difficulty as a sortable number; alphabetical order would be wrong. */
export const DIFFICULTY_RANK: Record<Difficulty, number> = {
  Easy: 1, Medium: 2, Hard: 3,
}
export function difficultyRank(d?: string | null): number | null {
  return d && d in DIFFICULTY_RANK ? DIFFICULTY_RANK[d as Difficulty] : null
}

// ---------- sorting ----------

// 'due' is deliberately absent: it lives on the Card relation, and Prisma
// cannot place nulls last through a relation, so ascending would always
// lead with the problems that have no review scheduled at all. Sorting by
// it correctly needs Problem.dueAt denormalised — not worth the drift risk
// until it is actually wanted.
export const SORT_FIELDS = [
  'title', 'difficulty', 'created', 'source',
] as const
export type SortField = (typeof SORT_FIELDS)[number]

export const SORT_LABEL: Record<SortField, string> = {
  title: 'Title',
  difficulty: 'Difficulty',
  created: 'Created',
  source: 'Source',
}

/** What asc/desc mean for each field, so the UI can say it plainly. */
export const SORT_DIR_LABEL: Record<SortField, { asc: string; desc: string }> = {
  title: { asc: 'A → Z', desc: 'Z → A' },
  difficulty: { asc: 'Easy first', desc: 'Hard first' },
  created: { asc: 'Oldest first', desc: 'Newest first' },
  source: { asc: 'A → Z', desc: 'Z → A' },
}

export type SortDir = 'asc' | 'desc'
export interface SortSpec { field: SortField; dir: SortDir }

/**
 * Applied when the URL carries no sort. Defined here rather than in the
 * server or the UI so the two cannot disagree about what "default" means
 * and show one order while applying another.
 */
export const DEFAULT_SORT: readonly SortSpec[] = [
  { field: 'created', dir: 'desc' },
]

/** True when `specs` is just the default, so the URL can stay clean. */
export function isDefaultSort(specs: SortSpec[]): boolean {
  return (
    specs.length === DEFAULT_SORT.length &&
    specs.every((s, i) => s.field === DEFAULT_SORT[i].field && s.dir === DEFAULT_SORT[i].dir)
  )
}

/** Parse 'difficulty:desc,created:asc'. Tolerates the old single-value forms. */
export function parseSort(raw?: string | null): SortSpec[] {
  if (!raw) return []
  const out: SortSpec[] = []
  for (const part of raw.split(',')) {
    const [f, d] = part.trim().split(':')
    if (!f) continue
    // Legacy value from before multi-sort existed.
    if (f === 'recent') { out.push({ field: 'created', dir: 'desc' }); continue }
    if (!(SORT_FIELDS as readonly string[]).includes(f)) continue
    const field = f as SortField
    if (out.some((s) => s.field === field)) continue // no duplicate keys
    out.push({ field, dir: d === 'desc' ? 'desc' : 'asc' })
  }
  return out
}

export function formatSort(specs: SortSpec[]): string {
  return specs.map((s) => `${s.field}:${s.dir}`).join(',')
}

export interface Tag {
  id: string
  name: string
  slug: string
  count?: number
}

export interface Testcase {
  id: string
  input: string
  expected: string
  ord: number
}

export interface Solution {
  id: string
  language: string
  code: string
  filePath: string
  loc: number
  syncedAt: string
}

export interface Note {
  framework: string | null
  notes: string | null
  mistakes: string | null
  updatedAt?: string
}

/** Row shape for the list view — no code, so the payload stays small. */
export interface ProblemSummary {
  id: string
  slug: string
  title: string
  folderPath: string
  source: string | null
  judgeUrl: string | null
  difficulty: Difficulty | null
  topics: Tag[]
  patterns: Tag[]
  hasNote: boolean
  loc: number
  createdAt: string
  /** Next scheduled review, or null if the problem has no card. */
  dueAt: string | null
}

/** Full detail — includes code and test cases. */
export interface ProblemDetail extends ProblemSummary {
  firstSolvedAt: string | null
  note: Note | null
  solutions: Solution[]
  testcases: Testcase[]
}

export interface ProblemQuery {
  q?: string
  topic?: string
  pattern?: string
  difficulty?: Difficulty
  source?: string
  sort?: 'title' | 'recent' | 'difficulty'
  page?: number
  perPage?: number
}

export interface Paged<T> {
  items: T[]
  total: number
  page: number
  perPage: number
}

export interface Facets {
  topics: Tag[]
  patterns: Tag[]
  sources: { name: string; count: number }[]
  difficulties: { name: string; count: number }[]
}

export interface Stats {
  problems: number
  withNotes: number
  withMistakes: number
  topics: number
  patterns: number
  totalLoc: number
}

/** Phase 2 write payloads. */
export interface UpdateProblemInput {
  title?: string
  source?: string | null
  judgeUrl?: string | null
  difficulty?: Difficulty | null
  topics?: string[]
  patterns?: string[]
}

export interface UpdateNoteInput {
  framework?: string | null
  notes?: string | null
  mistakes?: string | null
}

export interface CreateProblemInput {
  title: string
  topic: string
  folderName?: string
  difficulty?: Difficulty | null
  source?: string | null
  judgeUrl?: string | null
  scaffold?: boolean
}

// ---------- Phase 3: spaced repetition ----------

export interface CardInfo {
  due: string
  /** Rung of the shared ladder to apply next. */
  step: number
  reps: number
  lapses: number
  lastReview: string | null
  suspended: boolean
  /** Negative when the card is not due yet. */
  overdueDays: number
  /** Interval a successful review would schedule, e.g. '2 weeks'. */
  nextIntervalLabel: string
}

export interface ReviewQueueItem {
  problem: ProblemSummary
  card: CardInfo
}

export interface ReviewQueue {
  items: ReviewQueueItem[]
  /** Total due right now, which may exceed the returned page. */
  dueTotal: number
  newTotal: number
}

export interface ReviewStats {
  due: number
  /** Due for more than a day — the part of `due` that is a genuine backlog. */
  backlog: number
  /** Never reviewed here (still on the bottom rung with no reps). */
  newCards: number
  reviewedToday: number
  totalCards: number
  suspended: number
  /** Share of reviews in the last 30 days marked good, or null if none. */
  retention30d: number | null
}

export interface ForecastDay {
  date: string
  count: number
}

export interface GradeResult {
  card: CardInfo
}

/** A topic as a lens on the review queue — counts only, no schedule. */
export interface TopicDueRow {
  topic: Tag
  problems: number
  /** Problems in this topic due now. */
  due: number
  /** Problems never reviewed here. */
  fresh: number
  /** Earliest upcoming due date among its problems, or null. */
  nextDue: string | null
}

export interface ProblemSyncResult {
  ok: true
  folderPath: string
  /** .java files read from the folder. */
  files: number
  /** Rows dropped because the file no longer exists. */
  removedFiles: number
  testcases: number
  loc: number
  cardCreated: boolean
  problem: ProblemDetail
}
