// Domain types shared by the API, the web app, and (later) React Native.
// Deliberately independent of Prisma so clients never import the ORM.

export const DIFFICULTIES = ['Easy', 'Medium', 'Hard'] as const
export type Difficulty = (typeof DIFFICULTIES)[number]

export const STATUSES = ['unsolved', 'solved', 'needs_review'] as const
export type Status = (typeof STATUSES)[number]

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
  status: Status
  topics: Tag[]
  patterns: Tag[]
  hasNote: boolean
  loc: number
}

/** Full detail — includes code and test cases. */
export interface ProblemDetail extends ProblemSummary {
  createdAt: string
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
  status?: Status
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
  statuses: { name: string; count: number }[]
}

export interface Stats {
  problems: number
  solved: number
  unsolved: number
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
  status?: Status
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
  stability: number
  difficulty: number
  reps: number
  lapses: number
  state: 'new' | 'learning' | 'review' | 'relearning'
  lastReview: string | null
  suspended: boolean
  /** Negative when the card is not due yet. */
  overdueDays: number
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
  newCards: number
  reviewedToday: number
  totalCards: number
  suspended: number
  /** Correct share of graded reviews in the last 30 days, or null if none. */
  retention30d: number | null
}

export interface ForecastDay {
  date: string
  count: number
}

export interface GradeResult {
  card: CardInfo
  /** What each button would do next, for the following card render. */
  intervals: Record<string, { due: string; label: string }>
}
