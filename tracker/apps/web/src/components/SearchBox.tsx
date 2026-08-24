import { useEffect, useMemo, useRef, useState } from 'react'
import type { Facets } from '@tracker/shared'
import { cx, inputCls } from './ui'

export type PickKind = 'topic' | 'pattern' | 'source'

interface Suggestion {
  kind: PickKind
  label: string
  /** what goes in the query string: slug for topic/pattern, name for source */
  value: string
  count?: number
}

const GROUP_LABEL: Record<PickKind, string> = {
  topic: 'Topics',
  pattern: 'Patterns',
  source: 'Sources',
}

/** Bold the matched span so it is obvious why a row is in the list. */
function Highlight({ text, term }: { text: string; term: string }) {
  const i = text.toLowerCase().indexOf(term.toLowerCase())
  if (!term || i < 0) return <>{text}</>
  return (
    <>
      {text.slice(0, i)}
      <b className="text-[#e6edf3]">{text.slice(i, i + term.length)}</b>
      {text.slice(i + term.length)}
    </>
  )
}

export default function SearchBox({
  value, onChange, facets, onPick,
}: {
  value: string
  onChange: (v: string) => void
  facets?: Facets
  onPick: (kind: PickKind, value: string) => void
}) {
  const [open, setOpen] = useState(false)
  const [active, setActive] = useState(0)
  const wrap = useRef<HTMLDivElement>(null)

  const suggestions = useMemo<Suggestion[]>(() => {
    const term = value.trim().toLowerCase()
    if (!term || !facets) return []
    const hit = (name: string) => name.toLowerCase().includes(term)
    const take = <T,>(xs: T[]) => xs.slice(0, 5)
    return [
      ...take(facets.topics.filter((t) => hit(t.name))).map((t) => ({
        kind: 'topic' as const, label: t.name, value: t.slug, count: t.count,
      })),
      ...take(facets.patterns.filter((p) => hit(p.name))).map((p) => ({
        kind: 'pattern' as const, label: p.name, value: p.slug, count: p.count,
      })),
      ...take(facets.sources.filter((s) => hit(s.name))).map((s) => ({
        kind: 'source' as const, label: s.name, value: s.name, count: s.count,
      })),
    ]
  }, [value, facets])

  useEffect(() => setActive(0), [value])

  // close when clicking outside
  useEffect(() => {
    const onDoc = (e: MouseEvent) => {
      if (wrap.current && !wrap.current.contains(e.target as Node)) setOpen(false)
    }
    document.addEventListener('mousedown', onDoc)
    return () => document.removeEventListener('mousedown', onDoc)
  }, [])

  const choose = (s: Suggestion) => {
    onPick(s.kind, s.value)
    setOpen(false)
  }

  const onKeyDown = (e: React.KeyboardEvent) => {
    if (!open || !suggestions.length) return
    if (e.key === 'ArrowDown') {
      e.preventDefault()
      setActive((a) => (a + 1) % suggestions.length)
    } else if (e.key === 'ArrowUp') {
      e.preventDefault()
      setActive((a) => (a - 1 + suggestions.length) % suggestions.length)
    } else if (e.key === 'Enter') {
      // Enter picks the highlighted filter; the text search is already live.
      e.preventDefault()
      choose(suggestions[active])
    } else if (e.key === 'Escape') {
      setOpen(false)
    }
  }

  let idx = -1

  return (
    <div ref={wrap} className="relative flex-1">
      <input
        className={inputCls}
        placeholder="Search titles, approaches, notes, mistakes — or a topic, pattern, source…"
        value={value}
        onChange={(e) => { onChange(e.target.value); setOpen(true) }}
        onFocus={() => setOpen(true)}
        onKeyDown={onKeyDown}
      />

      {open && suggestions.length > 0 && (
        <div className="absolute z-30 mt-1 w-full overflow-hidden rounded-lg border border-[#30363d] bg-[#161b22] shadow-xl">
          {(['topic', 'pattern', 'source'] as PickKind[]).map((kind) => {
            const rows = suggestions.filter((s) => s.kind === kind)
            if (!rows.length) return null
            return (
              <div key={kind}>
                <div className="border-b border-[#21262d] bg-[#0d1117] px-3 py-1 text-[10px] font-semibold uppercase tracking-wide text-[#6e7681]">
                  {GROUP_LABEL[kind]}
                </div>
                {rows.map((s) => {
                  idx++
                  const on = idx === active
                  const myIdx = idx
                  return (
                    <button
                      key={`${s.kind}:${s.value}`}
                      onMouseEnter={() => setActive(myIdx)}
                      onClick={() => choose(s)}
                      className={cx(
                        'flex w-full items-center justify-between px-3 py-1.5 text-left text-[13px]',
                        on ? 'bg-[#0d2d5e] text-[#79c0ff]' : 'text-[#c9d1d9]',
                      )}
                    >
                      <span className="truncate">
                        <Highlight text={s.label} term={value.trim()} />
                      </span>
                      {s.count !== undefined && (
                        <span className="ml-3 shrink-0 text-[11px] text-[#6e7681]">{s.count}</span>
                      )}
                    </button>
                  )
                })}
              </div>
            )
          })}
          <div className="border-t border-[#21262d] px-3 py-1.5 text-[11px] text-[#6e7681]">
            ↑↓ to move · Enter to filter · results below already match the text
          </div>
        </div>
      )}
    </div>
  )
}
