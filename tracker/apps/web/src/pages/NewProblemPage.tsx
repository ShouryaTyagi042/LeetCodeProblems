import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { DIFFICULTIES } from '@tracker/shared'
import { api } from '../lib/api'
import { Button, Field, inputCls } from '../components/ui'
import ComboInput from '../components/ComboInput'

export default function NewProblemPage() {
  const nav = useNavigate()
  const qc = useQueryClient()
  const facets = useQuery({ queryKey: ['facets'], queryFn: api.facets })

  const [title, setTitle] = useState('')
  const [topic, setTopic] = useState('')
  const [folderName, setFolderName] = useState('')
  const [difficulty, setDifficulty] = useState('')
  const [source, setSource] = useState('')
  const [judgeUrl, setJudgeUrl] = useState('')
  const [scaffold, setScaffold] = useState(true)

  const derived = folderName || title.replace(/[^A-Za-z0-9]+/g, '')
  const topicDir = topic.replace(/[^A-Za-z0-9]+/g, '')

  const create = useMutation({
    mutationFn: () =>
      api.createProblem({
        title, topic, folderName: derived,
        difficulty: (difficulty || null) as any,
        source: source || null, judgeUrl: judgeUrl || null, scaffold,
      }),
    onSuccess: (p) => {
      qc.invalidateQueries()
      nav(`/problems/${p.slug}`)
    },
  })

  return (
    <div className="mx-auto max-w-xl">
      <h1 className="mb-1 text-lg font-semibold">New problem</h1>
      <p className="mb-5 text-[13px] text-[#8b949e]">
        Runs <code className="font-mono text-[12px]">cp_setup.sh</code> to create the folder,
        so the scaffold stays exactly what the script produces.
      </p>

      <div className="space-y-3">
        <Field label="Title">
          <input className={inputCls} value={title} autoFocus
            onChange={(e) => setTitle(e.target.value)} placeholder="Rotting Oranges" />
        </Field>

        <Field label="Topic">
          <ComboInput
            value={topic}
            onChange={setTopic}
            options={facets.data?.topics ?? []}
            placeholder="Start typing to search topics…"
          />
        </Field>

        <Field label="Folder name (optional — derived from title)">
          <input className={inputCls} value={folderName}
            onChange={(e) => setFolderName(e.target.value)} placeholder={derived} />
        </Field>

        <div className="grid grid-cols-2 gap-3">
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
              placeholder="LeetCode"
            />
          </Field>
        </div>

        <Field label="Judge URL">
          <input className={inputCls} value={judgeUrl}
            onChange={(e) => setJudgeUrl(e.target.value)} />
        </Field>

        <label className="flex items-center gap-2 text-[13px] text-[#c9d1d9]">
          <input type="checkbox" checked={scaffold}
            onChange={(e) => setScaffold(e.target.checked)} />
          Create the folder on disk with cp_setup.sh
        </label>

        {topicDir && derived && (
          <div className="rounded-md border border-[#262d36] bg-[#0d1117] px-3 py-2 font-mono text-[12px] text-[#8b949e]">
            topics/{topicDir}/{derived}/
          </div>
        )}

        {create.isError && (
          <div className="rounded-md border border-[#7d2429] bg-[#3d1418] px-3 py-2 text-[12px] text-[#f85149]">
            {String((create.error as Error).message)}
          </div>
        )}

        <div className="flex gap-2 pt-1">
          <Button variant="primary"
            disabled={!title.trim() || !topic.trim() || create.isPending}
            onClick={() => create.mutate()}>
            {create.isPending ? 'Creating…' : 'Create problem'}
          </Button>
          <Button onClick={() => nav('/problems')}>Cancel</Button>
        </div>
      </div>
    </div>
  )
}
