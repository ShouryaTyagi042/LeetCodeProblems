import { useEffect, useRef, useState } from 'react'
import {
  DEFAULT_SORT, SORT_DIR_LABEL, SORT_FIELDS, SORT_LABEL,
  isDefaultSort, type SortField, type SortSpec,
} from '@tracker/shared'
import { cx } from './ui'

/**
 * Multi-key sort builder. Keys apply in the order listed, so the first row
 * is the primary sort and the rest are tie-breaks — which is why order can
 * be changed here and not just membership.
 */
export default function SortControl({
  value, onChange,
}: {
  value: SortSpec[]
  onChange: (next: SortSpec[]) => void
}) {
  const [open, setOpen] = useState(false)
  const wrap = useRef<HTMLDivElement>(null)

  useEffect(() => {
    const onDoc = (e: MouseEvent) => {
      if (wrap.current && !wrap.current.contains(e.target as Node)) setOpen(false)
    }
    document.addEventListener('mousedown', onDoc)
    return () => document.removeEventListener('mousedown', onDoc)
  }, [])

  const unused = SORT_FIELDS.filter((f) => !value.some((s) => s.field === f))

  const flip = (i: number) =>
    onChange(value.map((s, j) => (j === i ? { ...s, dir: s.dir === 'asc' ? 'desc' : 'asc' } : s)))
  const drop = (i: number) => onChange(value.filter((_, j) => j !== i))
  const move = (i: number, by: number) => {
    const j = i + by
    if (j < 0 || j >= value.length) return
    const next = [...value]
    ;[next[i], next[j]] = [next[j], next[i]]
    onChange(next)
  }
  const add = (f: SortField) => onChange([...value, { field: f, dir: f === 'title' ? 'asc' : 'desc' }])

  const summary =
    value.length === 0
      ? 'Sort'
      : value.length === 1
        ? `${SORT_LABEL[value[0].field]} ${value[0].dir === 'asc' ? '↑' : '↓'}`
        : `${SORT_LABEL[value[0].field]} ${value[0].dir === 'asc' ? '↑' : '↓'} +${value.length - 1}`

  return (
    <div ref={wrap} className="relative shrink-0">
      <button
        onClick={() => setOpen((o) => !o)}
        className="flex h-[38px] w-44 items-center justify-between rounded-md border border-[#30363d] bg-[#0d1117] px-3 text-sm text-[#e6edf3] hover:border-[#484f58]"
      >
        <span className="truncate">{summary}</span>
        <span className="ml-2 text-[#6e7681]">▾</span>
      </button>

      {open && (
        <div className="absolute right-0 z-30 mt-1 w-72 rounded-lg border border-[#30363d] bg-[#161b22] p-2 shadow-xl">
          {value.length > 0 && (
            <div className="mb-2 space-y-1">
              {value.map((s, i) => (
                <div key={s.field} className="flex items-center gap-1 rounded-md bg-[#0d1117] px-2 py-1.5">
                  <span className="w-4 shrink-0 text-center text-[10px] text-[#6e7681]">{i + 1}</span>
                  <span className="w-20 shrink-0 truncate text-[13px] text-[#e6edf3]">
                    {SORT_LABEL[s.field]}
                  </span>
                  <button
                    onClick={() => flip(i)}
                    title="Toggle direction"
                    className="flex-1 truncate rounded px-1.5 py-0.5 text-left text-[11px] text-[#79c0ff] hover:bg-[#21262d]"
                  >
                    {s.dir === 'asc' ? '↑' : '↓'} {SORT_DIR_LABEL[s.field][s.dir]}
                  </button>
                  <button
                    onClick={() => move(i, -1)} disabled={i === 0}
                    title="Move up"
                    className="rounded px-1 text-[11px] text-[#8b949e] hover:bg-[#21262d] disabled:opacity-25"
                  >↑</button>
                  <button
                    onClick={() => move(i, 1)} disabled={i === value.length - 1}
                    title="Move down"
                    className="rounded px-1 text-[11px] text-[#8b949e] hover:bg-[#21262d] disabled:opacity-25"
                  >↓</button>
                  <button
                    onClick={() => drop(i)}
                    title="Remove"
                    className="rounded px-1 text-[#8b949e] hover:bg-[#21262d] hover:text-[#f85149]"
                  >×</button>
                </div>
              ))}
            </div>
          )}

          {unused.length > 0 && (
            <>
              <div className="px-1 py-1 text-[10px] font-semibold uppercase tracking-wide text-[#6e7681]">
                {value.length ? 'Then by' : 'Sort by'}
              </div>
              <div className="flex flex-wrap gap-1">
                {unused.map((f) => (
                  <button
                    key={f}
                    onClick={() => add(f)}
                    className="rounded-full border border-[#30363d] px-2 py-0.5 text-[11px] text-[#c9d1d9] hover:border-[#58a6ff] hover:text-[#79c0ff]"
                  >
                    + {SORT_LABEL[f]}
                  </button>
                ))}
              </div>
            </>
          )}

          {!isDefaultSort(value) && (
            <button
              onClick={() => onChange([...DEFAULT_SORT])}
              className={cx(
                'mt-2 w-full rounded px-2 py-1 text-[11px] text-[#8b949e] hover:bg-[#21262d]',
              )}
            >
              Reset to default ({DEFAULT_SORT.map(
                (s) => `${SORT_LABEL[s.field]} ${s.dir === 'asc' ? '↑' : '↓'}`,
              ).join(', ')})
            </button>
          )}
        </div>
      )}
    </div>
  )
}
