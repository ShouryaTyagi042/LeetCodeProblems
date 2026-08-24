/**
 * Spaced repetition scheduling, wrapping the reference FSRS
 * implementation (ts-fsrs).
 *
 * Lives in `shared` because both sides need it: the API computes the new
 * state when you grade, and the client previews "Good -> 12d" on the
 * buttons before you commit. React Native will import this untouched.
 */
import {
  createEmptyCard, fsrs, generatorParameters, Rating, State,
  type Card as FsrsCard, type Grade,
} from 'ts-fsrs'

export const RATINGS = ['again', 'hard', 'good', 'easy'] as const
export type RatingName = (typeof RATINGS)[number]

/** 1..4, matching FSRS and the number keys in the review UI. */
export const RATING_VALUE: Record<RatingName, number> = {
  again: 1, hard: 2, good: 3, easy: 4,
}
export const RATING_NAME: Record<number, RatingName> = {
  1: 'again', 2: 'hard', 3: 'good', 4: 'easy',
}

export const CARD_STATES = ['new', 'learning', 'review', 'relearning'] as const
export type CardState = (typeof CARD_STATES)[number]

const STATE_TO_NAME: Record<number, CardState> = {
  [State.New]: 'new',
  [State.Learning]: 'learning',
  [State.Review]: 'review',
  [State.Relearning]: 'relearning',
}
const NAME_TO_STATE: Record<CardState, State> = {
  new: State.New,
  learning: State.Learning,
  review: State.Review,
  relearning: State.Relearning,
}

/**
 * `enable_fuzz` spreads same-day clusters so a batch of problems reviewed
 * together does not come back on one single day forever.
 * `maximum_interval` is capped well under the FSRS default (~36500d): a
 * DSA pattern you last touched three years ago is not really "known", and
 * an interview is never that far away.
 */
export const FSRS_PARAMS = generatorParameters({
  enable_fuzz: true,
  enable_short_term: true,
  maximum_interval: 365,
})

const scheduler = fsrs(FSRS_PARAMS)

/** Plain, serialisable card state — what the DB stores and the API returns. */
export interface SrsCard {
  due: string
  stability: number
  difficulty: number
  elapsedDays: number
  scheduledDays: number
  reps: number
  lapses: number
  state: CardState
  lastReview: string | null
}

export function newCard(now: Date = new Date()): SrsCard {
  return fromFsrs(createEmptyCard(now))
}

function toFsrs(c: SrsCard): FsrsCard {
  return {
    due: new Date(c.due),
    stability: c.stability,
    difficulty: c.difficulty,
    elapsed_days: c.elapsedDays,
    scheduled_days: c.scheduledDays,
    reps: c.reps,
    lapses: c.lapses,
    state: NAME_TO_STATE[c.state],
    last_review: c.lastReview ? new Date(c.lastReview) : undefined,
    learning_steps: 0,
  } as FsrsCard
}

function fromFsrs(c: FsrsCard): SrsCard {
  return {
    due: c.due.toISOString(),
    stability: c.stability,
    difficulty: c.difficulty,
    elapsedDays: c.elapsed_days,
    scheduledDays: c.scheduled_days,
    reps: c.reps,
    lapses: c.lapses,
    state: STATE_TO_NAME[c.state] ?? 'new',
    lastReview: c.last_review ? new Date(c.last_review).toISOString() : null,
  }
}

/** Apply a grade and return the resulting card state. */
export function grade(card: SrsCard, rating: RatingName, now: Date = new Date()): SrsCard {
  const out = scheduler.next(toFsrs(card), now, RATING_VALUE[rating] as Grade)
  return fromFsrs(out.card)
}

/** What each button would do, for the interval previews in the UI. */
export function preview(
  card: SrsCard,
  now: Date = new Date(),
): Record<RatingName, { due: string; label: string }> {
  const s = scheduler.repeat(toFsrs(card), now)
  const pick = (r: Rating.Again | Rating.Hard | Rating.Good | Rating.Easy) => {
    const due = (s as any)[r].card.due as Date
    return { due: new Date(due).toISOString(), label: humanInterval(now, new Date(due)) }
  }
  return {
    again: pick(Rating.Again),
    hard: pick(Rating.Hard),
    good: pick(Rating.Good),
    easy: pick(Rating.Easy),
  }
}

/** Compact interval label: 10m, 2h, 4d, 3mo, 1.2y */
export function humanInterval(from: Date, to: Date): string {
  const mins = Math.round((to.getTime() - from.getTime()) / 60000)
  if (mins < 1) return 'now'
  if (mins < 60) return `${mins}m`
  const hours = mins / 60
  if (hours < 24) return `${Math.round(hours)}h`
  const days = hours / 24
  if (days < 30) return `${Math.round(days)}d`
  const months = days / 30
  if (months < 12) return `${Math.round(months)}mo`
  return `${(days / 365).toFixed(1)}y`
}

/**
 * Seed a card from a due date carried over from elsewhere (the Notion
 * `Due At` column). The interval that produced that date is unknown, so
 * approximate stability with the created -> due gap: wrong in detail, but
 * far closer than restarting every problem from zero.
 */
export function seedCard(opts: {
  due: Date
  createdAt?: Date | null
  now?: Date
}): SrsCard {
  const now = opts.now ?? new Date()
  const created = opts.createdAt ?? null
  const days = created
    ? Math.max(1, Math.round((opts.due.getTime() - created.getTime()) / 86400000))
    : 7
  const stability = Math.min(days, FSRS_PARAMS.maximum_interval)
  return {
    due: opts.due.toISOString(),
    stability,
    difficulty: 5, // FSRS difficulty is 1..10; 5 is neutral
    elapsedDays: 0,
    scheduledDays: Math.round(stability),
    reps: 1,
    lapses: 0,
    state: 'review',
    lastReview: created ? created.toISOString() : now.toISOString(),
  }
}
