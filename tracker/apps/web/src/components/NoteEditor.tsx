import type { Draft, DraftSetter } from '../lib/useProblemDraft'
import { Field } from './ui'

const area =
  'w-full rounded-md border border-[#30363d] bg-[#0d1117] px-3 py-2 text-[13px] ' +
  'leading-relaxed text-[#e6edf3] outline-none focus:border-[#58a6ff]'

/**
 * Notes half of the problem draft. The draft and the Save button both live on
 * the page, so one Save covers this tab and Details together.
 */
export default function NoteEditor({ draft, set }: { draft: Draft; set: DraftSetter }) {
  return (
    <div className="space-y-3">
      <Field label="Framework — the approach, in your words">
        <textarea rows={3} className={area} value={draft.framework}
          onChange={(e) => set('framework', e.target.value)} />
      </Field>
      <Field label="Notes">
        <textarea rows={6} className={area} value={draft.notes}
          onChange={(e) => set('notes', e.target.value)} />
      </Field>
      <Field label="Mistakes — what you got wrong">
        <textarea rows={3} className={area} value={draft.mistakes}
          onChange={(e) => set('mistakes', e.target.value)} />
      </Field>
    </div>
  )
}
