import { useEffect, useState } from 'react'
import { Link, useSearchParams } from 'react-router-dom'
import { useQuery, keepPreviousData } from '@tanstack/react-query'
import type { ProblemQuery } from '@tracker/shared'
import { api } from '../lib/api'
import Filters from '../components/Filters'
import SearchBox, { type PickKind } from '../components/SearchBox'
import ActiveFilters from '../components/ActiveFilters'
import { Chip, Empty, difficultyTone, inputBase } from '../components/ui'

export default function ProblemsPage() {
  const [sp, setSp] = useSearchParams()
  const value = Object.fromEntries(sp.entries()) as Record<string, string>
  const [term, setTerm] = useState(value.q ?? '')

  // debounce the search box so typing doesn't fire a request per keystroke
  useEffect(() => {
    const t = setTimeout(() => {
      // Functional update: this fires 250ms late, so building from a
      // captured `sp` could wipe a filter picked in the meantime.
      setSp(
        (prev) => {
          const next = new URLSearchParams(prev)
          term ? next.set('q', term) : next.delete('q')
          next.delete('page')
          return next
        },
        { replace: true },
      )
    }, 250)
    return () => clearTimeout(t)
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [term])

  const facets = useQuery({ queryKey: ['facets'], queryFn: api.facets })
  const page = Number(value.page ?? 1)
  const query: ProblemQuery = { ...(value as ProblemQuery), page, perPage: 50 }
  const list = useQuery({
    queryKey: ['problems', query],
    queryFn: () => api.listProblems(query),
    placeholderData: keepPreviousData,
  })

  const patch = (p: Record<string, string | undefined>) => {
    setSp((prev) => {
      const next = new URLSearchParams(prev)
      for (const [k, v] of Object.entries(p)) v ? next.set(k, v) : next.delete(k)
      next.delete('page')
      return next
    })
  }

  const total = list.data?.total ?? 0
  const pages = Math.ceil(total / 50)

  return (
    <div className="flex gap-6">
      <Filters facets={facets.data} value={value} onChange={patch} />

      <div className="min-w-0 flex-1">
        <div className="mb-3 flex items-start gap-2">
          <SearchBox
            value={term}
            onChange={setTerm}
            facets={facets.data}
            onPick={(kind: PickKind, v: string) => {
              // Picking a facet replaces the free-text search with the filter —
              // keeping both would usually return nothing.
              setTerm('')
              patch({ [kind]: v, q: undefined })
            }}
          />
          <select
            className={`${inputBase} w-36 shrink-0`}
            value={value.sort ?? 'title'}
            onChange={(e) => patch({ sort: e.target.value })}
          >
            <option value="title">A–Z</option>
            <option value="recent">Recent</option>
            <option value="difficulty">Difficulty</option>
          </select>
        </div>

        <ActiveFilters
          value={value}
          facets={facets.data}
          onClear={(k) => patch({ [k]: undefined })}
          onClearAll={() =>
            patch({ topic: undefined, pattern: undefined, source: undefined,
                    difficulty: undefined, status: undefined })
          }
        />

        <div className="mb-2 text-[12px] text-[#8b949e]">
          {list.isLoading ? 'Loading…' : `${total} problem${total === 1 ? '' : 's'}`}
        </div>

        <div className="overflow-hidden rounded-lg border border-[#262d36]">
          {list.data?.items.length === 0 && <Empty>Nothing matches those filters.</Empty>}
          {list.data?.items.map((p, i) => (
            <Link
              key={p.id}
              to={`/problems/${p.slug}`}
              className={
                'flex items-center gap-3 px-4 py-2.5 hover:bg-[#161b22] ' +
                (i ? 'border-t border-[#1c2129]' : '')
              }
            >
              <span
                title={p.status}
                className={
                  'h-1.5 w-1.5 shrink-0 rounded-full ' +
                  (p.status === 'solved' ? 'bg-[#3fb950]' : 'bg-[#6e7681]')
                }
              />
              <span className="w-[300px] shrink-0 truncate text-[13px]">{p.title}</span>
              <div className="flex min-w-0 flex-1 gap-1 overflow-hidden">
                {p.topics.slice(0, 2).map((t) => (
                  <Chip key={t.id}>{t.name}</Chip>
                ))}
                {p.patterns.slice(0, 2).map((t) => (
                  <Chip key={t.id} tone="accent">{t.name}</Chip>
                ))}
              </div>
              {p.hasNote && <span title="has notes" className="text-[11px] text-[#8b949e]">✎</span>}
              {p.difficulty && (
                <Chip tone={difficultyTone(p.difficulty)}>{p.difficulty}</Chip>
              )}
              <span className="w-14 shrink-0 text-right text-[11px] text-[#6e7681]">
                {p.loc || ''} {p.loc ? 'LOC' : ''}
              </span>
            </Link>
          ))}
        </div>

        {pages > 1 && (
          <div className="mt-4 flex items-center justify-center gap-2 text-[13px]">
            <button
              disabled={page <= 1}
              onClick={() => patch({ page: String(page - 1) })}
              className="rounded px-3 py-1 hover:bg-[#161b22] disabled:opacity-40"
            >
              ← Prev
            </button>
            <span className="text-[#8b949e]">{page} / {pages}</span>
            <button
              disabled={page >= pages}
              onClick={() => patch({ page: String(page + 1) })}
              className="rounded px-3 py-1 hover:bg-[#161b22] disabled:opacity-40"
            >
              Next →
            </button>
          </div>
        )}
      </div>
    </div>
  )
}
