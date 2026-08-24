import { useEffect, useMemo, useRef, useState } from 'react'
import type { Tag } from '@tracker/shared'
import { cx } from './ui'

/** Bold the matched span so it is obvious why a row is listed. */
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

/**
 * Multi-value picker backed by the existing vocabulary. Free text is still
 * allowed — a genuinely new pattern has to start somewhere — but the
 * suggestions make reusing an existing name the path of least resistance,
 * which is what keeps the facet list from filling up with near-duplicates.
 */
export default function TagInput({
  value, onChange, options, placeholder, tone = 'default',
}: {
  value: string[]
  onChange: (next: string[]) => void
  options: Tag[]
  placeholder?: string
  tone?: 'default' | 'accent'
}) {
  const [term, setTerm] = useState('')
  const [open, setOpen] = useState(false)
  const [active, setActive] = useState(0)
  const wrap = useRef<HTMLDivElement>(null)
  const input = useRef<HTMLInputElement>(null)

  const chosen = useMemo(
    () => new Set(value.map((v) => v.trim().toLowerCase())),
    [value],
  )

  const matches = useMemo(() => {
    const t = term.trim().toLowerCase()
    return options
      .filter((o) => !chosen.has(o.name.toLowerCase()))
      .filter((o) => (t ? o.name.toLowerCase().includes(t) : true))
      .slice(0, 8)
  }, [term, options, chosen])

  // Offer to create only when the term matches nothing already picked or listed.
  const canCreate =
    term.trim().length > 0 &&
    !chosen.has(term.trim().toLowerCase()) &&
    !options.some((o) => o.name.toLowerCase() === term.trim().toLowerCase())

  const rows = canCreate ? matches.length + 1 : matches.length

  useEffect(() => setActive(0), [term])

  useEffect(() => {
    const onDoc = (e: MouseEvent) => {
      if (wrap.current && !wrap.current.contains(e.target as Node)) setOpen(false)
    }
    document.addEventListener('mousedown', onDoc)
    return () => document.removeEventListener('mousedown', onDoc)
  }, [])

  const add = (name: string) => {
    const n = name.trim()
    if (!n || chosen.has(n.toLowerCase())) return
    onChange([...value, n])
    setTerm('')
    setActive(0)
  }

  const remove = (name: string) => onChange(value.filter((v) => v !== name))

  const onKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === 'Backspace' && !term && value.length) {
      remove(value[value.length - 1])
      return
    }
    if (e.key === 'ArrowDown' && rows) {
      e.preventDefault(); setOpen(true); setActive((a) => (a + 1) % rows); return
    }
    if (e.key === 'ArrowUp' && rows) {
      e.preventDefault(); setActive((a) => (a - 1 + rows) % rows); return
    }
    if (e.key === 'Enter' || e.key === ',') {
      e.preventDefault()
      if (open && active < matches.length) add(matches[active].name)
      else if (term.trim()) add(term)
      return
    }
    if (e.key === 'Escape') setOpen(false)
  }

  const chipCls =
    tone === 'accent'
      ? 'border-[#1f4e8c] bg-[#0d2d5e] text-[#79c0ff] hover:bg-[#1f4e8c]'
      : 'border-[#30363d] bg-[#21262d] text-[#c9d1d9] hover:bg-[#30363d]'

  return (
    <div ref={wrap} className="relative">
      <div
        onClick={() => { input.current?.focus(); setOpen(true) }}
        className="flex min-h-[38px] cursor-text flex-wrap items-center gap-1 rounded-md border border-[#30363d] bg-[#0d1117] px-2 py-1.5 focus-within:border-[#58a6ff]"
      >
        {value.map((v) => (
          <span
            key={v}
            className={cx('inline-flex items-center gap-1 rounded-full border py-0.5 pl-2 pr-1 text-[11px]', chipCls)}
          >
            {v}
            <button
              type="button"
              aria-label={`Remove ${v}`}
              onClick={(e) => { e.stopPropagation(); remove(v) }}
              className="rounded-full px-1 leading-none hover:bg-black/30"
            >
              ×
            </button>
          </span>
        ))}
        <input
          ref={input}
          value={term}
          placeholder={value.length ? '' : placeholder}
          onChange={(e) => { setTerm(e.target.value); setOpen(true) }}
          onFocus={() => setOpen(true)}
          onKeyDown={onKeyDown}
          className="min-w-[80px] flex-1 bg-transparent px-1 py-0.5 text-[13px] text-[#e6edf3] outline-none placeholder:text-[#6e7681]"
        />
      </div>

      {open && rows > 0 && (
        <div className="absolute z-30 mt-1 max-h-60 w-full overflow-y-auto rounded-lg border border-[#30363d] bg-[#161b22] shadow-xl">
          {matches.map((m, i) => (
            <button
              key={m.id}
              type="button"
              onMouseEnter={() => setActive(i)}
              onClick={() => add(m.name)}
              className={cx(
                'flex w-full items-center justify-between px-3 py-1.5 text-left text-[13px]',
                i === active ? 'bg-[#0d2d5e] text-[#79c0ff]' : 'text-[#c9d1d9]',
              )}
            >
              <span className="truncate"><Highlight text={m.name} term={term.trim()} /></span>
              {m.count !== undefined && (
                <span className="ml-3 shrink-0 text-[11px] text-[#6e7681]">{m.count}</span>
              )}
            </button>
          ))}
          {canCreate && (
            <button
              type="button"
              onMouseEnter={() => setActive(matches.length)}
              onClick={() => add(term)}
              className={cx(
                'flex w-full items-center gap-2 border-t border-[#21262d] px-3 py-1.5 text-left text-[13px]',
                active === matches.length ? 'bg-[#0d2d5e] text-[#79c0ff]' : 'text-[#8b949e]',
              )}
            >
              <span className="text-[#6e7681]">Create</span>
              <span className="text-[#e6edf3]">{term.trim()}</span>
            </button>
          )}
        </div>
      )}
    </div>
  )
}
