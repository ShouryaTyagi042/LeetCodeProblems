import { fileURLToPath } from 'node:url'
import path from 'node:path'
import fs from 'node:fs'

const here = path.dirname(fileURLToPath(import.meta.url))

/** Repo root — tracker/ lives directly inside it. */
export const REPO_ROOT = path.resolve(here, '../../..')

/** The AlgoZenith tree holding topics/, cp_setup.sh, run.sh. */
export const ALGO_ROOT =
  process.env.ALGOZENITH_ROOT ?? path.join(REPO_ROOT, 'AlgoZenith')

export const TOPICS_DIR = path.join(ALGO_ROOT, 'topics')

export function assertLayout(): void {
  if (!fs.existsSync(TOPICS_DIR)) {
    throw new Error(
      `Cannot find ${TOPICS_DIR}. Set ALGOZENITH_ROOT to the AlgoZenith directory.`,
    )
  }
}

/** Topic folder name -> display name, where humanize() gets it wrong. */
export const TOPIC_DISPLAY: Record<string, string> = {
  '2DArrays': '2D Arrays',
  '2DPrefixSum': '2D Prefix Sum',
  GreedyAndSweepLine: 'Greedy & Sweep Line',
  DequeAndOrderedSet: 'Deque & Ordered Set',
  StackAndQueues: 'Stack & Queues',
  VariablesAndOperators: 'Variables & Operators',
  IfElseConditions: 'If-Else Conditions',
  MultiSet: 'MultiSet',
  STL: 'STL',
}
