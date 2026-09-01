import { Suspense, lazy, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import type { ProblemDetail } from '@tracker/shared'
import { api } from '../lib/api'
// CodeMirror is ~60% of the bundle and only the detail page needs it.
const CodeView = lazy(() => import('../components/CodeView'))
import NoteEditor from '../components/NoteEditor'
import MetaEditor from '../components/MetaEditor'
import { useProblemDraft } from '../lib/useProblemDraft'
import { ArrowLeft, ExternalLink } from '../components/icons'
import { Button, Chip, Empty, buttonCls, cx, difficultyTone } from '../components/ui'

type Tab = 'notes' | 'details' | 'tests'

export default function ProblemPage() {
  const { topic, name } = useParams()
  const slug = `${topic}/${name}`
  const q = useQuery({ queryKey: ['problem', slug], queryFn: () => api.getProblem(slug) })

  if (q.isLoading) return <Empty>Loading…</Empty>
  if (q.isError || !q.data) return <Empty>Problem not found.</Empty>
  // Keyed on the problem so switching problems starts a clean draft rather
  // than carrying one across.
  return <Loaded key={q.data.id} p={q.data} slug={slug} />
}

/** Split out so the draft hook only ever runs against a problem that exists. */
function Loaded({ p, slug }: { p: ProblemDetail; slug: string }) {
  const [tab, setTab] = useState<Tab>('notes')
  const [fileIdx, setFileIdx] = useState(0)
  const qc = useQueryClient()
  const { draft, set, savedCount, notesDirty, detailsDirty, dirty, save } = useProblemDraft(p)

  // Re-reads this problem's files only, so it can be pressed right after
  // editing a solution without waiting on a full-repo sync.
  const sync = useMutation({
    mutationFn: () => api.syncProblem(p.id),
    onSuccess: (res) => {
      qc.setQueryData(['problem', slug], res.problem)
      qc.invalidateQueries({ queryKey: ['problems'] })
      qc.invalidateQueries({ queryKey: ['stats'] })
      qc.invalidateQueries({ queryKey: ['review'] })
    },
  })

  const sol = p.solutions[fileIdx]

  return (
    <div>
      <div className="mb-4">
        {/* -ml-3 cancels the button's own padding so the label lines up with
            the title beneath it rather than sitting indented from it. */}
        <Link to="/problems" className={cx(buttonCls('ghost'), '-ml-3')}>
          <ArrowLeft size={14} />All problems
        </Link>
        <div className="mt-1 flex flex-wrap items-center gap-2">
          <h1 className="text-lg font-semibold">{p.title}</h1>
          {dirty && (
            <span className="rounded-full border border-[#9e6a03] bg-[#3b2300] px-2 py-0.5 text-[11px] text-[#e3b341]">
              ● Unsaved draft
            </span>
          )}
          {p.difficulty && <Chip tone={difficultyTone(p.difficulty)}>{p.difficulty}</Chip>}
          {p.source && <Chip>{p.source}</Chip>}
          {p.judgeUrl && (
            <a href={p.judgeUrl} target="_blank" rel="noreferrer" className={buttonCls()}>
              Open on judge<ExternalLink size={13} />
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
          <div className="mb-3 flex items-center gap-1 border-b border-[#262d36]">
            {(['notes', 'details', 'tests'] as Tab[]).map((t) => (
              <button key={t} onClick={() => setTab(t)}
                className={cx(
                  '-mb-px border-b-2 px-3 py-2 text-[13px] capitalize',
                  tab === t
                    ? 'border-[#58a6ff] text-[#e6edf3]'
                    : 'border-transparent text-[#8b949e] hover:text-[#e6edf3]',
                )}>
                {t}
                {/* A dot marks which tab holds the unsaved edit, so it is
                    findable from whichever tab happens to be open. */}
                {((t === 'notes' && notesDirty) || (t === 'details' && detailsDirty)) && (
                  <span className="ml-1 text-[#e3b341]">●</span>
                )}
              </button>
            ))}

            {/* One Save for the whole problem: notes and details are separate
                endpoints, but that is not a distinction worth pushing onto
                whoever is writing them up. */}
            <div className="ml-auto flex items-center gap-2 pb-1.5">
              {save.isError && (
                <span className="text-[11px] text-[#f85149]">
                  {String((save.error as Error).message)}
                </span>
              )}
              {!dirty && save.isSuccess && (
                <span className="text-[11px] text-[#56d364]">Saved</span>
              )}
              <Button variant="primary" disabled={!dirty || save.isPending}
                onClick={() => save.mutate()}>
                {save.isPending ? 'Saving…' : 'Save'}
              </Button>
            </div>
          </div>

          {/* Panels stay mounted and are hidden with CSS rather than being
              unmounted on a tab switch, which would throw away an unsaved
              draft along with the component's state. */}
          <div className={cx(tab !== 'notes' && 'hidden')}>
            <NoteEditor draft={draft} set={set} savedCount={savedCount} />
          </div>
          <div className={cx(tab !== 'details' && 'hidden')}>
            <MetaEditor draft={draft} set={set} />
          </div>
          <div className={cx(tab !== 'tests' && 'hidden')}>
            {p.testcases.length === 0 ? (
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
            )}
          </div>
        </section>
      </div>
    </div>
  )
}
