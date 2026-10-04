import type { ReactNode } from 'react'
import { Link } from 'react-router-dom'
import { useQuery } from '@tanstack/react-query'
import type { ActivityDay, ProblemOfTheDay } from '@tracker/shared'
import { api } from '../lib/api'
import { Chip, buttonCls, cx, difficultyTone } from '../components/ui'
import { ExternalLink, Flame, Plus } from '../components/icons'

export default function DashboardPage() {
  const dash = useQuery({ queryKey: ['dashboard'], queryFn: api.dashboard, staleTime: 0 })
  const d = dash.data

  return (
    <div className="mx-auto max-w-5xl space-y-4">
      <h1 className="text-lg font-semibold">Dashboard</h1>

      <div className="grid grid-cols-2 gap-3 md:grid-cols-4">
        <StreakTile current={d?.currentStreak} activeToday={d?.activeToday} addedToday={d?.addedToday} />
        <Stat label="Longest streak" value={d?.longestStreak} unit="days" />
        <Stat label="Added this week" value={d?.addedThisWeek} />
        <Stat label="Added this year" value={d?.addedThisYear} />
      </div>

      <Panel title="Problems added" aside={d && `${d.activity.reduce((a, x) => a + x.added, 0)} in the last year`}>
        {d ? <Heatmap days={d.activity} /> : <div className="h-[122px]" />}
      </Panel>

      <div className="grid gap-4 md:grid-cols-2">
        <Revision />
        <ProblemOfTheDayPanel />
      </div>
    </div>
  )
}

function Panel({ title, aside, children }: { title: string; aside?: ReactNode; children: ReactNode }) {
  return (
    <section className="rounded-lg border border-[#262d36] bg-[#0d1117] p-4">
      <div className="mb-3 flex items-baseline gap-3">
        <h2 className="text-[13px] font-semibold">{title}</h2>
        {aside && <span className="text-[11px] text-[#8b949e]">{aside}</span>}
      </div>
      {children}
    </section>
  )
}

function Stat({ label, value, unit }: { label: string; value?: number; unit?: string }) {
  return (
    <div className="rounded-lg border border-[#262d36] bg-[#0d1117] px-4 py-3">
      <div className="text-[11px] font-medium uppercase tracking-wide text-[#8b949e]">{label}</div>
      <div className="mt-1 text-2xl font-semibold tabular-nums">
        {value ?? '–'}
        {unit && value !== undefined && <span className="ml-1 text-[12px] font-normal text-[#8b949e]">{unit}</span>}
      </div>
    </div>
  )
}

function StreakTile({ current, activeToday, addedToday }: {
  current?: number
  activeToday?: boolean
  addedToday?: number
}) {
  const lit = (current ?? 0) > 0
  return (
    <div className="rounded-lg border border-[#262d36] bg-[#0d1117] px-4 py-3">
      <div className="text-[11px] font-medium uppercase tracking-wide text-[#8b949e]">Current streak</div>
      <div className="mt-1 flex items-center gap-2 text-2xl font-semibold tabular-nums">
        <Flame size={20} className={lit ? 'text-[#f0883e]' : 'text-[#484f58]'} />
        {current ?? '–'}
        {current !== undefined && <span className="text-[12px] font-normal text-[#8b949e]">days</span>}
      </div>
      {/* Today counts toward the streak once something is added or reviewed,
          but an empty today does not break it until the day is over. */}
      {activeToday === false && lit && (
        <div className="mt-0.5 text-[11px] text-[#e3b341]">Add or review one today to keep it going</div>
      )}
      {activeToday && (
        <div className="mt-0.5 text-[11px] text-[#56d364]">
          {addedToday ? `${addedToday} added today` : 'Reviewed today'}
        </div>
      )}
    </div>
  )
}

// ---------- calendar ----------

