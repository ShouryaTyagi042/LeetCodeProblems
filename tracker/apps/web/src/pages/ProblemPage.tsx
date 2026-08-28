import { Suspense, lazy, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { api } from '../lib/api'
// CodeMirror is ~60% of the bundle and only the detail page needs it.
const CodeView = lazy(() => import('../components/CodeView'))
import NoteEditor from '../components/NoteEditor'
import MetaEditor from '../components/MetaEditor'
import { Button, Chip, Empty, cx, difficultyTone } from '../components/ui'

type Tab = 'notes' | 'details' | 'tests'

export default function ProblemPage() {
  const { topic, name } = useParams()
  const slug = `${topic}/${name}`
  const [tab, setTab] = useState<Tab>('notes')
  const [fileIdx, setFileIdx] = useState(0)

  const qc = useQueryClient()
  const q = useQuery({ queryKey: ['problem', slug], queryFn: () => api.getProblem(slug) })

  // Re-reads this problem's files only, so it can be pressed right after
  // editing a solution without waiting on a full-repo sync.
  const sync = useMutation({
    mutationFn: () => api.syncProblem(q.data!.id),
    onSuccess: (res) => {
      qc.setQueryData(['problem', slug], res.problem)
      qc.invalidateQueries({ queryKey: ['problems'] })
      qc.invalidateQueries({ queryKey: ['stats'] })
      qc.invalidateQueries({ queryKey: ['review'] })
    },
  })

  if (q.isLoading) return <Empty>Loading…</Empty>
  if (q.isError || !q.data) return <Empty>Problem not found.</Empty>
  const p = q.data
  const sol = p.solutions[fileIdx]

  return (
    <div>
      <div className="mb-4">
        <Link to="/problems" className="text-[12px] text-[#8b949e] hover:text-[#58a6ff]">
          ← All problems
        </Link>
        <div className="mt-1 flex flex-wrap items-center gap-2">
          <h1 className="text-lg font-semibold">{p.title}</h1>
          {p.difficulty && <Chip tone={difficultyTone(p.difficulty)}>{p.difficulty}</Chip>}
          {p.source && <Chip>{p.source}</Chip>}
          {p.judgeUrl && (
            <a href={p.judgeUrl} target="_blank" rel="noreferrer"
              className="text-[12px] text-[#58a6ff] hover:underline">
              Open on judge ↗
            </a>
          )}
          <div className="ml-auto flex items-center gap-2">
            {sync.isSuccess && !sync.isPending && (
              <span className="text-[11px] text-[#56d364]">
                {sync.data.files} file{sync.data.files === 1 ? '' : 's'},{' '}
                {sync.data.loc} lines
                {sync.data.removedFiles > 0 && `, ${sync.data.removedFiles} removed`}
              </span>
            )}
            {sync.isError && (
              <span className="text-[11px] text-[#f85149]">
                {String((sync.error as Error).message)}
              </span>
            )}
            <Button onClick={() => sync.mutate()} disabled={sync.isPending}>
              {sync.isPending ? 'Reading files…' : 'Sync files'}
            </Button>
          </div>
        </div>
        <div className="mt-1.5 flex flex-wrap gap-1">
          {p.topics.map((t) => <Chip key={t.id}>{t.name}</Chip>)}
          {p.patterns.map((t) => <Chip key={t.id} tone="accent">{t.name}</Chip>)}
        </div>
        <div className="mt-1 font-mono text-[11px] text-[#6e7681]">{p.folderPath}</div>
      </div>

      <div className="grid gap-5 lg:grid-cols-[minmax(0,1fr)_420px]">
        <section className="min-w-0">
          {p.solutions.length > 1 && (
            <div className="mb-2 flex gap-1">
              {p.solutions.map((s, i) => (
                <button key={s.id} onClick={() => setFileIdx(i)}
                  className={cx(
                    'rounded px-2 py-1 font-mono text-[11px]',
                    i === fileIdx ? 'bg-[#21262d] text-[#e6edf3]' : 'text-[#6e7681]',
                  )}>
                  {s.filePath.split('/').pop()}
                </button>
              ))}
            </div>
          )}
          {sol ? (
            <div className="overflow-hidden rounded-lg border border-[#262d36]">
              <div className="flex items-center justify-between border-b border-[#262d36] bg-[#161b22] px-3 py-1.5">
                <span className="font-mono text-[11px] text-[#8b949e]">{sol.filePath}</span>
                <span className="text-[11px] text-[#6e7681]">{sol.loc} lines</span>
              </div>
              <Suspense fallback={<div className="p-6 text-[12px] text-[#6e7681]">Loading editor…</div>}>
                <CodeView code={sol.code} />
              </Suspense>
            </div>
          ) : (
            <div className="rounded-lg border border-[#262d36] p-8 text-center text-[13px] text-[#6e7681]">
              No solution file in this folder yet.
            </div>
          )}
        </section>

        <section className="min-w-0">
          <div className="mb-3 flex gap-1 border-b border-[#262d36]">
            {(['notes', 'details', 'tests'] as Tab[]).map((t) => (
              <button key={t} onClick={() => setTab(t)}
                className={cx(
                  '-mb-px border-b-2 px-3 py-2 text-[13px] capitalize',
                  tab === t
                    ? 'border-[#58a6ff] text-[#e6edf3]'
                    : 'border-transparent text-[#8b949e] hover:text-[#e6edf3]',
                )}>
                {t}
              </button>
            ))}
          </div>

          {tab === 'notes' && <NoteEditor problem={p} />}
          {tab === 'details' && <MetaEditor problem={p} />}
          {tab === 'tests' && (
            p.testcases.length === 0 ? (
              <Empty>No test data.</Empty>
            ) : (
              p.testcases.map((tc) => (
                <div key={tc.id} className="mb-3 grid grid-cols-2 gap-3">
                  <div>
                    <div className="mb-1 text-[11px] uppercase tracking-wide text-[#8b949e]">input.txt</div>
                    <pre className="max-h-72 overflow-auto rounded-md border border-[#262d36] bg-[#0d1117] p-2 font-mono text-[11px] leading-relaxed">
                      {tc.input || '(empty)'}
                    </pre>
                  </div>
                  <div>
                    <div className="mb-1 text-[11px] uppercase tracking-wide text-[#8b949e]">expected.txt</div>
                    <pre className="max-h-72 overflow-auto rounded-md border border-[#262d36] bg-[#0d1117] p-2 font-mono text-[11px] leading-relaxed">
                      {tc.expected || '(empty)'}
                    </pre>
                  </div>
                </div>
              ))
            )
          )}
        </section>
      </div>
    </div>
  )
}
