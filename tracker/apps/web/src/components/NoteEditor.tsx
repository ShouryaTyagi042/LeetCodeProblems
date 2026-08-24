import { useEffect, useState } from 'react'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import type { ProblemDetail } from '@tracker/shared'
import { api } from '../lib/api'
import { Button, Field } from './ui'

const area =
  'w-full rounded-md border border-[#30363d] bg-[#0d1117] px-3 py-2 text-[13px] ' +
  'leading-relaxed text-[#e6edf3] outline-none focus:border-[#58a6ff]'

export default function NoteEditor({ problem }: { problem: ProblemDetail }) {
  const qc = useQueryClient()
  const [framework, setFramework] = useState(problem.note?.framework ?? '')
  const [notes, setNotes] = useState(problem.note?.notes ?? '')
  const [mistakes, setMistakes] = useState(problem.note?.mistakes ?? '')

  useEffect(() => {
    setFramework(problem.note?.framework ?? '')
    setNotes(problem.note?.notes ?? '')
    setMistakes(problem.note?.mistakes ?? '')
  }, [problem.id, problem.note])

  const dirty =
    framework !== (problem.note?.framework ?? '') ||
    notes !== (problem.note?.notes ?? '') ||
    mistakes !== (problem.note?.mistakes ?? '')

  const save = useMutation({
    mutationFn: () =>
      api.updateNote(problem.id, {
        framework: framework || null,
        notes: notes || null,
        mistakes: mistakes || null,
      }),
    onSuccess: (updated) => {
      qc.setQueryData(['problem', problem.slug], updated)
      qc.invalidateQueries({ queryKey: ['problems'] })
      qc.invalidateQueries({ queryKey: ['stats'] })
    },
  })

  return (
    <div className="space-y-3">
      <Field label="Framework — the approach, in your words">
        <textarea rows={3} className={area} value={framework}
          onChange={(e) => setFramework(e.target.value)} />
      </Field>
      <Field label="Notes">
        <textarea rows={6} className={area} value={notes}
          onChange={(e) => setNotes(e.target.value)} />
      </Field>
      <Field label="Mistakes — what you got wrong">
        <textarea rows={3} className={area} value={mistakes}
          onChange={(e) => setMistakes(e.target.value)} />
      </Field>
      <div className="flex items-center gap-3">
        <Button variant="primary" disabled={!dirty || save.isPending} onClick={() => save.mutate()}>
          {save.isPending ? 'Saving…' : 'Save notes'}
        </Button>
        {save.isError && (
          <span className="text-[12px] text-[#f85149]">{String(save.error)}</span>
        )}
        {!dirty && save.isSuccess && (
          <span className="text-[12px] text-[#56d364]">Saved</span>
        )}
      </div>
    </div>
  )
}
