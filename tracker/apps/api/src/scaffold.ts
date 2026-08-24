import { execFileSync } from 'node:child_process'
import fs from 'node:fs'
import path from 'node:path'
import { ALGO_ROOT, TOPICS_DIR } from '@tracker/tools/paths'

/**
 * Create a problem folder using the repo's own cp_setup.sh, so the
 * scaffold stays whatever the script says it is. Never reimplement the
 * template here — Main.java must stay a single self-contained file that
 * can be pasted into an online judge.
 */
export function scaffoldFolder(topicDir: string, folderName: string): string {
  if (!/^[A-Za-z0-9]+$/.test(topicDir) || !/^[A-Za-z0-9]+$/.test(folderName)) {
    throw new Error('topic and folder names must be alphanumeric')
  }
  const script = path.join(ALGO_ROOT, 'cp_setup.sh')
  if (!fs.existsSync(script)) throw new Error(`cp_setup.sh not found at ${script}`)

  const dest = path.join(TOPICS_DIR, topicDir)
  fs.mkdirSync(dest, { recursive: true })
  const target = path.join(dest, folderName)
  if (fs.existsSync(target)) throw new Error(`${target} already exists`)

  execFileSync('bash', [script, folderName], { cwd: dest, stdio: 'pipe' })
  return path.relative(ALGO_ROOT, target)
}
