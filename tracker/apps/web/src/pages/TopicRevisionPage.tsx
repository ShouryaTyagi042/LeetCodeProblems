import { Link } from 'react-router-dom'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { TOPIC_LADDER_LABELS, type TopicRevisionRow } from '@tracker/shared'
import { api } from '../lib/api'
import { Button, Chip, Empty, cx } from '../components/ui'

function dueLabel(iso: string, overdue: number): { text: string; tone: 'red' | 'amber' | 'default' } {
  if (overdue > 7) return { text: `${overdue}d overdue`, tone: 'red' }
  if (overdue >= 0) return { text: overdue === 0 ? 'due today' : `${overdue}d overdue`, tone: 'amber' }
  const days = Math.abs(overdue)
  return { text: `in ${days}d`, tone: 'default' }
}

export default function TopicRevisionPage() {
  const qc = useQueryClient()
  const rows = useQuery({
    queryKey: ['revision', 'topics'],
    queryFn: api.topicRevisions,
    staleTime: 0,
  })

  const revise = useMutation({
    mutationFn: ({ slug, outcome }: { slug: string; outcome: 'good' | 'again' }) =>
      api.reviseTopic(slug, outcome),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['revision', 'topics'] }),
  })

  if (rows.isLoading) return <Empty>Loading topics…</Empty>
  if (!rows.data?.length) return <Empty>No topics yet.</Empty>

  const due = rows.data.filter((r) => r.overdueDays >= 0)
  const later = rows.data.filter((r) => r.overdueDays < 0)

  return (
    <div className="mx-auto max-w-4xl">
      <div className="mb-1 flex items-baseline gap-3">
        <h1 className="text-lg font-semibold">Topic revision</h1>
        <span className="text-[12px] text-[#8b949e]">
          <b className="text-[#e6edf3]">{due.length}</b> of {rows.data.length} due
        </span>
      </div>
      <p className="mb-5 text-[13px] text-[#8b949e]">
        Sweep a whole topic, then mark it. The ladder is fixed:{' '}
        {TOPIC_LADDER_LABELS.join(' → ')}, then monthly. This is separate from
        per-problem review — use it to keep a technique warm rather than to
        recall one solution.
      </p>

      {due.length > 0 && <Section rows={due} onRevise={revise.mutate} busy={revise.isPending} />}

      {later.length > 0 && (
        <>
          <div className="mb-2 mt-6 text-[11px] font-semibold uppercase tracking-wide text-[#6e7681]">
            Scheduled
          </div>
          <Section rows={later} onRevise={revise.mutate} busy={revise.isPending} />
        </>
      )}
    </div>
  )
}

function Section({
  rows, onRevise, busy,
}: {
  rows: TopicRevisionRow[]
  onRevise: (v: { slug: string; outcome: 'good' | 'again' }) => void
  busy: boolean
}) {
  return (
    <div className="overflow-hidden rounded-lg border border-[#262d36]">
      {rows.map((r, i) => {
        const d = dueLabel(r.due, r.overdueDays)
        return (
          <div
            key={r.topic.id}
            className={cx(
              'flex flex-wrap items-center gap-3 px-4 py-2.5',
              i > 0 && 'border-t border-[#1c2129]',
            )}
          >
            <Link
              to={`/problems?topic=${r.topic.slug}`}
              className="w-48 shrink-0 truncate text-[13px] font-medium hover:text-[#58a6ff]"
            >
              {r.topic.name}
            </Link>

            <span className="w-24 shrink-0 text-[11px] text-[#6e7681]">
              {r.problems} problem{r.problems === 1 ? '' : 's'}
            </span>

            <span className="w-24 shrink-0">
              {r.problemsDue > 0 && (
                <Chip tone="accent">{r.problemsDue} due</Chip>
              )}
            </span>

            <span className="w-28 shrink-0">
              <Chip tone={d.tone}>{d.text}</Chip>
            </span>

            <span
              className="w-28 shrink-0 text-[11px] text-[#6e7681]"
              title={`${r.reps} sweep${r.reps === 1 ? '' : 's'} so far`}
            >
              next: {r.nextIntervalLabel}
            </span>

            <div className="ml-auto flex gap-1.5">
              <button
                disabled={busy}
                onClick={() => onRevise({ slug: r.topic.slug, outcome: 'again' })}
                title="Shaky — come back in 3 days"
                className="rounded-md border border-[#7d2429] bg-[#3d1418] px-2.5 py-1 text-[12px] text-[#f85149] hover:bg-[#5a1d22] disabled:opacity-50"
              >
                Shaky
              </button>
              <button
                disabled={busy}
                onClick={() => onRevise({ slug: r.topic.slug, outcome: 'good' })}
                title={`Solid — come back in ${r.nextIntervalLabel}`}
                className="rounded-md border border-[#1f6f34] bg-[#0f2f1a] px-2.5 py-1 text-[12px] text-[#56d364] hover:bg-[#17472a] disabled:opacity-50"
              >
                Solid
              </button>
            </div>
          </div>
        )
      })}
    </div>
  )
}
