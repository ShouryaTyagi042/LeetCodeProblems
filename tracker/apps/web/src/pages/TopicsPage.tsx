import { Link } from 'react-router-dom'
import { useQuery } from '@tanstack/react-query'
import { LADDER_LABELS, type TopicDueRow } from '@tracker/shared'
import { api } from '../lib/api'
import { Chip, Empty, cx } from '../components/ui'

function daysUntil(iso: string): number {
  return Math.ceil((new Date(iso).getTime() - Date.now()) / 86400000)
}

export default function TopicsPage() {
  const rows = useQuery({
    queryKey: ['revision', 'topics'],
    queryFn: api.topicsDue,
    staleTime: 0,
  })

  if (rows.isLoading) return <Empty>Loading topics…</Empty>
  if (!rows.data?.length) return <Empty>No topics yet.</Empty>

  const totalDue = rows.data.reduce((a, r) => a + r.due, 0)
  const max = Math.max(...rows.data.map((r) => r.problems), 1)

  return (
    <div className="mx-auto max-w-4xl">
      <div className="mb-1 flex items-baseline gap-3">
        <h1 className="text-lg font-semibold">Topics</h1>
        <span className="text-[12px] text-[#8b949e]">
          <b className="text-[#e6edf3]">{totalDue}</b> problems due across{' '}
          {rows.data.length} topics
        </span>
      </div>
      <p className="mb-5 text-[13px] text-[#8b949e]">
        A topic is a filter on the review queue, not a thing that is scheduled
        by itself — everything is tracked per problem, on one ladder:{' '}
        {LADDER_LABELS.join(' → ')}, then monthly. Pick a topic to review only
        its problems.
      </p>

      <div className="overflow-hidden rounded-lg border border-[#262d36]">
        {rows.data.map((r, i) => (
          <Row key={r.topic.id} row={r} max={max} first={i === 0} />
        ))}
      </div>
    </div>
  )
}

function Row({ row: r, max, first }: { row: TopicDueRow; max: number; first: boolean }) {
  const pct = Math.round((r.due / Math.max(r.problems, 1)) * 100)
  return (
    <div
      className={cx(
        'flex flex-wrap items-center gap-3 px-4 py-2.5',
        !first && 'border-t border-[#1c2129]',
      )}
    >
      <Link
        to={`/problems?topic=${r.topic.slug}`}
        className="w-44 shrink-0 truncate text-[13px] font-medium hover:text-[#58a6ff]"
      >
        {r.topic.name}
      </Link>

      {/* Bar shows how much of the topic is due, relative to its size. */}
      <div className="hidden h-1.5 w-40 shrink-0 overflow-hidden rounded-full bg-[#21262d] sm:block">
        <div
          className="h-full rounded-full bg-[#1f6feb]"
          style={{ width: `${(r.problems / max) * 100}%`, opacity: 0.25 }}
        />
        <div
          className="-mt-1.5 h-full rounded-full bg-[#58a6ff]"
          style={{ width: `${(r.due / max) * 100}%` }}
        />
      </div>

      <span className="w-24 shrink-0 text-[11px] text-[#6e7681]">
        {r.problems} problem{r.problems === 1 ? '' : 's'}
      </span>

      <span className="w-20 shrink-0">
        {r.due > 0 ? (
          <Chip tone="accent">{r.due} due</Chip>
        ) : (
          <span className="text-[11px] text-[#6e7681]">clear</span>
        )}
      </span>

      <span className="w-24 shrink-0 text-[11px] text-[#6e7681]">
        {r.due > 0 ? `${pct}% of topic` : r.nextDue ? `next in ${daysUntil(r.nextDue)}d` : ''}
      </span>

      <div className="ml-auto">
        {r.due > 0 && (
          <Link
            to={`/review?topic=${r.topic.slug}`}
            className="rounded-md border border-[#1f6feb] bg-[#1f6feb] px-3 py-1 text-[12px] font-medium text-white hover:bg-[#388bfd]"
          >
            Review {r.due}
          </Link>
        )}
      </div>
    </div>
  )
}
