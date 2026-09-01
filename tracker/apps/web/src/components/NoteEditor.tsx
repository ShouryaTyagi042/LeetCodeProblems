import { useEffect, useState } from 'react'
import type { Draft, DraftSetter } from '../lib/useProblemDraft'
import { Field, buttonCls, cx } from './ui'
import { Eye, Pencil } from './icons'
import Markdown from './Markdown'

const area =
  'w-full rounded-md border border-[#30363d] bg-[#0d1117] px-3 py-2 font-mono text-[12px] ' +
  'leading-relaxed text-[#e6edf3] outline-none focus:border-[#58a6ff]'

const FIELDS = [
  { key: 'framework', label: 'Framework — the approach, in your words', rows: 4 },
  { key: 'notes', label: 'Notes', rows: 10 },
  { key: 'mistakes', label: 'Mistakes — what you got wrong', rows: 4 },
] as const

/**
 * Notes half of the problem draft. The draft and the Save button both live on
 * the page, so one Save covers this tab and Details together.
 *
 * The three fields are markdown. Editing shows the source in a monospace
 * textarea; saving is what commits it, so saving is also what switches to
 * the rendered view.
 */
export default function NoteEditor({
  draft, set, savedCount,
}: {
  draft: Draft
  set: DraftSetter
  savedCount: number
}) {
  const filled = FIELDS.filter((f) => draft[f.key].trim())
  // Start on whichever side is useful: rendered when there is something to
  // read, the textareas when the note is still empty.
  const [editing, setEditing] = useState(filled.length === 0)

  useEffect(() => {
    if (savedCount > 0) setEditing(false)
  }, [savedCount])

  return (
    <div className="space-y-3">
      <div className="flex items-center justify-between">
        <span className="text-[11px] text-[#6e7681]">
          {editing ? 'Markdown — saving renders it' : 'Rendered markdown'}
        </span>
        <button
          onClick={() => setEditing((v) => !v)}
          className={cx(buttonCls('ghost'), 'text-[#8b949e]')}
        >
          {editing
            ? <><Eye size={13} />Preview</>
            : <><Pencil size={13} />Edit</>}
        </button>
      </div>

      {editing ? (
        FIELDS.map((f) => (
          <Field key={f.key} label={f.label}>
            <textarea
              rows={f.rows}
              className={area}
              value={draft[f.key]}
              onChange={(e) => set(f.key, e.target.value)}
            />
          </Field>
        ))
      ) : filled.length === 0 ? (
        <p className="py-6 text-center text-[12px] text-[#6e7681]">
          Nothing written up yet. Press Edit to start.
        </p>
      ) : (
        // Only what has content: an empty heading over an empty box is noise
        // when the point of this side is reading it back.
        filled.map((f) => (
          <div key={f.key}>
            <div className="mb-1 text-[11px] font-medium uppercase tracking-wide text-[#8b949e]">
              {f.label}
            </div>
            <div className="rounded-md border border-[#262d36] bg-[#0d1117] px-3 py-2">
              <Markdown>{draft[f.key]}</Markdown>
            </div>
          </div>
        ))
      )}
    </div>
  )
}
