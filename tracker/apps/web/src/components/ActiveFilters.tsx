import type { Facets } from '@tracker/shared'
import { X } from './icons'

const KEYS = ['topic', 'pattern', 'source', 'difficulty'] as const
type Key = (typeof KEYS)[number]

const LABEL: Record<Key, string> = {
  topic: 'topic', pattern: 'pattern', source: 'source',
  difficulty: 'difficulty',
}

/** slug -> display name, so a chip reads "0-1 BFS" not "0-1-bfs". */
function display(key: Key, value: string, facets?: Facets): string {
  if (key === 'topic') return facets?.topics.find((t) => t.slug === value)?.name ?? value
  if (key === 'pattern') return facets?.patterns.find((p) => p.slug === value)?.name ?? value
  return value
}

export default function ActiveFilters({
  value, facets, onClear, onClearAll,
}: {
  value: Record<string, string>
  facets?: Facets
  onClear: (key: Key) => void
  onClearAll: () => void
}) {
  const active = KEYS.filter((k) => value[k])
  if (!active.length) return null

  return (
    <div className="mb-3 flex flex-wrap items-center gap-1.5">
      {active.map((k) => (
        <span
          key={k}
          className="inline-flex items-center gap-1.5 rounded-full border border-[#1f4e8c] bg-[#0d2d5e] py-0.5 pl-2 pr-1 text-[11px] text-[#79c0ff]"
        >
          <span className="text-[#58a6ff]/60">{LABEL[k]}:</span>
          {display(k, value[k], facets)}
          <button
            onClick={() => onClear(k)}
            aria-label={`Clear ${LABEL[k]} filter`}
            className="inline-flex items-center rounded-full px-1 hover:bg-[#1f4e8c]"
          >
            <X size={11} />
          </button>
        </span>
      ))}
      {active.length > 1 && (
        <button onClick={onClearAll} className="ml-1 text-[11px] text-[#8b949e] hover:text-[#58a6ff]">
          clear all
        </button>
      )}
    </div>
  )
}
