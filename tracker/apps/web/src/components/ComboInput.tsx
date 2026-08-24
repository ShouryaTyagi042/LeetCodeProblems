import { useEffect, useMemo, useRef, useState } from 'react'
import { cx, inputCls } from './ui'

/** Single-value text field that suggests values already in use. */
export default function ComboInput({
  value, onChange, options, placeholder,
}: {
  value: string
  onChange: (v: string) => void
  options: { name: string; count?: number }[]
  placeholder?: string
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
          if (!open || !matches.length) return
          if (e.key === 'ArrowDown') { e.preventDefault(); setActive((a) => (a + 1) % matches.length) }
          else if (e.key === 'ArrowUp') { e.preventDefault(); setActive((a) => (a - 1 + matches.length) % matches.length) }
          else if (e.key === 'Enter') { e.preventDefault(); pick(matches[active].name) }
          else if (e.key === 'Escape') setOpen(false)
        }}
      />
      {open && matches.length > 0 && (
        <div className="absolute z-30 mt-1 max-h-60 w-full overflow-y-auto rounded-lg border border-[#30363d] bg-[#161b22] shadow-xl">
          {matches.map((m, i) => (
            <button
              key={m.name}
              type="button"
              onMouseEnter={() => setActive(i)}
              onClick={() => pick(m.name)}
              className={cx(
                'flex w-full items-center justify-between px-3 py-1.5 text-left text-[13px]',
                i === active ? 'bg-[#0d2d5e] text-[#79c0ff]' : 'text-[#c9d1d9]',
              )}
            >
              <span className="truncate">{m.name}</span>
              {m.count !== undefined && (
                <span className="ml-3 shrink-0 text-[11px] text-[#6e7681]">{m.count}</span>
              )}
            </button>
          ))}
        </div>
      )}
    </div>
  )
}
