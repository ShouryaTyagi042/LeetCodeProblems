#!/bin/bash
# Install a git post-commit hook that re-syncs the tracker DB.
#
# Not installed automatically -- it changes what every `git commit` does,
# so it is opt-in. Run this script to enable it, delete
# .git/hooks/post-commit to disable.
set -euo pipefail

REPO_ROOT="$(git -C "$(dirname "$0")" rev-parse --show-toplevel)"
HOOK="$REPO_ROOT/.git/hooks/post-commit"

if [ -e "$HOOK" ] && ! grep -q 'dsa-tracker sync' "$HOOK"; then
  echo "A post-commit hook already exists at $HOOK."
  echo "Add this line to it manually instead:"
  echo "  (cd \"$REPO_ROOT/tracker\" && pnpm -s sync >/dev/null 2>&1 &)"
  exit 1
fi

cat > "$HOOK" << 'HOOK_EOF'
#!/bin/bash
# dsa-tracker sync — keeps the tracker DB in step with the repo.
# Runs detached so it never slows down a commit.
REPO_ROOT="$(git rev-parse --show-toplevel)"
(cd "$REPO_ROOT/tracker" && pnpm -s sync >/dev/null 2>&1 &)
HOOK_EOF

chmod +x "$HOOK"
echo "Installed $HOOK"
echo "Every commit now re-indexes the repo into tracker/tracker.db."
