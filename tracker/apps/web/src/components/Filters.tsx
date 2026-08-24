import type { Facets } from '@tracker/shared'
import { cx } from './ui'

interface Props {
  facets?: Facets
  value: Record<string, string>
  onChange: (patch: Record<string, string | undefined>) => void
}

function Group({
  title, items, active, onPick,
}: {
  title: string
  items: { name: string; slug?: string; count?: number }[]
  active?: string
  onPick: (v?: string) => void
}) {
  if (!items.length) return null
  return (
    <div className="mb-5">
      <div className="mb-2 flex items-center justify-between">
        <span className="text-[11px] font-semibold uppercase tracking-wide text-[#8b949e]">
          {title}
        </span>
        {active && (
          <button onClick={() => onPick(undefined)} className="text-[11px] text-[#58a6ff]">
            clear
          </button>
        )}
      </div>
      <div className="max-h-64 space-y-0.5 overflow-y-auto pr-1">
        {items.map((it) => {
          const key = it.slug ?? it.name
          const on = active === key
          return (
            <button
              key={key}
              onClick={() => onPick(on ? undefined : key)}
              className={cx(
                'flex w-full items-center justify-between rounded px-2 py-1 text-left text-[13px]',
                on ? 'bg-[#0d2d5e] text-[#79c0ff]' : 'text-[#c9d1d9] hover:bg-[#161b22]',
              )}
            >
              <span className="truncate">{it.name}</span>
              {it.count !== undefined && (
                <span className="ml-2 shrink-0 text-[11px] text-[#6e7681]">{it.count}</span>
              )}
            </button>
          )
        })}
      </div>
    </div>
  )
}

export default function Filters({ facets, value, onChange }: Props) {
  return (
    <aside className="w-56 shrink-0">
      <Group
        title="Difficulty"
        items={(facets?.difficulties ?? []).map((s) => ({ name: s.name, count: s.count }))}
        active={value.difficulty}
        onPick={(v) => onChange({ difficulty: v })}
      />
      <Group
        title="Topic"
        items={facets?.topics ?? []}
        active={value.topic}
        onPick={(v) => onChange({ topic: v })}
      />
      <Group
        title="Pattern"
        items={facets?.patterns ?? []}
        active={value.pattern}
        onPick={(v) => onChange({ pattern: v })}
      />
      <Group
        title="Source"
        items={(facets?.sources ?? []).map((s) => ({ name: s.name, count: s.count }))}
        active={value.source}
        onPick={(v) => onChange({ source: v })}
      />
    </aside>
  )
}
