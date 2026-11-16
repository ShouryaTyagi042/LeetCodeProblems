import { useState, type ReactNode } from 'react'
import { Link } from 'react-router-dom'
import { useQuery } from '@tanstack/react-query'
import {
  INTERVIEW_SET,
  type InterviewGapItem, type InterviewItem, type Sheet,
} from '@tracker/shared'
import { api } from '../lib/api'
import { Chip, buttonCls, cx, difficultyTone } from '../components/ui'
import { ExternalLink, Plus, Refresh } from '../components/icons'

const SHEETS: Sheet[] = ['Striver', 'NeetCode 150', 'Blind 75']

const SHEET_BADGE: Record<Sheet, { short: string; cls: string }> = {
  'Striver': { short: 'S', cls: 'border-[#7d5e12] text-[#e3b341]' },
  'NeetCode 150': { short: 'N', cls: 'border-[#1f4e8c] text-[#79c0ff]' },
  'Blind 75': { short: 'B', cls: 'border-[#6e40c9] text-[#d2a8ff]' },
}

/** Split a list already in group order wherever the group changes. */
function byGroup<T>(xs: T[], groupOf: (x: T) => string): { name: string; items: { x: T; n: number }[] }[] {
  const out: { name: string; items: { x: T; n: number }[] }[] = []
  xs.forEach((x, i) => {
    const name = groupOf(x)
    const last = out[out.length - 1]
    if (last?.name === name) last.items.push({ x, n: i + 1 })
    else out.push({ name, items: [{ x, n: i + 1 }] })
  })
  return out
}

export default function InterviewPage() {
  return (
    <div className="mx-auto max-w-5xl space-y-4">
      <div className="flex flex-wrap items-baseline gap-3">
        <h1 className="text-lg font-semibold">Interview 50</h1>
        <span className="flex items-center gap-3 text-[11px] text-[#8b949e]">
          {SHEETS.map((s) => (
            <span key={s} className="flex items-center gap-1"><SheetBadge sheet={s} />{s}</span>
          ))}
        </span>
      </div>
      <Top50 />
      <Gaps />
    </div>
  )
}

function Section({ title, aside, action, children }: {
  title: string
  aside?: ReactNode
  action?: ReactNode
  children: ReactNode
}) {
  return (
    <section className="rounded-lg border border-[#262d36] bg-[#0d1117] p-4">
      <div className="mb-3 flex flex-wrap items-center gap-3">
        <h2 className="text-[13px] font-semibold">{title}</h2>
        {aside && <span className="text-[11px] text-[#8b949e]">{aside}</span>}
        {action && <div className="ml-auto flex items-center gap-2">{action}</div>}
      </div>
      {children}
    </section>
  )
}

function SheetBadge({ sheet }: { sheet: Sheet }) {
  const b = SHEET_BADGE[sheet]
  return (
    <span title={sheet}
      className={cx('inline-flex h-4 w-4 shrink-0 items-center justify-center rounded border text-[9px] font-semibold', b.cls)}>
      {b.short}
    </span>
  )
}

function Sheets({ sheets }: { sheets: Sheet[] }) {
  // Fixed slots, so the badges line up in a column down the list.
  return (
    <span className="flex shrink-0 gap-0.5">
      {SHEETS.map((s) =>
        sheets.includes(s) ? <SheetBadge key={s} sheet={s} /> : <span key={s} className="w-4" />,
      )}
    </span>
  )
}

// ---------- the 50 ----------

function Top50() {
  const list = useQuery({ queryKey: ['interview'], queryFn: api.interview, staleTime: 0 })
  const items = list.data ?? []
  const due = items.filter((x) => x.card && x.card.overdueDays >= 0).length
  const onSheet = items.filter((x) => x.pick.sheets.length > 0).length

  return (
    <Section
      title="Top 50 to revise"
      aside={items.length > 0 && `${due} due · ${onSheet} also on a public sheet`}
      action={
        <Link to={`/review?set=${INTERVIEW_SET}`} className={buttonCls('primary')}>
          Revise all {items.length || ''}
        </Link>
      }
    >
      <p className="-mt-1 mb-3 text-[12px] text-[#8b949e]">
        Picked from what is solved here. Revising one moves it on its normal schedule.
      </p>
      {list.isLoading && <div className="h-40" />}
      {list.isError && <p className="text-[13px] text-[#f85149]">{(list.error as Error).message}</p>}
      <div className="grid gap-x-6 gap-y-4 md:grid-cols-2">
        {byGroup(items, (x) => x.pick.group).map((g) => (
          <div key={g.name}>
            <GroupHeading>{g.name}</GroupHeading>
            <ul className="divide-y divide-[#1c2129] border-t border-[#1c2129]">
              {g.items.map(({ x, n }) => <PickRow key={x.problem.id} item={x} n={n} />)}
            </ul>
          </div>
        ))}
      </div>
    </Section>
  )
}

function GroupHeading({ children }: { children: ReactNode }) {
  return <h3 className="mb-1 text-[11px] font-medium uppercase tracking-wide text-[#8b949e]">{children}</h3>
}

