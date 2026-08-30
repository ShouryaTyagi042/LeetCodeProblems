// One draft per problem, shared by the Notes and Details tabs.
//
// The draft deliberately lives above both editors rather than inside them:
// a single Save has to commit whatever is dirty in either tab, and an edit
// must survive switching tabs, which unmounts nothing but does re-render.

import { useEffect, useState } from 'react'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import type { Difficulty, ProblemDetail } from '@tracker/shared'
import { api } from './api'

export interface Draft {
  framework: string
  notes: string
  mistakes: string
  difficulty: string
  source: string
  judgeUrl: string
  topics: string[]
  patterns: string[]
}

export type DraftSetter = <K extends keyof Draft>(key: K, value: Draft[K]) => void

const sameList = (a: string[], b: string[]) =>
  a.length === b.length && a.every((x, i) => x === b[i])

/** The draft as it would be with no unsaved edits — i.e. what the server holds. */
function seed(p: ProblemDetail): Draft {
  return {
    framework: p.note?.framework ?? '',
    notes: p.note?.notes ?? '',
    mistakes: p.note?.mistakes ?? '',
    difficulty: p.difficulty ?? '',
    source: p.source ?? '',
    judgeUrl: p.judgeUrl ?? '',
    topics: p.topics.map((t) => t.name),
    patterns: p.patterns.map((t) => t.name),
  }
}

export function useProblemDraft(problem: ProblemDetail) {
  const qc = useQueryClient()
  const [draft, setDraft] = useState<Draft>(() => seed(problem))

  // Re-seed only when this is a different problem. The page keeps the editors
  // mounted while navigating between problems, and re-seeding on every new
  // query object would discard an unsaved draft whenever the problem
  // refetched — pressing Sync files, for one.
  useEffect(() => {
    setDraft(seed(problem))
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [problem.id])

  const set: DraftSetter = (key, value) => setDraft((d) => ({ ...d, [key]: value }))

  const saved = seed(problem)
  const notesDirty =
    draft.framework !== saved.framework ||
    draft.notes !== saved.notes ||
    draft.mistakes !== saved.mistakes
  const detailsDirty =
    draft.difficulty !== saved.difficulty ||
    draft.source !== saved.source ||
    draft.judgeUrl !== saved.judgeUrl ||
    !sameList(draft.topics, saved.topics) ||
    !sameList(draft.patterns, saved.patterns)

  const save = useMutation({
    mutationFn: async () => {
      // Notes and details are two endpoints, so one Save is two calls at
      // most — only for the halves that actually changed. Run them in
      // sequence rather than together: both return the whole problem, and
      // the later response has to be the one that carries both edits.
      let updated = problem
      if (detailsDirty) {
        updated = await api.updateProblem(problem.id, {
          difficulty: (draft.difficulty || null) as Difficulty | null,
          source: draft.source.trim() || null,
          judgeUrl: draft.judgeUrl.trim() || null,
          topics: draft.topics,
          patterns: draft.patterns,
        })
      }
      if (notesDirty) {
        updated = await api.updateNote(problem.id, {
          framework: draft.framework || null,
          notes: draft.notes || null,
          mistakes: draft.mistakes || null,
        })
      }
      return updated
    },
    onSuccess: (updated) => {
      qc.setQueryData(['problem', problem.slug], updated)
      // Take the stored values back — the server trims source and judgeUrl,
      // so the draft has to follow or it would read as dirty forever.
      setDraft(seed(updated))
      qc.invalidateQueries({ queryKey: ['problems'] })
      qc.invalidateQueries({ queryKey: ['stats'] })
      qc.invalidateQueries({ queryKey: ['facets'] })
      qc.invalidateQueries({ queryKey: ['topics'] })
      qc.invalidateQueries({ queryKey: ['patterns'] })
    },
  })

  return {
    draft,
    set,
    notesDirty,
    detailsDirty,
    dirty: notesDirty || detailsDirty,
    save,
  }
}
