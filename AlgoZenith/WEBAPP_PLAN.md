# DSA Practice Tracker — Web App & Backend Plan

A plan to replace the Notion database + folder tree with a purpose-built
app: React on the web now, React Native later, one API and one data model
behind both.

Written 2026-08-22. Reflects repo state at commit `0140ca7`.

---

## 1. Why build this

The current setup is three systems that don't talk to each other:

| System | Holds | Problem |
|---|---|---|
| `topics/` in this repo | 213 problems, 41k lines of Java, test data | No metadata. Folder name is the only description. |
| Notion `Questions Database` | 87 rows: difficulty, links, approach, mistakes, review dates | Covers only 41% of solved problems. No link to the code. |
| Your head | Which problems you actually remember | Doesn't scale past ~200 problems. |

`notion_folder_map.csv` now joins the first two, but it is a static file
that goes stale the moment you solve something new. The count is growing
fast enough that manual reconciliation is already the bottleneck — that
is the actual reason to build this, not the UI.

**What the app is for:** deciding what to revise next, and finding the
problem you half-remember. Everything else is secondary.

---

## 2. Hard constraints

These are non-negotiable and shape every decision below.

1. **`Main.java` must stay a single self-contained file.** Solutions get
   pasted into online judge IDEs (Codeforces, AtCoder, CSES, LeetCode) —
   no classpath, no second file, no build step. The app must never
   require restructuring solution files.
2. **Git stays the source of truth for code.** `run.sh`, `cp_setup.sh`,
   and `git log --follow` all work today. The database indexes the repo;
   it does not replace it. If the app dies, nothing is lost.
3. **Single user.** No multi-tenancy, no sharing, no roles. Resist every
   temptation to build for users who don't exist.
4. **Offline-tolerant on mobile.** Revision happens on a phone, on a
   commute, without signal.

---

## 3. Architecture

API-first, so the web app and the future React Native app are two clients
of one backend. No server-rendered HTML, no logic that lives only in the
web layer.

```
  ┌────────────────┐     ┌──────────────────┐
  │  React (web)   │     │  React Native    │   <- later
  │  Vite + TS     │     │  Expo            │
  └────────┬───────┘     └────────┬─────────┘
           │                      │
           └──────────┬───────────┘
                      │  shared/ : API client, types, scheduling logic
                      ▼
              ┌───────────────┐
              │  REST API     │  Fastify + TypeScript
              │  + scheduler  │
              └───────┬───────┘
                      ▼
              ┌───────────────┐        ┌──────────────────┐
              │  PostgreSQL   │ <───── │  sync job        │
              └───────────────┘        │  reads git repo  │
                                       └──────────────────┘
```

**The sync job is the important piece.** It walks `topics/*/*/`, reads
`Main.java` + `input.txt` + `expected.txt`, and upserts into Postgres
keyed on folder path. Run it on a git hook or on demand. This is what
kills the manual reconciliation problem permanently: solving a problem
and committing it *is* the act of registering it.

### Monorepo layout

```
tracker/
  apps/
    web/          React + Vite
    api/          Fastify
    mobile/       Expo            (phase 4)
  packages/
    shared/       types, API client, spaced-repetition logic
    db/           Prisma schema + migrations
  tools/
    sync/         repo -> DB indexer
    import/       one-time Notion CSV import
```

`packages/shared` is what makes React Native cheap later. Put the API
client, the domain types, and the scheduling maths there from day one —
even while there is only one consumer. **Share logic, not components.**
Attempting to share UI via `react-native-web` costs more than it saves at
this size.

---

## 4. Tech choices

