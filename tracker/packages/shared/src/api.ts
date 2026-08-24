// Transport-agnostic API client. The web app and the future React Native
// app both use this — only the baseUrl differs.

import type {
  CreateProblemInput, Facets, ForecastDay, GradeResult, Paged, ProblemDetail,
  CardInfo, ProblemQuery, ProblemSummary, ReviewQueue, ReviewStats, Stats, Tag,
  TopicDueRow, UpdateNoteInput, UpdateProblemInput,
} from './types.js'

export class ApiError extends Error {
  constructor(public status: number, message: string) {
    super(message)
    this.name = 'ApiError'
  }
}

export interface ApiOptions {
  baseUrl: string
  token?: string
  fetch?: typeof globalThis.fetch
}

export function createApi(opts: ApiOptions) {
  const doFetch = opts.fetch ?? globalThis.fetch.bind(globalThis)

  async function req<T>(path: string, init?: RequestInit): Promise<T> {
    const res = await doFetch(`${opts.baseUrl}${path}`, {
      ...init,
      headers: {
        'content-type': 'application/json',
        ...(opts.token ? { authorization: `Bearer ${opts.token}` } : {}),
        ...(init?.headers ?? {}),
      },
    })
    if (!res.ok) {
      const body = await res.text().catch(() => '')
      let msg = body
      try { msg = JSON.parse(body).message ?? body } catch { /* plain text */ }
      throw new ApiError(res.status, msg || res.statusText)
    }
    return res.status === 204 ? (undefined as T) : ((await res.json()) as T)
  }

  const qs = (q: Record<string, unknown>) => {
    const p = new URLSearchParams()
    for (const [k, v] of Object.entries(q)) {
      if (v !== undefined && v !== null && v !== '') p.set(k, String(v))
    }
    const s = p.toString()
    return s ? `?${s}` : ''
  }

  return {
    listProblems: (q: ProblemQuery = {}) =>
      req<Paged<ProblemSummary>>(`/api/problems${qs(q as Record<string, unknown>)}`),

    // Encode per segment so a slug keeps its '/' as a real path separator
    // rather than %2F, which many proxies reject.
    getProblem: (slugOrId: string) =>
      req<ProblemDetail>(
        `/api/problems/${slugOrId.split('/').map(encodeURIComponent).join('/')}`,
      ),

    updateProblem: (id: string, body: UpdateProblemInput) =>
      req<ProblemDetail>(`/api/problems/${id}`, { method: 'PATCH', body: JSON.stringify(body) }),

    updateNote: (id: string, body: UpdateNoteInput) =>
      req<ProblemDetail>(`/api/problems/${id}/note`, { method: 'PUT', body: JSON.stringify(body) }),

    createProblem: (body: CreateProblemInput) =>
      req<ProblemDetail>('/api/problems', { method: 'POST', body: JSON.stringify(body) }),

    deleteProblem: (id: string) =>
      req<void>(`/api/problems/${id}`, { method: 'DELETE' }),

    facets: () => req<Facets>('/api/facets'),
    stats: () => req<Stats>('/api/stats'),
    topics: () => req<Tag[]>('/api/topics'),
    patterns: () => req<Tag[]>('/api/patterns'),
    sync: () => req<{ created: number; updated: number; removed: number }>('/api/sync', { method: 'POST' }),

    // ---- review ----
    reviewQueue: (limit = 30, topic?: string) =>
      req<ReviewQueue>(
        `/api/review/queue?limit=${limit}${topic ? `&topic=${encodeURIComponent(topic)}` : ''}`,
      ),
    reviewStats: () => req<ReviewStats>('/api/review/stats'),
    reviewForecast: (days = 14) => req<ForecastDay[]>(`/api/review/forecast?days=${days}`),
    gradeProblem: (problemId: string, outcome: 'good' | 'again', durationMs?: number) =>
      req<GradeResult>(`/api/review/${problemId}/grade`, {
        method: 'POST',
        body: JSON.stringify({ outcome, durationMs }),
      }),
    topicsDue: () => req<TopicDueRow[]>('/api/revision/topics'),

    setSuspended: (problemId: string, suspended: boolean) =>
      req<CardInfo>(`/api/review/${problemId}/suspend`, {
        method: 'POST',
        body: JSON.stringify({ suspended }),
      }),
  }
}

export type Api = ReturnType<typeof createApi>
