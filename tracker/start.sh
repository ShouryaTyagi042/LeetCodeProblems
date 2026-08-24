#!/bin/bash
#
# Start the DSA tracker: API on :5174, web on :5173.
#
#   ./start.sh          start both
#   ./start.sh --seed   re-sync the repo and re-import Notion first
#   ./start.sh --fresh  rebuild the database from scratch, then seed
#
# Safe to run repeatedly: it installs what is missing, creates the database
# if absent, and clears anything already holding the two ports.
set -euo pipefail

cd "$(dirname "$0")"
API_PORT=5174
WEB_PORT=5173

say()  { printf '\033[36m▸\033[0m %s\n' "$*"; }
warn() { printf '\033[33m!\033[0m %s\n' "$*"; }
die()  { printf '\033[31m✖\033[0m %s\n' "$*" >&2; exit 1; }

# ---- prerequisites ----
command -v node >/dev/null || die "node is not installed"
command -v pnpm >/dev/null || die "pnpm is not installed (npm i -g pnpm)"

FRESH=0; SEED=0
for arg in "$@"; do
  case "$arg" in
    --fresh) FRESH=1; SEED=1 ;;
    --seed)  SEED=1 ;;
    -h|--help) sed -n '2,10p' "$0" | sed 's/^# \{0,1\}//'; exit 0 ;;
    *) die "unknown option: $arg" ;;
  esac
done

# ---- free the ports ----
# Vite falls forward onto the next free port when 5173 is busy, which is the
# API's port; it binds ::1 while Fastify binds 127.0.0.1, so requests to
# localhost:5174 silently hit the wrong server. strictPort stops Vite doing
# that, and clearing both ports here stops a stale run causing it.
for port in $API_PORT $WEB_PORT; do
  pids=$(lsof -ti tcp:"$port" 2>/dev/null || true)
  if [ -n "$pids" ]; then
    warn "port $port in use by pid(s) $(echo "$pids" | tr '\n' ' ')— stopping"
    # shellcheck disable=SC2086
    kill $pids 2>/dev/null || true
    sleep 1
    pids=$(lsof -ti tcp:"$port" 2>/dev/null || true)
    # shellcheck disable=SC2086
    [ -n "$pids" ] && kill -9 $pids 2>/dev/null || true
  fi
done

# ---- dependencies ----
if [ ! -d node_modules ] || [ package.json -nt node_modules ]; then
  say "installing dependencies"
  pnpm install
fi

# ---- database ----
if [ "$FRESH" = 1 ]; then
  say "rebuilding the database from scratch"
  rm -f tracker.db
fi
if [ ! -f tracker.db ]; then
  say "creating tracker.db"
  pnpm -s db:push
  SEED=1
fi
if [ "$SEED" = 1 ]; then
  say "indexing the repo and importing Notion metadata"
  pnpm -s seed
fi

count=$(sqlite3 tracker.db "SELECT COUNT(*) FROM Problem;" 2>/dev/null || echo 0)
say "$count problems indexed"

# ---- run ----
say "api  http://localhost:$API_PORT"
say "web  http://localhost:$WEB_PORT"
echo
exec pnpm -s dev
