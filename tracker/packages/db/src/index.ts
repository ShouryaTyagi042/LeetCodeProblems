import path from 'node:path'
import { fileURLToPath } from 'node:url'
import { PrismaClient } from '@prisma/client'

export * from '@prisma/client'

/**
 * Resolve the SQLite file to tracker/tracker.db regardless of which
 * workspace the process was started from. Set DATABASE_URL explicitly to
 * override (e.g. pointing at Postgres later).
 */
const here = path.dirname(fileURLToPath(import.meta.url))
export const DB_PATH = path.resolve(here, '../../../tracker.db')

if (!process.env.DATABASE_URL) {
  process.env.DATABASE_URL = `file:${DB_PATH}`
}

let _prisma: PrismaClient | undefined
export function getPrisma(): PrismaClient {
  if (!_prisma) _prisma = new PrismaClient()
  return _prisma
}