const LEVELS = ['#161b22', '#0e4429', '#006d32', '#26a641', '#39d353']
const level = (n: number) => (n <= 0 ? 0 : Math.min(n, 4))
const MONTHS = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
const CELL = 11
const GAP = 3

/** GitHub-style grid: one column per week, Sunday at the top. */
function Heatmap({ days }: { days: ActivityDay[] }) {
  const parse = (s: string) => new Date(`${s}T00:00:00`)
  // Pad the first week so each column starts on a Sunday.
  const lead = parse(days[0].date).getDay()
  const cells: (ActivityDay | null)[] = [...Array(lead).fill(null), ...days]
  const weeks: (ActivityDay | null)[][] = []
  for (let i = 0; i < cells.length; i += 7) weeks.push(cells.slice(i, i + 7))

  // A month label sits over the first column whose first real day is in a
  // new month, and is skipped if it would crowd the previous one.
  const labels: { col: number; text: string }[] = []
  let lastMonth = -1
  weeks.forEach((w, col) => {
    const first = w.find(Boolean)
    if (!first) return
    const m = parse(first.date).getMonth()
    if (m !== lastMonth) {
      if (!labels.length || col - labels[labels.length - 1].col >= 3) {
        labels.push({ col, text: MONTHS[m] })
      }
      lastMonth = m
    }
  })

  const step = CELL + GAP
  return (
    <div className="overflow-x-auto pb-1">
      <div className="flex gap-2" style={{ width: 'max-content' }}>
        <div className="flex flex-col pt-[18px] text-[10px] text-[#8b949e]" style={{ gap: GAP }}>
          {['', 'Mon', '', 'Wed', '', 'Fri', ''].map((t, i) => (
            <div key={i} style={{ height: CELL, lineHeight: `${CELL}px` }}>{t}</div>
          ))}
        </div>
        <div>
          <div className="relative h-[18px] text-[10px] text-[#8b949e]">
            {labels.map((l) => (
              <span key={l.col} className="absolute" style={{ left: l.col * step }}>{l.text}</span>
            ))}
          </div>
          <div className="flex" style={{ gap: GAP }}>
            {weeks.map((w, i) => (
              <div key={i} className="flex flex-col" style={{ gap: GAP }}>
                {w.map((day, j) =>
                  day ? (
                    <div
                      key={j}
                      title={`${day.added || 'No'} problem${day.added === 1 ? '' : 's'} on ${parse(day.date).toDateString()}`}
                      className="rounded-[2px] outline outline-1 -outline-offset-1 outline-[#1b1f230f]"
                      style={{ width: CELL, height: CELL, background: LEVELS[level(day.added)] }}
                    />
                  ) : (
                    <div key={j} style={{ width: CELL, height: CELL }} />
                  ),
                )}
              </div>
            ))}
          </div>
          <div className="mt-2 flex items-center justify-end gap-1 text-[10px] text-[#8b949e]">
            Less
            {LEVELS.map((c) => (
              <div key={c} className="rounded-[2px]" style={{ width: CELL, height: CELL, background: c }} />
            ))}
            More
          </div>
        </div>
      </div>
    </div>
  )
}

// ---------- revision ----------

