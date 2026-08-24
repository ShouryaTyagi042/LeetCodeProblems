import { useState } from 'react'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { DIFFICULTIES, STATUSES, type ProblemDetail } from '@tracker/shared'
import { api } from '../lib/api'
import { Button, Field, inputCls } from './ui'

export default function MetaEditor({ problem }: { problem: ProblemDetail }) {
  const qc = useQueryClient()
  const [difficulty, setDifficulty] = useState(problem.difficulty ?? '')
  const [status, setStatus] = useState(problem.status)
  const [source, setSource] = useState(problem.source ?? '')
  const [judgeUrl, setJudgeUrl] = useState(problem.judgeUrl ?? '')
  const [topics, setTopics] = useState(problem.topics.map((t) => t.name).join(', '))
  const [patterns, setPatterns] = useState(problem.patterns.map((t) => t.name).join(', '))

  const save = useMutation({
    mutationFn: () =>
      api.updateProblem(problem.id, {
        difficulty: (difficulty || null) as any,
        status: status as any,
        source: source || null,
        judgeUrl: judgeUrl || null,
        topics: topics.split(',').map((s) => s.trim()).filter(Boolean),
        patterns: patterns.split(',').map((s) => s.trim()).filter(Boolean),
      }),
    onSuccess: (updated) => {
      qc.setQueryData(['problem', problem.slug], updated)
      qc.invalidateQueries({ queryKey: ['problems'] })
      qc.invalidateQueries({ queryKey: ['facets'] })
    },
  })

  return (
    <div className="space-y-3">
      <div className="grid grid-cols-2 gap-3">
        <Field label="Difficulty">
          <select className={inputCls} value={difficulty}
            onChange={(e) => setDifficulty(e.target.value)}>
            <option value="">—</option>
            {DIFFICULTIES.map((d) => <option key={d} value={d}>{d}</option>)}
          </select>
        </Field>
        <Field label="Status">
          <select className={inputCls} value={status}
            onChange={(e) => setStatus(e.target.value as any)}>
            {STATUSES.map((s) => <option key={s} value={s}>{s}</option>)}
          </select>
        </Field>
      </div>
      <Field label="Source">
        <input className={inputCls} value={source} onChange={(e) => setSource(e.target.value)} />
      </Field>
      <Field label="Judge URL">
        <input className={inputCls} value={judgeUrl} onChange={(e) => setJudgeUrl(e.target.value)} />
      </Field>
      <Field label="Topics (comma separated)">
        <input className={inputCls} value={topics} onChange={(e) => setTopics(e.target.value)} />
      </Field>
      <Field label="Patterns (comma separated)">
        <input className={inputCls} value={patterns} onChange={(e) => setPatterns(e.target.value)} />
      </Field>
      <Button variant="primary" disabled={save.isPending} onClick={() => save.mutate()}>
        {save.isPending ? 'Saving…' : 'Save details'}
      </Button>
      {save.isError && <div className="text-[12px] text-[#f85149]">{String(save.error)}</div>}
    </div>
  )
}
