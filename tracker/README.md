# DSA Tracker

Phases 1 and 2 of [`../AlgoZenith/WEBAPP_PLAN.md`](../AlgoZenith/WEBAPP_PLAN.md):
a browsable, searchable, editable index of every problem in `AlgoZenith/topics/`,
joined to the metadata that used to live in Notion.

## Quick start

```bash
cd tracker
pnpm install
pnpm db:push        # create tracker.db from the Prisma schema
pnpm seed           # sync (repo -> DB), then import Notion metadata
pnpm dev            # api on :5174, web on :5173
```

Open http://localhost:5173.

## What it does

**Phase 1 — read**
- Every problem in `topics/` indexed with its solution code and test data.
- Filter by status, difficulty, topic, pattern, source.
- Full-text search over titles, approaches, notes, and mistakes.
- Detail view: solution with Java syntax highlighting, `input.txt`/`expected.txt`,
  judge link, and your own notes side by side.

**Phase 2 — write**
- Edit framework / notes / mistakes in the app.
- Edit difficulty, status, source, judge URL, topics, patterns.
- Create a problem, which shells out to `cp_setup.sh` to scaffold the folder.
- Notion is no longer needed. `notion_updated/` stays as an archive.

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

**`packages/shared` holds the types, API client, and slug helpers.** It has
one consumer today; it will have two when the mobile app lands. Share logic,
not components.

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

## Not built yet

Phase 3 (spaced repetition) is scaffolded in the schema — the `Review` table
exists and is seeded with the 84 `Due At` dates from Notion, so scheduling
will not start from zero. No scheduling logic is implemented.