| Layer | Choice | Why |
|---|---|---|
| Language | TypeScript everywhere | One set of types from DB to mobile. The alternative (Python API) means hand-maintaining types twice. |
| Backend | Fastify | Small, fast, unopinionated. NestJS is too much structure for one user. |
| ORM | Prisma | Generates TS types from the schema, so `shared/` gets them free. |
| DB | PostgreSQL (Neon or Supabase free tier) | Full-text search, JSONB, arrays — no separate search service needed at this scale. |
| Frontend | React + Vite + TanStack Query | Query handles caching/refetch, which is most of the client state. |
| Styling | Tailwind | Fast, and the class names don't transfer to RN anyway. |
| Editor/viewer | CodeMirror 6 | Java syntax highlighting, works on mobile. |
| Auth | Single API token in an env var | One user. Do not build login. Revisit only if this ever goes public. |
| Hosting | Vercel (web) + Fly.io (API) | Free tiers cover this comfortably. |

**Decision to revisit, not now:** Supabase would remove most of the API
layer (Postgres + auth + generated REST client). It is genuinely tempting
for a single-user app. The reason to keep a thin API anyway is the
scheduling engine and the sync job, which want real server code. If phase 1 feels
like boilerplate, switch — the data model below is unaffected.

---

## 5. Data model

```sql
problem
  id              uuid pk
  slug            text unique         -- 'graphs/dijkstras'
  title           text                -- 'Dijkstra's Shortest Path'
  folder_path     text unique         -- 'topics/Graphs/Dijkstras'
  source          text                -- AlgoZenith | LeetCode | CSES | AtCoder | Codeforces
  judge_url       text
  difficulty      text                -- Easy | Medium | Hard
  status          text                -- unsolved | solved | needs_review
  first_solved_at timestamptz
  created_at      timestamptz

topic              id, name, slug                    -- Graphs, Two Pointers  (26 today)
pattern            id, name, slug                    -- BFS, Dijkstras, 0-1 BFS, Game DP  (34 today)
problem_topic      problem_id, topic_id              -- m2m
problem_pattern    problem_id, pattern_id            -- m2m

solution
  id, problem_id, language, code text, file_path,
  commit_sha, loc int, synced_at

testcase
  id, problem_id, input text, expected text, ord int

note
  problem_id pk, framework text, notes text, mistakes text, updated_at

review                                                -- one row per revision
  id, problem_id, reviewed_at, outcome,               -- again | hard | good | easy
  ease real, interval_days int, due_at date
```

Notes on the design:

- **Topics and patterns are both many-to-many, and separate.** Your Notion
  data already labels problems with multiple topics (`Arrays, Deque,
  MultiSets`), which the folder tree cannot express — a folder has one
  parent. This is the single biggest thing the DB buys you over
  directories.
- **`pattern` is the more valuable axis.** Your `Form` field already has
  34 distinct values (`BFS, Dijkstras, SSSP`, `0-1 BFS`, `BS on Ans
  (Sweep Based)`, `Game Dp, MniMax DP, Suffix Sum`, `Interval DP`,
  `Atomic Contribution`, `Flood Fill`, `Super Node`). That vocabulary
  describes what you actually know far better than 26 folder names do.
  Make it first-class, not a tag string.
- **`review` is append-only history, not a mutable `due_at` column.**
  Notion's single `Due At` field loses the record of how a problem has
  gone over time. Keeping every review is what allows "problems I keep
  getting wrong" later.
- **`folder_path` is the sync key.** Renaming a folder must be handled as
  a rename (match on `slug` or previous path), not as delete + insert, or
  review history is lost.

---

## 6. Data migration

One-time, and mostly already done:

1. `tools/import` reads `notion_updated/.../Questions*_all.csv` and
   `notion_folder_map.csv` → seeds `problem`, `note`, `topic`,
   `pattern`, and an initial `review` row per problem carrying the
   existing `Due At`. All 87 rows already resolve to a real folder.
2. `tools/sync` walks `topics/*/*/` → creates the remaining 126 problems
   that have no Notion row, with `title` derived from the folder name and
   `status` from whether `expected.txt` is non-empty (142 solved, 71 not).
3. Manually enrich the 126 as they come up in revision. Don't batch it.

Fields carried over: `Topics`, `Form`, `Framework`, `Notes`, `Mistakes`,
`Difficulty`, `Link`, `Source`, `Due At`, `Created time`.

After this runs, Notion is read-only history and the app is the system of
record. Don't dual-write — pick a cutover date.