function Revision() {
  const stats = useQuery({ queryKey: ['review', 'stats'], queryFn: api.reviewStats, staleTime: 0 })
  // Its own key: the review page caches its 30-card queue under
  // ['review','queue',…] and must not pick up this 5-card preview.
  const upNext = useQuery({
    queryKey: ['dashboard', 'queue'],
    queryFn: () => api.reviewQueue(5),
    staleTime: 0,
  })
  const s = stats.data
  const items = upNext.data?.items ?? []

  return (
    <Panel
      title="Revision"
      aside={s && `${s.reviewedToday} reviewed today${s.retention30d !== null ? ` · ${Math.round(s.retention30d * 100)}% solid (30d)` : ''}`}
    >
      <div className="mb-3 flex items-center gap-4">
        <div>
          <div className="text-2xl font-semibold tabular-nums">{s?.due ?? '–'}</div>
          <div className="text-[11px] text-[#8b949e]">due now</div>
        </div>
        {!!s?.backlog && (
          <div>
            <div className="text-2xl font-semibold tabular-nums text-[#e3b341]">{s.backlog}</div>
            <div className="text-[11px] text-[#8b949e]">overdue &gt; 1 day</div>
          </div>
        )}
        <div className="ml-auto">
          {s?.due ? (
            <Link to="/review" className={buttonCls('primary')}>Start review</Link>
          ) : (
            <Link to="/topics" className={buttonCls()}>Topics</Link>
          )}
        </div>
      </div>

      {items.length > 0 ? (
        <ul className="divide-y divide-[#1c2129] border-t border-[#1c2129]">
          {items.map(({ problem: p, card }) => (
            <li key={p.id} className="flex items-center gap-2 py-2">
              <Link to={`/problems/${p.slug}`} className="min-w-0 flex-1 truncate text-[13px] hover:text-[#58a6ff]">
                {p.title}
              </Link>
              {p.difficulty && <Chip tone={difficultyTone(p.difficulty)}>{p.difficulty}</Chip>}
              <span className="w-16 shrink-0 text-right text-[11px] text-[#6e7681]">
                {card.overdueDays > 0 ? `${card.overdueDays}d late` : 'today'}
              </span>
            </li>
          ))}
        </ul>
      ) : (
        !upNext.isLoading && (
          <p className="text-[13px] text-[#8b949e]">Nothing due — everything is scheduled for later.</p>
        )
      )}
    </Panel>
  )
}

// ---------- problem of the day ----------

function ProblemOfTheDayPanel() {
  const potd = useQuery({
    queryKey: ['potd'],
    queryFn: api.problemOfTheDay,
    staleTime: 60 * 60 * 1000,
    retry: 1,
  })

  return (
    <Panel title="Problem of the day" aside="LeetCode daily">
      {potd.isLoading && <p className="text-[13px] text-[#8b949e]">Fetching today’s question…</p>}
      {potd.isError && (
        <p className="text-[13px] text-[#f85149]">{(potd.error as Error).message}</p>
      )}
      {potd.data && <Potd q={potd.data} />}
    </Panel>
  )
}

function Potd({ q }: { q: ProblemOfTheDay }) {
  const addHref = `/problems/new?${new URLSearchParams({
    title: q.title,
    source: 'LeetCode Problem Of The Day',
    judgeUrl: q.url,
    ...(q.difficulty ? { difficulty: q.difficulty } : {}),
  })}`

  return (
    <div>
      <div className="mb-2 flex items-start gap-2">
        <span className="pt-0.5 font-mono text-[12px] text-[#6e7681]">{q.frontendId}.</span>
        <a href={q.url} target="_blank" rel="noreferrer"
          className="flex-1 text-[15px] font-medium leading-snug hover:text-[#58a6ff]">
          {q.title}
        </a>
        {q.difficulty && <Chip tone={difficultyTone(q.difficulty)}>{q.difficulty}</Chip>}
      </div>

      {q.tags.length > 0 && (
        <div className="mb-4 flex flex-wrap gap-1">
          {q.tags.map((t) => <Chip key={t}>{t}</Chip>)}
        </div>
      )}

      <div className={cx('flex flex-wrap items-center gap-2', !q.tags.length && 'mt-4')}>
        <a href={q.url} target="_blank" rel="noreferrer" className={buttonCls()}>
          Open on LeetCode <ExternalLink size={12} />
        </a>
        {q.tracked ? (
          <>
            <Link to={`/problems/${q.tracked.slug}`} className={buttonCls()}>View in tracker</Link>
            <Chip tone="green">Solved</Chip>
          </>
        ) : (
          <Link to={addHref} className={buttonCls('primary')}>
            <Plus size={12} /> Add to tracker
          </Link>
        )}
      </div>
    </div>
  )
}
