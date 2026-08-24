/** PascalCase / arbitrary text -> kebab-case slug. Used on both sides. */
export function slugify(s: string): string {
  return s
    .replace(/([a-z0-9])([A-Z])/g, '$1 $2')
    .replace(/[^A-Za-z0-9]+/g, ' ')
    .trim()
    .toLowerCase()
    .replace(/\s+/g, '-')
}

/** 'AllPairsShortestPath' -> 'All Pairs Shortest Path' */
export function humanize(folderName: string): string {
  return folderName
    .replace(/([a-z0-9])([A-Z])/g, '$1 $2')
    .replace(/([A-Z]+)([A-Z][a-z])/g, '$1 $2')
    .replace(/[_-]+/g, ' ')
    .trim()
}
