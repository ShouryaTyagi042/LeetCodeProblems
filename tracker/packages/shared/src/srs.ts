/**
 * Scheduling — one fixed ladder, applied per problem.
 *
 * Topics are only a filter over this queue; they carry no schedule of their
 * own. Whether a technique needs work is answered by how many of its
 * problems are due, not by a separate judgement about the topic.
 *
 * 3 days → 1 week → 2 weeks → 1 month, then monthly. Past the last rung the
 * interval stays put rather than growing: a technique you might be asked
 * about in an interview is never really "finished", and the open-ended
 * intervals a model like FSRS produces (years, in some cases) are not
 * useful here.
 */

export const LADDER_DAYS = [3, 7, 14, 30] as const
export const LADDER_LABELS = ['3 days', '1 week', '2 weeks', '1 month'] as const

export const OUTCOMES = ['again', 'good'] as const
export type Outcome = (typeof OUTCOMES)[number]

/** Days until the next review at a given rung. */
export function intervalDays(step: number): number {
  const i = Math.max(0, Math.min(step, LADDER_DAYS.length - 1))
  return LADDER_DAYS[i]
}

export function intervalLabel(step: number): string {
  const i = Math.max(0, Math.min(step, LADDER_LABELS.length - 1))
  return LADDER_LABELS[i]
}

export interface LadderState {
  /** Rung to APPLY next, not the one just used — a never-reviewed item sits
   *  at 0 so its first review schedules 3 days rather than skipping ahead. */
  step: number
  reps: number
  due: string
  lastReviewedAt: string | null
}

/**
 * Apply an outcome. 'good' uses the current rung then climbs one (capped at
 * the top, so it repeats monthly). 'again' drops back to the bottom —
 * something you could not recall is due again in days, not a month.
 */
export function advance(
  state: Pick<LadderState, 'step' | 'reps'>,
  outcome: Outcome,
  now: Date = new Date(),
): LadderState {
  const applied = outcome === 'again' ? 0 : state.step
  const days = intervalDays(applied)
  const nextStep =
    outcome === 'again' ? 0 : Math.min(applied + 1, LADDER_DAYS.length - 1)
  return {
    step: nextStep,
    reps: state.reps + 1,
    due: new Date(now.getTime() + days * 86400000).toISOString(),
    lastReviewedAt: now.toISOString(),
  }
}

/** A brand-new item is due immediately, at the bottom rung. */
export function newLadderState(now: Date = new Date()): LadderState {
  return { step: 0, reps: 0, due: now.toISOString(), lastReviewedAt: null }
}

export const newCard = newLadderState

/** Compact interval label: 3d, 2w, 1mo. */
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
 * Place an item on the ladder from a due date carried in from elsewhere
 * (Notion's `Due At`). The rung is inferred from how long the previous
 * interval appears to have been, so an item already on a monthly cadence is
 * not knocked back to 3 days.
 */
export function seedLadder(opts: {
  due: Date
  createdAt?: Date | null
}): LadderState {
  const created = opts.createdAt ?? null
  const gapDays = created
    ? Math.max(1, Math.round((opts.due.getTime() - created.getTime()) / 86400000))
    : 7
  // Which rung does that gap look like? Take the next one up from there.
  let rung = 0
  for (let i = 0; i < LADDER_DAYS.length; i++) {
    if (gapDays >= LADDER_DAYS[i]) rung = i
  }
  return {
    step: Math.min(rung + 1, LADDER_DAYS.length - 1),
    reps: 1,
    due: opts.due.toISOString(),
    lastReviewedAt: created ? created.toISOString() : null,
  }
}

export const seedCard = seedLadder
