import { Suspense, lazy, useCallback, useEffect, useRef, useState } from 'react'
import { Link, useNavigate, useSearchParams } from 'react-router-dom'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { LADDER_LABELS, type CardInfo, type Outcome, type ReviewQueue } from '@tracker/shared'
import { api } from '../lib/api'
import { Button, Chip, buttonCls, cx, difficultyTone } from '../components/ui'
import { ArrowLeft, ChevronDown, ChevronRight, ExternalLink, Lightbulb, Refresh, Shuffle } from '../components/icons'
import Markdown from '../components/Markdown'

const CodeView = lazy(() => import('../components/CodeView'))

/** Same two outcomes as topic revision, so the whole app schedules alike. */
const OUTCOME_UI: { key: Outcome; label: string; hint: string; cls: string }[] = [
  {
    key: 'again',
    label: 'Shaky',
    hint: 'back to 3 days',
    cls: 'border-[#7d2429] bg-[#3d1418] text-[#f85149] hover:bg-[#5a1d22]',
  },
  {
    key: 'good',
    label: 'Solid',
    hint: '',
    cls: 'border-[#1f6f34] bg-[#0f2f1a] text-[#56d364] hover:bg-[#17472a]',
  },
]

/**
 * A one-card queue for revising a single problem on demand, from its own
 * page. It is graded through the same endpoint as the real queue, so it
 * moves the problem along the same ladder and counts as a normal review.
 */
async function singleQueue(slug: string): Promise<ReviewQueue> {
  const problem = await api.getProblem(slug)
  const card = await api.reviewCard(problem.id)
  return {
    items: [{ problem, card }],
    dueTotal: card.overdueDays >= 0 ? 1 : 0,
    newTotal: card.reps === 0 ? 1 : 0,
  }
}

type QueueItem = ReviewQueue['items'][number]

/** Fisher–Yates; returns a new array. */
function shuffled<T>(xs: readonly T[]): T[] {
  const a = [...xs]
  for (let i = a.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1))
    ;[a[i], a[j]] = [a[j], a[i]]
  }
  return a
}

/**
 * Shuffle the cards still to come, `from` onwards, leaving the ones already
 * graded where they are. The card on screen is shuffled too, but never left
 * on screen — pressing shuffle should always change the question.
 */
function shuffleRest(items: QueueItem[], from: number): QueueItem[] {
  const rest = items.slice(from)
  if (rest.length < 2) return items
  const out = shuffled(rest)
  if (out[0] === rest[0]) {
    const j = 1 + Math.floor(Math.random() * (out.length - 1))
    ;[out[0], out[j]] = [out[j], out[0]]
  }
  return [...items.slice(0, from), ...out]
}

