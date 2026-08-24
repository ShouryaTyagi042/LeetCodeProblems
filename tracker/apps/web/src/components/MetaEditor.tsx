import { useEffect, useState } from 'react'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { DIFFICULTIES, type ProblemDetail } from '@tracker/shared'
import { api } from '../lib/api'
import { Button, Field, inputCls } from './ui'
import TagInput from './TagInput'
import ComboInput from './ComboInput'

const same = (a: string[], b: string[]) =>
  a.length === b.length && a.every((x, i) => x === b[i])

export default function MetaEditor({ problem }: { problem: ProblemDetail }) {
  const qc = useQueryClient()
  // Facets carry every existing topic, pattern and source with counts, and
  // are already cached by the list page.
  const facets = useQuery({ queryKey: ['facets'], queryFn: api.facets })

  const initTopics = problem.topics.map((t) => t.name)
  const initPatterns = problem.patterns.map((t) => t.name)

  const [difficulty, setDifficulty] = useState(problem.difficulty ?? '')
  const [source, setSource] = useState(problem.source ?? '')
  const [judgeUrl, setJudgeUrl] = useState(problem.judgeUrl ?? '')
  const [topics, setTopics] = useState<string[]>(initTopics)
  const [patterns, setPatterns] = useState<string[]>(initPatterns)

  // Re-seed when navigating between problems without unmounting.
  useEffect(() => {
    setDifficulty(problem.difficulty ?? '')
    setSource(problem.source ?? '')
    setJudgeUrl(problem.judgeUrl ?? '')
    setTopics(problem.topics.map((t) => t.name))
    setPatterns(problem.patterns.map((t) => t.name))
  }, [problem])

  const dirty =
    difficulty !== (problem.difficulty ?? '') ||
    source !== (problem.source ?? '') ||
    judgeUrl !== (problem.judgeUrl ?? '') ||
    !same(topics, initTopics) ||
    !same(patterns, initPatterns)

  const save = useMutation({
    mutationFn: () =>
      api.updateProblem(problem.id, {
        difficulty: (difficulty || null) as any,
        source: source.trim() || null,
        judgeUrl: judgeUrl.trim() || null,
        topics,
        patterns,
      }),
    onSuccess: (updated) => {
      qc.setQueryData(['problem', problem.slug], updated)
      qc.invalidateQueries({ queryKey: ['problems'] })
      qc.invalidateQueries({ queryKey: ['facets'] })
      qc.invalidateQueries({ queryKey: ['topics'] })
      qc.invalidateQueries({ queryKey: ['patterns'] })
    },
  })

  return (
    <div className="space-y-3">
      <Field label="Difficulty">
        <select className={inputCls} value={difficulty}
          onChange={(e) => setDifficulty(e.target.value)}>
          <option value="">—</option>
          {DIFFICULTIES.map((d) => <option key={d} value={d}>{d}</option>)}
        </select>
      </Field>

      <Field label="Source">
        <ComboInput
          value={source}
          onChange={setSource}
          options={facets.data?.sources ?? []}
          placeholder="LeetCode, CSES, AlgoZenith…"
        />
      </Field>

      <Field label="Judge URL">
        <input className={inputCls} value={judgeUrl}
          onChange={(e) => setJudgeUrl(e.target.value)} />
      </Field>

      <Field label="Topics">
        <TagInput
          value={topics}
          onChange={setTopics}
          options={facets.data?.topics ?? []}
          placeholder="Start typing to search topics…"
        />
      </Field>

      <Field label="Patterns">
        <TagInput
          value={patterns}
          onChange={setPatterns}
          options={facets.data?.patterns ?? []}
          placeholder="BFS, Interval DP, Sweep Line…"
          tone="accent"
        />
      </Field>

      <div className="flex items-center gap-3">
        <Button variant="primary" disabled={!dirty || save.isPending}
          onClick={() => save.mutate()}>
          {save.isPending ? 'Saving…' : 'Save details'}
        </Button>
        {!dirty && save.isSuccess && (
          <span className="text-[12px] text-[#56d364]">Saved</span>
        )}
      </div>
      {save.isError && (
        <div className="text-[12px] text-[#f85149]">
          {String((save.error as Error).message)}
        </div>
      )}
    </div>
  )
}