function PickRow({ item, n }: { item: InterviewItem; n: number }) {
  const { pick, problem: p, card } = item
  const isDue = card && card.overdueDays >= 0
  return (
    <li className="flex items-center gap-2 py-1.5">
      <span className="w-5 shrink-0 text-right font-mono text-[11px] text-[#6e7681]">{n}</span>
      <div className="min-w-0 flex-1">
        <Link to={`/problems/${p.slug}`} className="block truncate text-[13px] hover:text-[#58a6ff]"
          title={pick.aka ? `${p.title} — ${pick.aka}` : p.title}>
          {p.title}
          {pick.aka && <span className="ml-1.5 text-[11px] text-[#6e7681]">{pick.aka}</span>}
        </Link>
        <div className="truncate text-[11px] text-[#6e7681]">{pick.why}</div>
      </div>
      <Sheets sheets={pick.sheets} />
      {p.difficulty && <Chip tone={difficultyTone(p.difficulty)}>{p.difficulty}</Chip>}
      {isDue && <Chip tone="amber">due</Chip>}
      <Link to={`/review?problem=${p.slug}`} title="Revise this problem"
        className="shrink-0 rounded p-1 text-[#8b949e] hover:bg-[#21262d] hover:text-[#e6edf3]">
        <Refresh size={13} />
      </Link>
    </li>
  )
}

// ---------- gaps ----------

type GapFilter = 'open' | 'all'

const gapState = (g: InterviewGapItem) =>
  g.trackedSlug ? 'covered' : g.gap.elsewhere ? 'elsewhere' : 'open'

function Gaps() {
  const gaps = useQuery({ queryKey: ['interview', 'gaps'], queryFn: api.interviewGaps, staleTime: 0 })
  const [filter, setFilter] = useState<GapFilter>('open')
  const items = gaps.data ?? []
  const count = (s: string) => items.filter((g) => gapState(g) === s).length
  const shown = filter === 'all' ? items : items.filter((g) => gapState(g) !== 'covered')

  return (
    <Section
      title="Gaps in the tracker"
      aside={items.length > 0 && (
        <>
          {count('open')} not solved · {count('elsewhere')} solved outside the tracker · {count('covered')} covered
        </>
      )}
      action={
        <div className="flex rounded-md border border-[#30363d] text-[12px]">
          {(['open', 'all'] as GapFilter[]).map((f) => (
            <button key={f} onClick={() => setFilter(f)}
              className={cx('px-2.5 py-1', filter === f ? 'bg-[#21262d] text-[#e6edf3]' : 'text-[#8b949e] hover:text-[#e6edf3]')}>
              {f === 'open' ? 'Remaining' : 'All'}
            </button>
          ))}
        </div>
      }
    >
      <p className="-mt-1 mb-3 text-[12px] text-[#8b949e]">
        Problems that Striver, NeetCode 150 and Blind 75 rely on and nothing here covers yet. One is
        ticked off once it is added to the tracker with its LeetCode link. “In StriverSDE” means a
        solution exists in the repo, outside the folders the tracker reads.
      </p>
      {gaps.isLoading && <div className="h-40" />}
      {gaps.isError && <p className="text-[13px] text-[#f85149]">{(gaps.error as Error).message}</p>}
      {items.length > 0 && shown.length === 0 && (
        <p className="text-[13px] text-[#56d364]">Every gap is covered.</p>
      )}
      <div className="grid gap-x-6 gap-y-4 md:grid-cols-2">
        {byGroup(shown, (g) => g.gap.group).map((g) => (
          <div key={g.name}>
            <GroupHeading>{g.name}</GroupHeading>
            <ul className="divide-y divide-[#1c2129] border-t border-[#1c2129]">
              {g.items.map(({ x }) => <GapRow key={x.gap.leetcode} item={x} />)}
            </ul>
          </div>
        ))}
      </div>
    </Section>
  )
}

function GapRow({ item }: { item: InterviewGapItem }) {
  const { gap, trackedSlug } = item
  const url = `https://leetcode.com/problems/${gap.leetcode}/`
  const addHref = `/problems/new?${new URLSearchParams({ title: gap.title, source: 'LeetCode', judgeUrl: url })}`
  return (
    <li className="flex items-center gap-2 py-1.5">
      <a href={url} target="_blank" rel="noreferrer"
        className={cx('min-w-0 flex-1 truncate text-[13px] hover:text-[#58a6ff]', !!trackedSlug && 'text-[#6e7681] line-through')}>
        {gap.title}
      </a>
      <Sheets sheets={gap.sheets} />
      {trackedSlug ? (
        <Link to={`/problems/${trackedSlug}`}><Chip tone="green">covered</Chip></Link>
      ) : gap.elsewhere ? (
        <span title={gap.elsewhere}><Chip tone="amber">in StriverSDE</Chip></span>
      ) : (
        <Chip>not solved</Chip>
      )}
      <a href={url} target="_blank" rel="noreferrer" title="Open on LeetCode"
        className="shrink-0 rounded p-1 text-[#8b949e] hover:bg-[#21262d] hover:text-[#e6edf3]">
        <ExternalLink size={12} />
      </a>
      {!trackedSlug && (
        <Link to={addHref} title="Add to tracker"
          className="shrink-0 rounded p-1 text-[#8b949e] hover:bg-[#21262d] hover:text-[#e6edf3]">
          <Plus size={12} />
        </Link>
      )}
    </li>
  )
}
