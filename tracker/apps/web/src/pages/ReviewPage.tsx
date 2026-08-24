import { Suspense, lazy, useCallback, useEffect, useMemo, useRef, useState } from 'react'
import { Link } from 'react-router-dom'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { RATINGS, preview, type RatingName, type SrsCard } from '@tracker/shared'
import { api } from '../lib/api'
import { Button, Chip, cx, difficultyTone } from '../components/ui'

const CodeView = lazy(() => import('../components/CodeView'))

const RATING_STYLE: Record<RatingName, string> = {
  again: 'border-[#7d2429] bg-[#3d1418] text-[#f85149] hover:bg-[#5a1d22]',
  hard: 'border-[#7d5e12] bg-[#3a2c05] text-[#e3b341] hover:bg-[#54400a]',
  good: 'border-[#1f6f34] bg-[#0f2f1a] text-[#56d364] hover:bg-[#17472a]',
  easy: 'border-[#1f4e8c] bg-[#0d2d5e] text-[#79c0ff] hover:bg-[#164173]',
}

export default function ReviewPage() {
  const qc = useQueryClient()
  const [idx, setIdx] = useState(0)
  const [revealed, setRevealed] = useState(false)
  const [done, setDone] = useState(0)
  const startedAt = useRef(Date.now())

  const queue = useQuery({
    queryKey: ['review', 'queue'],
    queryFn: () => api.reviewQueue(30),
    // Scheduling changes on every grade; never serve this from cache.
    staleTime: 0,
    refetchOnMount: 'always',
  })

  const current = queue.data?.items[idx]

  // Full detail (code, notes) only for the card on screen, plus a prefetch
  // of the next one so revealing never waits on the network.
  const detail = useQuery({
    queryKey: ['problem', current?.problem.slug],
    queryFn: () => api.getProblem(current!.problem.slug),
    enabled: Boolean(current),
  })

  const next = queue.data?.items[idx + 1]
  useEffect(() => {
    if (!next) return
    qc.prefetchQuery({
      queryKey: ['problem', next.problem.slug],
      queryFn: () => api.getProblem(next.problem.slug),
    })
  }, [next, qc])

  useEffect(() => {
    setRevealed(false)
    startedAt.current = Date.now()
  }, [idx])

  const intervals = useMemo(() => {
    if (!current) return null
    const c = current.card as unknown as SrsCard
    try {
      return preview(c, new Date())
    } catch {
      return null
    }
  }, [current])

  const gradeMut = useMutation({
    mutationFn: (rating: RatingName) =>
      api.gradeProblem(current!.problem.id, rating, Date.now() - startedAt.current),
    onSuccess: () => {
      setDone((d) => d + 1)
      qc.invalidateQueries({ queryKey: ['review', 'stats'] })
      qc.invalidateQueries({ queryKey: ['review', 'forecast'] })
      if (idx + 1 < (queue.data?.items.length ?? 0)) setIdx((i) => i + 1)
      else queue.refetch().then(() => setIdx(0))
    },
  })

  const submit = useCallback(
    (r: RatingName) => {
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
      if (e.key === ' ' || e.key === 'Enter') { e.preventDefault(); setRevealed(true); return }
      const n = Number(e.key)
      if (n >= 1 && n <= 4 && revealed) { e.preventDefault(); submit(RATINGS[n - 1]) }
    }
    window.addEventListener('keydown', onKey)
    return () => window.removeEventListener('keydown', onKey)
  }, [revealed, submit])

  if (queue.isLoading) {
    return <div className="py-20 text-center text-sm text-[#6e7681]">Loading queue…</div>
  }

  if (!current) {
    return (
      <div className="mx-auto max-w-md py-20 text-center">
        <div className="mb-2 text-3xl">✓</div>
        <h1 className="mb-1 text-lg font-semibold">Nothing due</h1>
        <p className="mb-5 text-[13px] text-[#8b949e]">
          {done > 0
            ? `${done} review${done === 1 ? '' : 's'} done this session.`
            : 'Everything is scheduled for later.'}
        </p>
        <Link to="/problems"><Button>Browse problems</Button></Link>
      </div>
    )
  }

  const p = current.problem
  const note = detail.data?.note
  const sol = detail.data?.solutions?.[0]
  const overdue = current.card.overdueDays

  return (
    <div className="mx-auto max-w-3xl">
      <div className="mb-4 flex items-center gap-3 text-[12px] text-[#8b949e]">
        <span><b className="text-[#e6edf3]">{queue.data!.dueTotal}</b> due</span>
        <span>·</span>
        <span><b className="text-[#e6edf3]">{done}</b> done</span>
        <div className="ml-auto flex items-center gap-2">
          <Chip>{current.card.state}</Chip>
          {overdue > 0 && <Chip tone="amber">{overdue}d overdue</Chip>}
        </div>
      </div>

      <div className="rounded-lg border border-[#262d36] bg-[#0d1117]">
        <div className="border-b border-[#262d36] px-5 py-4">
          <div className="flex flex-wrap items-center gap-2">
            <h1 className="text-lg font-semibold">{p.title}</h1>
            {p.difficulty && <Chip tone={difficultyTone(p.difficulty)}>{p.difficulty}</Chip>}
            {p.source && <Chip>{p.source}</Chip>}
          </div>
          <div className="mt-1.5 flex flex-wrap items-center gap-1">
            {p.topics.map((t) => <Chip key={t.id}>{t.name}</Chip>)}
          </div>
          <div className="mt-2 flex items-center gap-3 text-[12px]">
            {p.judgeUrl && (
              <a href={p.judgeUrl} target="_blank" rel="noreferrer" className="text-[#58a6ff] hover:underline">
                Read the problem ↗
              </a>
            )}
            <Link to={`/problems/${p.slug}`} className="text-[#8b949e] hover:text-[#58a6ff]">
              Open full page
            </Link>
            <span className="font-mono text-[11px] text-[#6e7681]">{p.folderPath}</span>
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
              Show answer <span className="ml-1 opacity-60">(space)</span>
            </Button>
          </div>
        ) : (
          <div className="space-y-4 px-5 py-4">
            {p.patterns.length > 0 && (
              <div>
                <div className="mb-1 text-[11px] font-semibold uppercase tracking-wide text-[#8b949e]">
                  Patterns
                </div>
                <div className="flex flex-wrap gap-1">
                  {p.patterns.map((t) => <Chip key={t.id} tone="accent">{t.name}</Chip>)}
                </div>
              </div>
            )}
            {note?.framework && (
              <Section title="Framework">{note.framework}</Section>
            )}
            {note?.mistakes && (
              <Section title="Mistakes" tone="warn">{note.mistakes}</Section>
            )}
            {note?.notes && <Section title="Notes">{note.notes}</Section>}

            {sol ? (
              <div className="overflow-hidden rounded-md border border-[#262d36]">
                <div className="flex items-center justify-between border-b border-[#262d36] bg-[#161b22] px-3 py-1.5">
                  <span className="font-mono text-[11px] text-[#8b949e]">{sol.filePath}</span>
                  <span className="text-[11px] text-[#6e7681]">{sol.loc} lines</span>
                </div>
                <Suspense fallback={<div className="p-6 text-[12px] text-[#6e7681]">Loading…</div>}>
                  <CodeView code={sol.code} />
                </Suspense>
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
        <div className="mt-4 grid grid-cols-4 gap-2">
          {RATINGS.map((r, i) => (
            <button
              key={r}
              disabled={gradeMut.isPending}
              onClick={() => submit(r)}
              className={cx(
                'rounded-md border px-3 py-2.5 text-[13px] font-medium capitalize transition disabled:opacity-50',
                RATING_STYLE[r],
              )}
            >
              <div>{r}</div>
              <div className="mt-0.5 text-[11px] font-normal opacity-70">
                {intervals ? intervals[r].label : '—'} · {i + 1}
              </div>
            </button>
          ))}
        </div>
      )}

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
  children: React.ReactNode
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
      <div className="whitespace-pre-wrap rounded-md border border-[#262d36] bg-[#161b22] px-3 py-2 text-[13px] leading-relaxed text-[#c9d1d9]">
        {children}
      </div>
    </div>
  )
}
