import { Link, Outlet, useLocation } from 'react-router-dom'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { api } from '../lib/api'
import { Button } from './ui'

export default function Shell() {
  const qc = useQueryClient()
  const { pathname } = useLocation()
  const stats = useQuery({ queryKey: ['stats'], queryFn: api.stats })
  const review = useQuery({
    queryKey: ['review', 'stats'],
    queryFn: api.reviewStats,
    staleTime: 0,
  })
  const sync = useMutation({
    mutationFn: api.sync,
    onSuccess: () => qc.invalidateQueries(),
  })

  return (
    <div className="flex min-h-full flex-col">
      <header className="sticky top-0 z-20 border-b border-[#262d36] bg-[#0e1116]/95 backdrop-blur">
        <div className="mx-auto flex max-w-[1400px] items-center gap-4 px-5 py-3">
          <Link to="/problems" className="text-sm font-semibold tracking-tight">
            DSA<span className="text-[#58a6ff]">Tracker</span>
          </Link>

          <nav className="flex items-center gap-1 text-[13px]">
            <Link
              to="/problems"
              className={
                'rounded px-2 py-1 ' +
                (pathname.startsWith('/problems') ? 'bg-[#21262d] text-[#e6edf3]' : 'text-[#8b949e] hover:text-[#e6edf3]')
              }
            >
              Problems
            </Link>
            <Link
              to="/review"
              className={
                'flex items-center gap-1.5 rounded px-2 py-1 ' +
                (pathname === '/review' ? 'bg-[#21262d] text-[#e6edf3]' : 'text-[#8b949e] hover:text-[#e6edf3]')
              }
            >
              Review
              {!!review.data?.due && (
                <span className="rounded-full bg-[#1f6feb] px-1.5 text-[10px] font-semibold text-white">
                  {review.data.due}
                </span>
              )}
            </Link>
          </nav>

          {stats.data && (
            <div className="hidden gap-4 text-[11px] text-[#8b949e] sm:flex">
              <span><b className="text-[#e6edf3]">{stats.data.problems}</b> problems</span>
              <span><b className="text-[#56d364]">{stats.data.solved}</b> solved</span>
              <span><b className="text-[#e6edf3]">{stats.data.withNotes}</b> with notes</span>
              <span><b className="text-[#e6edf3]">{stats.data.patterns}</b> patterns</span>
            </div>
          )}

          <div className="ml-auto flex items-center gap-2">
            {sync.data && (
              <span className="text-[11px] text-[#8b949e]">
                +{sync.data.created} / ~{sync.data.updated}
              </span>
            )}
            <Button onClick={() => sync.mutate()} disabled={sync.isPending}>
              {sync.isPending ? 'Syncing…' : 'Sync repo'}
            </Button>
            {pathname !== '/problems/new' && (
              <Link to="/problems/new">
                <Button variant="primary">New problem</Button>
              </Link>
            )}
          </div>
        </div>
      </header>

      <main className="mx-auto w-full max-w-[1400px] flex-1 px-5 py-5">
        <Outlet />
      </main>
    </div>
  )
}
