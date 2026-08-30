import { useQuery } from '@tanstack/react-query'
import { DIFFICULTIES } from '@tracker/shared'
import { api } from '../lib/api'
import type { Draft, DraftSetter } from '../lib/useProblemDraft'
import { Field, inputCls } from './ui'
import TagInput from './TagInput'
import ComboInput from './ComboInput'

/**
 * Details half of the problem draft. The draft and the Save button both live
 * on the page, so one Save covers this tab and Notes together.
 */
export default function MetaEditor({ draft, set }: { draft: Draft; set: DraftSetter }) {
  // Facets carry every existing topic, pattern and source with counts, and
  // are already cached by the list page.
  const facets = useQuery({ queryKey: ['facets'], queryFn: api.facets })

  return (
    <div className="space-y-3">
      <Field label="Difficulty">
        <select className={inputCls} value={draft.difficulty}
          onChange={(e) => set('difficulty', e.target.value)}>
          <option value="">—</option>
          {DIFFICULTIES.map((d) => <option key={d} value={d}>{d}</option>)}
        </select>
      </Field>

      <Field label="Source">
        <ComboInput
          value={draft.source}
          onChange={(v) => set('source', v)}
          options={facets.data?.sources ?? []}
          placeholder="LeetCode, CSES, AlgoZenith…"
        />
      </Field>

      <Field label="Judge URL">
        <input className={inputCls} value={draft.judgeUrl}
          onChange={(e) => set('judgeUrl', e.target.value)} />
      </Field>

      <Field label="Topics">
        <TagInput
          value={draft.topics}
          onChange={(v) => set('topics', v)}
          options={facets.data?.topics ?? []}
          placeholder="Start typing to search topics…"
        />
      </Field>

      <Field label="Patterns">
        <TagInput
          value={draft.patterns}
          onChange={(v) => set('patterns', v)}
          options={facets.data?.patterns ?? []}
          placeholder="BFS, Interval DP, Sweep Line…"
          tone="accent"
        />
      </Field>
    </div>
  )
}
