import { useEffect, useMemo, useRef, useState } from 'react'
import { slugify } from '@tracker/shared'
import { cx, inputCls } from './ui'
import { Plus } from './icons'

/**
 * Single-value text field that suggests values already in use.
 *
 * A value that matches nothing has always been accepted — the server upserts
 * it — but there was no sign of that: type a topic that does not exist yet
 * and the list simply went empty, which reads as "no such thing" rather than
 * "this one is new". Pass createLabel to offer it explicitly instead.
 */
export default function ComboInput({
  value, onChange, options, placeholder, createLabel,
}: {
  value: string
  onChange: (v: string) => void
  options: { name: string; count?: number }[]
  placeholder?: string
  /** e.g. "topic" — enables a "Create new topic" row for an unmatched value. */
  createLabel?: string
}) {
  const [open, setOpen] = useState(false)
  const [active, setActive] = useState(0)
  const wrap = useRef<HTMLDivElement>(null)

  const matches = useMemo(() => {
    const t = value.trim().toLowerCase()
    return options
      .filter((o) => (t ? o.name.toLowerCase().includes(t) : true))
      .filter((o) => o.name.toLowerCase() !== t)
      .slice(0, 8)
  }, [value, options])

  // Matched against the same shape the server keys topics on, so the field
  // does not offer to create something that would just fold into an existing
  // row: "segment trees" and "Segment Trees" are one topic.
  const trimmed = value.trim()
  const exists = useMemo(
    () => options.some((o) => slugify(o.name) === slugify(trimmed)),
    [options, trimmed],
  )
  const canCreate = Boolean(createLabel) && trimmed.length > 0 && !exists
  // The create row sits after the suggestions, so Enter still completes to an
  // existing value first and creating is the deliberate choice.
  const rows: ({ kind: 'option'; name: string; count?: number } | { kind: 'create' })[] = [
    ...matches.map((m) => ({ kind: 'option' as const, name: m.name, count: m.count })),
    ...(canCreate ? [{ kind: 'create' as const }] : []),
  ]

  useEffect(() => setActive(0), [value])

  useEffect(() => {
    const onDoc = (e: MouseEvent) => {
      if (wrap.current && !wrap.current.contains(e.target as Node)) setOpen(false)
    }
    document.addEventListener('mousedown', onDoc)
    return () => document.removeEventListener('mousedown', onDoc)
  }, [])

  const pick = (v: string) => { onChange(v); setOpen(false) }

  return (
    <div ref={wrap} className="relative">
      <input
        className={inputCls}
        value={value}
        placeholder={placeholder}
        onChange={(e) => { onChange(e.target.value); setOpen(true) }}
        onFocus={() => setOpen(true)}
        onKeyDown={(e) => {
          if (!open || !rows.length) return
          if (e.key === 'ArrowDown') { e.preventDefault(); setActive((a) => (a + 1) % rows.length) }
          else if (e.key === 'ArrowUp') { e.preventDefault(); setActive((a) => (a - 1 + rows.length) % rows.length) }
          else if (e.key === 'Enter') {
            e.preventDefault()
            const row = rows[Math.min(active, rows.length - 1)]
            pick(row.kind === 'create' ? trimmed : row.name)
          }
          else if (e.key === 'Escape') setOpen(false)
        }}
      />
      {open && rows.length > 0 && (
        <div className="absolute z-30 mt-1 max-h-60 w-full overflow-y-auto rounded-lg border border-[#30363d] bg-[#161b22] shadow-xl">
          {rows.map((row, i) =>
            row.kind === 'create' ? (
              <button
                key="__create"
                type="button"
                onMouseEnter={() => setActive(i)}
                onClick={() => pick(trimmed)}
                className={cx(
                  'flex w-full items-center gap-1.5 px-3 py-1.5 text-left text-[13px]',
                  matches.length > 0 && 'border-t border-[#262d36]',
                  i === active ? 'bg-[#0d2d5e] text-[#79c0ff]' : 'text-[#8b949e]',
                )}
              >
                <Plus size={12} className="shrink-0" />
                <span className="truncate">
                  Create new {createLabel} “<span className="text-[#e6edf3]">{trimmed}</span>”
                </span>
              </button>
            ) : (
              <button
                key={row.name}
                type="button"
                onMouseEnter={() => setActive(i)}
                onClick={() => pick(row.name)}
                className={cx(
                  'flex w-full items-center justify-between px-3 py-1.5 text-left text-[13px]',
                  i === active ? 'bg-[#0d2d5e] text-[#79c0ff]' : 'text-[#c9d1d9]',
                )}
              >
                <span className="truncate">{row.name}</span>
                {row.count !== undefined && (
                  <span className="ml-3 shrink-0 text-[11px] text-[#6e7681]">{row.count}</span>
                )}
              </button>
            ),
          )}
        </div>
      )}
    </div>
  )
}