export default function ReviewPage() {
  const qc = useQueryClient()
  const nav = useNavigate()
  const [sp] = useSearchParams()
  const topic = sp.get('topic') ?? undefined
  const single = sp.get('problem') ?? undefined
  // Set once a single-problem revision is graded, to show where it landed.
  const [singleResult, setSingleResult] = useState<CardInfo | null>(null)
  const [idx, setIdx] = useState(0)
  const [revealed, setRevealed] = useState(false)
  // Two independent hints, because they give away different amounts. The
  // topic narrows the field; the pattern names the technique outright,
  // so it is worth being able to take the first without the second.
  const [hinted, setHinted] = useState(false)
  const [patternHinted, setPatternHinted] = useState(false)
  // Not reset between cards: collapsing the solution is a preference for the
  // sitting, not a per-problem state like the hints. Collapse once and it
  // stays collapsed for the rest of the session.
  const [codeOpen, setCodeOpen] = useState(true)
  const [done, setDone] = useState(0)
  const startedAt = useRef(Date.now())

  const queue = useQuery({
    queryKey: ['review', 'queue', topic, single],
    queryFn: () => (single ? singleQueue(single) : api.reviewQueue(30, topic)),
    // Scheduling changes on every grade; never serve this from cache.
    staleTime: 0,
    refetchOnMount: 'always',
  })

  // The order cards are shown in. It is the server's most-overdue-first order
  // until shuffle is pressed; from then on every queue fetched this session
  // is shuffled too, so working through to a refill does not quietly switch
  // back to due order.
  const [shuffleOn, setShuffleOn] = useState(false)
  const [orderSrc, setOrderSrc] = useState<ReviewQueue | undefined>(undefined)
  const [order, setOrder] = useState<QueueItem[]>([])
  if (queue.data !== orderSrc) {
    // Derived during render rather than in an effect, so a fresh queue never
    // paints a frame with the previous order (or with "Nothing due").
    setOrderSrc(queue.data)
    const items = queue.data?.items ?? []
    setOrder(shuffleOn ? shuffled(items) : items)
  }

  const current = order[idx]

  // Full detail (code, notes) only for the card on screen, plus a prefetch
  // of the next one so revealing never waits on the network.
  const detail = useQuery({
    queryKey: ['problem', current?.problem.slug],
    queryFn: () => api.getProblem(current!.problem.slug),
    enabled: Boolean(current),
  })

  const next = order[idx + 1]
  useEffect(() => {
    if (!next) return
    qc.prefetchQuery({
      queryKey: ['problem', next.problem.slug],
      queryFn: () => api.getProblem(next.problem.slug),
    })
  }, [next, qc])

  // Keyed on the card, not the index: shuffling swaps the card on screen
  // without moving the index, and that is a new question all the same.
  const currentId = current?.problem.id
  useEffect(() => {
    setRevealed(false)
    setHinted(false)
    setPatternHinted(false)
    startedAt.current = Date.now()
  }, [currentId])

  // Re-reads this problem's files from disk, the same as the button on the
  // full page — for when the solution has been edited since the card was
  // built and the code on screen is stale.
  const syncMut = useMutation({
    mutationFn: () => api.syncProblem(current!.problem.id),
    onSuccess: (res) => {
      qc.setQueryData(['problem', current!.problem.slug], res.problem)
      qc.invalidateQueries({ queryKey: ['problems'] })
      qc.invalidateQueries({ queryKey: ['stats'] })
      // Deliberately not ['review','queue']: refetching it mid-session would
      // reshuffle the order and move a different problem under the card you
      // are part way through. Grading avoids it for the same reason.
      qc.invalidateQueries({ queryKey: ['review', 'stats'] })
      qc.invalidateQueries({ queryKey: ['review', 'forecast'] })
    },
  })

  // Clear the result line when moving on, so it does not read as if it
  // belonged to the next problem.
  useEffect(() => {
    syncMut.reset()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentId])

  const gradeMut = useMutation({
    mutationFn: (outcome: Outcome) =>
      api.gradeProblem(current!.problem.id, outcome, Date.now() - startedAt.current),
    onSuccess: (res) => {
      setDone((d) => d + 1)
      qc.invalidateQueries({ queryKey: ['review', 'stats'] })
      qc.invalidateQueries({ queryKey: ['review', 'forecast'] })
      if (single) {
        // The problem page shows the next review date, and the dashboard
        // counts this toward the streak; both are stale now.
        qc.invalidateQueries({ queryKey: ['problem', single] })
        qc.invalidateQueries({ queryKey: ['problems'] })
        qc.invalidateQueries({ queryKey: ['dashboard'] })
        setSingleResult(res.card)
        return
      }
      if (idx + 1 < order.length) setIdx((i) => i + 1)
      else queue.refetch().then(() => setIdx(0))
    },
  })

  const canShuffle = !single && order.length - idx > 1
  const shuffleQueue = useCallback(() => {
    if (!canShuffle || gradeMut.isPending) return
    setShuffleOn(true)
    setOrder((o) => shuffleRest(o, idx))
  }, [canShuffle, idx, gradeMut.isPending])

  const submit = useCallback(
    (r: Outcome) => {
      if (!current || gradeMut.isPending) return
      if (!revealed) { setRevealed(true); return }
      gradeMut.mutate(r)
    },
    [current, revealed, gradeMut],
  )

  useEffect(() => {
    const onKey = (e: KeyboardEvent) => {
      const el = e.target as HTMLElement
      if (el && /INPUT|TEXTAREA|SELECT/.test(el.tagName)) return
      if (singleResult) return
      // Leave anything held with a modifier to the browser. e.key is the bare
      // letter whether or not cmd is down, so without this every shortcut here
      // also fires on its cmd- equivalent — and preventDefault then swallows
      // the real one. Cmd+C collapsed the solution instead of copying it.
      if (e.metaKey || e.ctrlKey || e.altKey) return
      if (e.key === ' ' || e.key === 'Enter') { e.preventDefault(); setRevealed(true); return }
      if (e.key === 'h' || e.key === 'H') { e.preventDefault(); setHinted((v) => !v); return }
      if (e.key === 'p' || e.key === 'P') { e.preventDefault(); setPatternHinted((v) => !v); return }
      if (e.key === 's' || e.key === 'S') { e.preventDefault(); shuffleQueue(); return }
      const n = Number(e.key)
      if (n >= 1 && n <= OUTCOME_UI.length && revealed) {
        e.preventDefault()
        submit(OUTCOME_UI[n - 1].key)
      }
    }
    window.addEventListener('keydown', onKey)
    return () => window.removeEventListener('keydown', onKey)
  }, [revealed, submit, singleResult, shuffleQueue])

  if (queue.isLoading) {
    return <div className="py-20 text-center text-sm text-[#6e7681]">Loading queue…</div>
  }

  if (single && (queue.isError || singleResult)) {
    const back = `/problems/${single}?mode=edit`
    return (
      <div className="mx-auto max-w-md py-20 text-center">
        {singleResult ? (
          <>
            <div className="mb-2 text-3xl">✓</div>
            <h1 className="mb-1 text-lg font-semibold">Revised</h1>
            <p className="mb-5 text-[13px] text-[#8b949e]">
              Next review {new Date(singleResult.due).toLocaleDateString(undefined, {
                weekday: 'short', day: 'numeric', month: 'short',
              })}.
            </p>
          </>
        ) : (
          <p className="mb-5 text-[13px] text-[#f85149]">
            {String((queue.error as Error).message)}
          </p>
        )}
        <div className="flex justify-center gap-2">
          <Button variant="primary" onClick={() => nav(back)}>Back to problem</Button>
          <Link to="/review" className={buttonCls()}>Review queue</Link>
        </div>
      </div>
    )
  }

  if (!current) {
    return (
      <div className="mx-auto max-w-md py-20 text-center">
        <div className="mb-2 text-3xl">✓</div>
        <h1 className="mb-1 text-lg font-semibold">Nothing due</h1>
        <p className="mb-5 text-[13px] text-[#8b949e]">
          {done > 0
            ? `${done} review${done === 1 ? '' : 's'} done this session.`
            : topic
              ? 'Nothing due in this topic.'
              : 'Everything is scheduled for later.'}
        </p>
        <div className="flex justify-center gap-2">
          <Link to="/topics" className={buttonCls()}>Topics</Link>
          <Link to="/problems" className={buttonCls()}>Browse problems</Link>
        </div>
      </div>
    )
  }

  const p = current.problem
  const note = detail.data?.note
  const sol = detail.data?.solutions?.[0]
  const overdue = current.card.overdueDays
  const showHint = hinted || revealed
  const showPatterns = patternHinted || revealed
  const daysUntilDue = Math.ceil((new Date(current.card.due).getTime() - Date.now()) / 86400000)

  return (
    <div className="mx-auto max-w-3xl">
      <div className="mb-4 flex items-center gap-3 text-[12px] text-[#8b949e]">
        {single ? (
          <>
            <Link to={`/problems/${single}?mode=edit`} className={cx(buttonCls('ghost'), '-ml-3')}>
              <ArrowLeft size={14} />Back to problem
            </Link>
            <span>Revising one problem</span>
          </>
        ) : (
          <>
            {topic && (
              <Link to="/topics" className={cx(buttonCls('ghost'), '-ml-3')}>
                <ArrowLeft size={14} />All topics
              </Link>
            )}
            <span><b className="text-[#e6edf3]">{queue.data!.dueTotal}</b> due{topic ? ' in this topic' : ''}</span>
            <span>·</span>
            <span><b className="text-[#e6edf3]">{done}</b> done</span>
          </>
        )}
        <div className="ml-auto flex items-center gap-2">
          <Chip>{current.card.reps === 0 ? 'new' : `seen ${current.card.reps}×`}</Chip>
          {overdue > 0 && <Chip tone="amber">{overdue}d overdue</Chip>}
          {/* Revising early still moves the ladder, the same as a due one. */}
          {daysUntilDue > 0 && <Chip>due in {daysUntilDue}d</Chip>}
        </div>
      </div>

      <div className="rounded-lg border border-[#262d36] bg-[#0d1117]">
        <div className="border-b border-[#262d36] px-5 py-4">
          <div className="flex flex-wrap items-center gap-2">
            <h1 className="text-lg font-semibold">{p.title}</h1>
            {p.difficulty && <Chip tone={difficultyTone(p.difficulty)}>{p.difficulty}</Chip>}
            {p.source && <Chip>{p.source}</Chip>}
          </div>

          {/* Whatever has been unlocked so far. The folder path rides with the
              topic hint because it reads topics/Graphs/DisjointSetUnion — the
              topic and usually the pattern, spelled out. */}
          {(showHint || showPatterns) && (
            <div className="mt-2 flex flex-wrap items-center gap-1.5">
              {showHint && p.topics.map((t) => <Chip key={t.id}>{t.name}</Chip>)}
              {showPatterns && (
                p.patterns.length > 0
                  ? p.patterns.map((t) => <Chip key={t.id} tone="accent">{t.name}</Chip>)
                  : <span className="text-[11px] text-[#6e7681]">no patterns tagged</span>
              )}
              {showHint && (
                <span className="font-mono text-[11px] text-[#6e7681]">{p.folderPath}</span>
              )}
            </div>
          )}

          {/* Every action in one row: same variant height, one gap, one left
              edge. Split across rows they lined up on three different edges. */}
          <div className="mt-3 flex flex-wrap items-center gap-2">
            {p.judgeUrl && (
              <a href={p.judgeUrl} target="_blank" rel="noreferrer" className={buttonCls()}>
                Read the problem<ExternalLink size={13} />
              </a>
            )}
            <Link to={`/problems/${p.slug}?mode=edit`} className={buttonCls()}>
              Open full page
            </Link>
            {/* All one variant on purpose. Ghost buttons draw no border, so
                their label sits inside an invisible box — in a row next to a
                bordered button that reads as misaligned and unevenly spaced,
                even though the boxes line up exactly. */}
            {!showHint && (
              <button onClick={() => setHinted(true)}
                className={cx(buttonCls(), 'text-[#8b949e]')}>
                <Lightbulb size={13} />Topic hint
                <span className="opacity-60">(h)</span>
              </button>
            )}
            {!showPatterns && (
              <button onClick={() => setPatternHinted(true)}
                className={cx(buttonCls(), 'text-[#8b949e]')}>
                <Lightbulb size={13} />Pattern hint
                <span className="opacity-60">(p)</span>
              </button>
            )}
            {canShuffle && (
              <button onClick={shuffleQueue} disabled={gradeMut.isPending}
                title="Swap in a random problem from the queue"
                className={cx(buttonCls('default', gradeMut.isPending), 'text-[#8b949e]')}>
                <Shuffle size={13} />Shuffle
                <span className="opacity-60">(s)</span>
              </button>
            )}
            <button
              onClick={() => syncMut.mutate()}
              disabled={syncMut.isPending}
              className={cx(buttonCls('default', syncMut.isPending), 'text-[#8b949e]')}
            >
              <Refresh size={13} />
              {syncMut.isPending ? 'Reading files…' : 'Sync files'}
            </button>
            {syncMut.isSuccess && (
              <span className="text-[11px] text-[#56d364]">
                {syncMut.data.files} file{syncMut.data.files === 1 ? '' : 's'},{' '}
                {syncMut.data.loc} lines
                {syncMut.data.removedFiles > 0 && `, ${syncMut.data.removedFiles} removed`}
              </span>
            )}
            {syncMut.isError && (
              <span className="text-[11px] text-[#f85149]">
                {String((syncMut.error as Error).message)}
              </span>
            )}
          </div>
        </div>

        {!revealed ? (
          <div className="px-5 py-10 text-center">
            <p className="mb-1 text-[13px] text-[#8b949e]">
              Recall the approach before revealing.
            </p>
            <p className="mb-5 text-[11px] text-[#6e7681]">
              Which technique does this need, and why that one?
            </p>
            <Button variant="primary" onClick={() => setRevealed(true)}>
              Show answer<span className="opacity-60">(space)</span>
            </Button>
          </div>
        ) : (
          // Patterns are not repeated here: revealing implies both hints, so
          // they are already showing in the header by this point.
          <div className="space-y-4 px-5 py-4">
            {note?.framework && (
              <Section title="Framework">{note.framework}</Section>
            )}
            {note?.mistakes && (
              <Section title="Mistakes" tone="warn">{note.mistakes}</Section>
            )}
            {note?.notes && <Section title="Notes">{note.notes}</Section>}

            {sol ? (
              // No overflow-hidden on this wrapper: an overflowing ancestor
              // becomes the sticky element's scroll container, and since this
              // box does not scroll, the bar below would never stick to the
              // viewport. The code gets its own clipping wrapper instead.
              <div className="rounded-md border border-[#262d36]">
                {/* The whole bar toggles, so the target is the width of the
                    panel rather than a chevron the size of a full stop. It
                    parks under the app header while a long solution scrolls
                    past, so the collapse control is always in reach. */}
                <button
                  onClick={() => setCodeOpen((v) => !v)}
                  aria-expanded={codeOpen}
                  className={cx(
                    'sticky top-[var(--app-header-h)] z-10 flex w-full items-center gap-2',
                    'bg-[#161b22] px-3 py-1.5 text-left hover:bg-[#1c2129]',
                    codeOpen ? 'rounded-t-md border-b border-[#262d36]' : 'rounded-md',
                  )}
                >
                  {codeOpen ? <ChevronDown size={13} className="shrink-0 text-[#6e7681]" />
                            : <ChevronRight size={13} className="shrink-0 text-[#6e7681]" />}
                  <span className="truncate font-mono text-[11px] text-[#8b949e]">
                    {sol.filePath}
                  </span>
                  <span className="ml-auto shrink-0 text-[11px] text-[#6e7681]">
                    {sol.loc} lines{codeOpen ? '' : ' · hidden'}
                  </span>
                </button>
                {codeOpen && (
                  <div className="overflow-hidden rounded-b-md">
                    <Suspense fallback={<div className="p-6 text-[12px] text-[#6e7681]">Loading…</div>}>
                      <CodeView code={sol.code} />
                    </Suspense>
                  </div>
                )}
              </div>
            ) : detail.isLoading ? (
              <div className="py-6 text-center text-[12px] text-[#6e7681]">Loading solution…</div>
            ) : (
              <div className="rounded-md border border-[#262d36] p-6 text-center text-[12px] text-[#6e7681]">
                No solution file for this problem.
              </div>
            )}
          </div>
        )}
      </div>

      {revealed && (
        <div className="mt-4 grid grid-cols-2 gap-2">
          {OUTCOME_UI.map((o, i) => (
            <button
              key={o.key}
              disabled={gradeMut.isPending}
              onClick={() => submit(o.key)}
              className={cx(
                'rounded-md border px-3 py-2.5 text-[13px] font-medium transition disabled:opacity-50',
                o.cls,
              )}
            >
              <div>{o.label}</div>
              <div className="mt-0.5 text-[11px] font-normal opacity-70">
                {o.hint || current.card.nextIntervalLabel}{' '}
                <span className="opacity-60">({i + 1})</span>
              </div>
            </button>
          ))}
        </div>
      )}

      <p className="mt-3 text-center text-[11px] text-[#6e7681]">
        Ladder: {LADDER_LABELS.join(' → ')}, then monthly — the same one topic
        revision uses.
      </p>

      {gradeMut.isError && (
        <div className="mt-3 text-[12px] text-[#f85149]">
          {String((gradeMut.error as Error).message)}
        </div>
      )}
    </div>
  )
}

function Section({
  title, children, tone,
}: {
  title: string
  children: string
  tone?: 'warn'
}) {
  return (
    <div>
      <div className={cx(
        'mb-1 text-[11px] font-semibold uppercase tracking-wide',
        tone === 'warn' ? 'text-[#e3b341]' : 'text-[#8b949e]',
      )}>
        {title}
      </div>
      <div className="rounded-md border border-[#262d36] bg-[#161b22] px-3 py-2">
        <Markdown>{children}</Markdown>
      </div>
    </div>
  )
}