---

## 7. Phased delivery

Each phase should be independently useful. Do not start the next until
the current one is genuinely in daily use.

### Phase 1 — Read-only browser *(the useful 20%)*
- Sync job + import job; DB populated.
- List view: filter by topic, pattern, difficulty, source, status.
- Problem detail: statement link, your framework/notes/mistakes, the
  solution with syntax highlighting, test cases.
- Full-text search over title, notes, framework, mistakes.

At this point it already beats Notion, because the code and the notes are
finally on one screen.

### Phase 2 — Write path
- Edit notes/framework/mistakes/patterns in the app.
- Create a problem in the app → optionally shells out to `cp_setup.sh`
  to scaffold the folder.
- Notion retired.

### Phase 3 — Spaced repetition *(the reason this exists)*

A **spaced repetition system (SRS)** decides *when* to show you a problem
again. Each review is graded, and the gap until the next one expands when
you recall it and shrinks when you don't — so you revisit each problem
just before you'd have forgotten it, instead of re-solving at random. You
are already doing this by hand: the `Due At` field, set on 84 of your 87
Notion rows, is a manually-maintained SRS.

- **FSRS** (Free Spaced Repetition Scheduler) — a model fitted to real
  review data, now the default in Anki. Preferred over **SM-2** (the
  1987 SuperMemo algorithm Anki used previously): better defaults, no
  hand-tuning of ease factors, actively maintained.
- Daily review queue: shows the problem statement and your own
  "Framework" note, hides the code until you've committed to an answer.
- Grade `again`/`hard`/`good`/`easy`; schedule next review.
- Seed intervals from the existing 84 `Due At` values so you don't
  restart from zero.

### Phase 4 — React Native (Expo)
- Reuses `packages/shared` wholesale; only screens are new.
- Offline-first: cache the review queue locally, sync grades when back
  online. Review on mobile, write code on desktop — don't try to make
  mobile a coding surface.

### Phase 5 — Insights
- Pattern coverage: which of the 34 patterns have few or stale problems.
- Recurring mistakes: cluster the `mistakes` text (only 24 rows have this
  today — Phase 2 should make it easy enough that the number grows).
- Time-since-review heatmap by topic; "weakest area" suggestion.
- Progress over time, solved-by-difficulty.

---

## 8. Feature ideas beyond the roadmap

Parked deliberately — each is a real idea, none should displace phases 1–3.

- **Run tests from the app.** A `/run` endpoint doing what `run.sh` does,
  returning pass/fail per case. Needs a sandbox; treat as a real project.
- **Judge submission links with verdict tracking** — manual entry first;
  scraping judges is fragile and against most ToS.
- **"Similar problems"** via shared patterns, to build recall by analogy.
- **Contest mode**: random N unseen problems, timed.
- **Import from LeetCode submissions** to fix the browser-solved gap that
  produced the 10 missing folders.
- **Diff view across attempts** — how your solution to a pattern changed
  over months.

---

## 9. Risks

| Risk | Mitigation |
|---|---|
| Building the app becomes procrastination from solving problems | Phase 1 must ship in a weekend. If it doesn't, cut scope, not quality. |
| DB and repo drift apart | Sync job runs on a git `post-commit` hook, not manually. |
| Folder renames orphan review history | Match on stable `slug`; log unmatched paths loudly instead of silently inserting. |
| Over-modelling for a single user | Every table above earns its place from data that exists today. Add nothing speculatively. |
| Mobile scope creep | RN is for *reviewing*, not authoring. |

---

## 10. Immediate next steps

1. Scaffold the monorepo (`apps/web`, `apps/api`, `packages/shared`,
   `packages/db`).
2. Write the Prisma schema from §5 and run the first migration.
3. Build `tools/sync` — it is the highest-risk piece and everything else
   depends on its output being right.
4. Build `tools/import` for the Notion CSV.
5. Only then start the React app.

Steps 3 and 4 are worth doing even if the app never gets built: they turn
`notion_folder_map.csv` from a snapshot into something that stays correct.
