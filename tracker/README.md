# DSA Tracker

A browsable, searchable, editable index of every problem in `AlgoZenith/topics/`,
joined to the metadata that used to live in Notion, with spaced-repetition
review scheduling on top.

## Quick start

```bash
cd tracker
./start.sh
```

Open http://localhost:5173. That is the whole setup — the script installs
dependencies, creates and seeds the database on first run, frees the two
ports if a previous run is still holding them, and starts both servers.

```bash
./start.sh --seed    # re-sync the repo and re-import Notion first
./start.sh --fresh   # rebuild the database from scratch, then seed
```

The underlying commands (`pnpm db:push`, `pnpm seed`, `pnpm dev`) still work
if you would rather drive them individually.

## What it does

**Phase 1 — read**
- Every problem in `topics/` indexed with its solution code and test data.
- Filter by status, difficulty, topic, pattern, source.
- Full-text search over titles, approaches, notes, and mistakes.
- Detail view: solution with Java syntax highlighting, `input.txt`/`expected.txt`,
  judge link, and your own notes side by side.

**Phase 2 — write**
- Edit framework / notes / mistakes in the app.
- Edit difficulty, status, source, judge URL, topics, patterns — backed by
  typeahead over the existing vocabulary, so a typo does not fork a tag.
- Create a problem, which shells out to `cp_setup.sh` to scaffold the folder.
- Notion is no longer needed. `notion_updated/` stays as an archive.

**Phase 3 — spaced repetition**
- One fixed ladder for every problem: 3 days → 1 week → 2 weeks → 1 month,
  then monthly. Seeded from the 84 Notion `Due At` dates so nothing restarts
  from zero.
- `/review` shows one due problem at a time: title, topics and the judge
  link as the prompt; patterns, framework, mistakes and code stay hidden
  until you reveal. Mark it **Solid** to climb a rung or **Shaky** to drop
  back to 3 days — keys `space` then `1`/`2`.
- `/topics` lists every topic with how many of its problems are due, and
  links into a review queue filtered to that topic. Topics are a filter on
  the queue, not something scheduled in their own right.
- Due badge in the nav, backlog and retention stats, and a forecast of the
  next two weeks.
- Every problem in the tree gets a card. There is no solved/unsolved flag —
  anything committed here is solved by definition.

## Design notes

**The repo is the source of truth for code.** The database indexes it.
`tools/sync` walks `topics/*/*/` and upserts by folder path — solving a
problem and committing it is the act of registering it. Nothing in the app
requires restructuring a solution file: `Main.java` must stay a single
self-contained file you can paste into a judge's IDE.

**Moves are handled as moves.** Problem folder basenames are unique across
all 213 problems, so a folder that changes topic is matched by basename and
updated in place. Its notes and review history survive a `git mv`. Rows in
the DB whose folder has vanished are reported, never silently deleted.

**SQLite, not Postgres.** The plan specified Postgres; this uses SQLite
because it needs no daemon and the file backs up with the repo. Nothing in
the schema is SQLite-specific — switching means changing `provider` in
`packages/db/prisma/schema.prisma` and setting `DATABASE_URL`. Do that when
the React Native app needs a hosted DB.

**`packages/shared` holds the types, API client, slug helpers, and the FSRS
wrapper.** It has one consumer today; it will have two when the mobile app
lands. Share logic, not components. Scheduling lives here specifically
because both sides need it — the server computes the authoritative next
state when you grade, and the client previews "good -> 5mo" on the buttons
before you commit.

**Cards are split from reviews** the way Anki splits cards from its revlog.
`Card` is the single mutable "where does this stand now" row, which makes
"what is due?" one indexed query; `Review` is the append-only log, so the
history can rebuild or audit any schedule. Re-running the import never
touches a card that has a real graded review — the Notion date only ever
seeds something never graded here.

## Layout

```
apps/api        Fastify REST API (:5174)
apps/web        React + Vite (:5173)
packages/db     Prisma schema + client
packages/shared types, API client — reused by React Native later
tools/sync      repo -> DB indexer
tools/import    one-time Notion CSV import
```

## Commands

| Command | What |
|---|---|
| `pnpm dev` | api + web together |
| `pnpm sync` | re-index the repo into the DB |
| `pnpm import:notion` | re-run the Notion CSV import |
| `pnpm seed` | sync then import |
| `pnpm db:studio` | browse the DB in Prisma Studio |
| `bash tools/install-hook.sh` | run sync automatically on every commit |

## Deviations from the plan

- **SQLite instead of Postgres**, as above.
- **The review prompt hides patterns.** The plan said to show the Framework
  note alongside the problem. In practice the pattern (`0-1 BFS`,
  `BS on Ans (Sweep Based)`) *is* the answer for a DSA problem, and so is
  the framework — showing either upfront defeats the recall. Topics stay
  visible, since you would know it was a graph problem from reading the
  statement anyway.
- **A fixed ladder rather than a fitted model.** FSRS was implemented first
  and then removed: it models recall of one specific item and produces
  open-ended intervals — on a 94-day overdue card it scheduled five months
  out. A short fixed ladder suits interview prep better, and gives problems
  and topics one shared vocabulary.

## Not built yet

Phase 4 (React Native) and Phase 5 (insights). `packages/shared` is already
the seam for Phase 4: types, API client and scheduling are all there and
free of DOM or Node dependencies.
