# Changelog

Work on the `repo-reorg` branch, which reorganises the problem archive and
adds a tracker app on top of it. Newest first.

The repository holds two things: `AlgoZenith/` — the problems themselves,
which remain plain folders of Java that can be pasted into an online judge —
and `tracker/`, an app that indexes them. Nothing in the tracker changes how
a solution is written or run.

---

## Unreleased

### One ladder for everything

Scheduling is now a single fixed ladder — **3 days → 1 week → 2 weeks →
1 month**, then monthly — used for every problem.

- Replaced FSRS (`ts-fsrs`, now removed) with the ladder. FSRS models recall
  of one specific item and produces open-ended intervals; on a 94-day
  overdue card it was scheduling five months out, which is not useful when
  the horizon is an interview.
- Two outcomes instead of four grades: **Solid** climbs a rung, **Shaky**
  drops back to 3 days.
- The stored `step` is the rung to apply *next*, not the one just used, so a
  problem's first review schedules 3 days rather than skipping to a week.
- Notion's `Due At` still seeds the ladder — the rung is inferred from how
  long the previous interval looks to have been, so a problem already on a
  monthly cadence is not knocked back to 3 days.

### Topics are a filter, not a schedule

`/topics` lists every topic with how many of its problems are due, and links
into a review queue filtered to that topic. Topics carry no schedule of
their own: whether a technique needs work is answered by how many of its
problems are due, not by a separate judgement about the topic.

### Dropped solved/unsolved

Everything committed here is solved by definition, so the field carried no
information. It is gone from the schema, filters, facets, stats, sort fields
and the editor.

The consequence that mattered: review cards were only created for `solved`
problems, which excluded 71 — and 49 of those had substantial solution code,
marked unsolved only because `expected.txt` was empty. **Review cards went
from 159 to 213.**

### Sorting

- Multi-key sort with a per-key direction, reorderable (key order is sort
  precedence). URL format `?sort=difficulty:desc,created:asc`.
- New **Created** field; the list defaults to newest first.
- Fixed: sorting by difficulty ordered the raw string, giving Easy → **Hard**
  → Medium. Now sorted on a numeric rank.
- Fixed: problems with no Notion row took `now()` as their creation date, so
  126 of 213 looked created the day the database was built. They now take a
  `2026-01-01` sentinel.
- Unset values sort last in both directions rather than leading an ascending
  list.

### Search and forms

- The search box suggests matching topics, patterns and sources, grouped and
  counted; picking one applies the filter. Active filters show as removable
  chips.
- Topics and patterns are typeahead tag inputs rather than comma-separated
  text, so a typo no longer forks a tag. Creating a new one is still
  possible but deliberate.

### Running it

`tracker/start.sh` starts both servers with one command — installing
dependencies, creating and seeding the database on first run, and freeing
both ports first.

### Fixes

- **Pagination did nothing.** `patch()` deleted the page param immediately
  after setting it, so Next and Prev never changed the URL.
- **Vite was squatting on the API's port.** When 5173 was busy Vite fell
  forward to 5174, binding `::1` while Fastify binds `127.0.0.1`, so
  `localhost:5174` silently hit the wrong server. `strictPort` now makes it
  fail loudly.
- **Forecast buckets were labelled a day early** — local midnight formatted
  via `toISOString()` lands on the previous UTC day at +5:30.
- **Slugs contain a slash**, and the API only matched the `%2F`-encoded
  form, which nginx rejects. Two-segment paths now work.
- **`tools/` was never typechecked**, which let a broken card-seeding block
  through: Prisma ignores `undefined` fields, so it silently wrote the due
  date and skipped the rung. It has a `tsconfig.json` now.

---

## 2026-08-24 — Tracker app

A pnpm monorepo under `tracker/`: Fastify API, React + Vite web app, Prisma
schema, and two data jobs. SQLite rather than the Postgres named in the
plan — no daemon, and the file backs up with the repo.

- **Browse** — 213 problems indexed with their code and test data. Filter by
  topic, pattern, difficulty, source. Full-text search across titles and
  your own notes. Detail view puts the syntax-highlighted Java, the test
  data and your notes on one screen.
- **Edit** — notes, framework, mistakes and metadata, in the app. Creating a
  problem shells out to `cp_setup.sh`, so the scaffold stays exactly what
  the script produces.
- **`tools/sync`** walks `topics/*/*/` and upserts by folder path, so
  committing a solution is what registers it. Folder basenames are unique
  across all 213 problems, so a folder that changes topic is matched by
  basename and updated in place — notes and review history survive a
  `git mv`. Rows whose folder has vanished are reported, never silently
  deleted.
- **`tools/import`** joins the Notion CSV via `notion_folder_map.csv`.

## 2026-08-22 — Notion reconciled

- `notion_folder_map.csv`: the join key between the Notion database and the
  repo. All 87 Notion rows resolve to a folder that exists on disk; 43
  needed hand-verification, since typos and renames existed on both sides
  and did not correspond (`Labryinth`/`Labyrinth`, `Shortest Route I`/
  `ShortestPathI`).
- Unwound the `STL/` merge. The Notion export showed problems are labelled
  by technique, not by language feature, so all 14 folders were
  redistributed.
- Scaffolded the 10 problems that existed in Notion but had no folder — all
  LeetCode-style titles solved in the browser and never saved locally.
- `WEBAPP_PLAN.md`: the plan for the app.

## 2026-08-21 — Repository reorganised

- **Untracked 838 build artifacts** (683 `.class`, 150 `output.txt`,
  5 `.DS_Store`) and added a `.gitignore`. Tracked bytes fell from ~1.86 MB
  to 1.06 MB. All files stayed on disk.
- **Consolidated 202 problems into `AlgoZenith/topics/`.** The tree
  previously had competing source-based (`AtCoder/`, `CodeForces/`, `CSES/`)
  and topic-based (`CohortAssignments/`, `Mathematics/`) hierarchies, so
  `Graphs` lived in three places and `BinarySearch` in two. Every move was a
  `git mv` — the blob-hash multiset was identical before and after, so no
  file content changed and `git log --follow` still works.
- Fixed naming along the way: `Comibnatorics` → `Combinatorics`,
  `Greedy&SweepLine` → `GreedyAndSweepLine` (the `&` broke unquoted paths),
  `fundamentals` → `Fundamentals`.
- `MIGRATION.md` records where every problem came from, since the folder
  path no longer encodes it.
