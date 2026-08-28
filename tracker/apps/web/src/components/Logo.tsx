/**
 * Four marks at widening intervals — the review ladder itself
 * (3 days → 1 week → 2 weeks → 1 month), the one mechanic this app is
 * built around. The gaps grow the way the intervals do, and the first
 * mark is full height because it is the one that is due.
 *
 * Deliberately not a node graph: that is the stock symbol for anything
 * algorithm-shaped, and three circles joined by hairlines collapse into a
 * smudge at favicon size. Opacities bottom out at 0.48 for the same
 * reason — below roughly that, a thin bar disappears into the background
 * at 16px. Kept in step with public/favicon.svg.
 */
export default function Logo({ size = 22 }: { size?: number }) {
  return (
    <svg
      width={size}
      height={size}
      viewBox="0 0 24 24"
      fill="none"
      role="img"
      aria-label="DSA Tracker"
    >
      {/* due now */}
      <rect x="1.5" y="4.5" width="3" height="15" rx="1.5" fill="#58a6ff" />
      {/* scheduled, at widening intervals */}
      <rect x="7" y="8" width="3" height="8" rx="1.5" fill="#58a6ff" opacity="0.78" />
      <rect x="13" y="8" width="3" height="8" rx="1.5" fill="#58a6ff" opacity="0.62" />
      <rect x="19.5" y="8" width="3" height="8" rx="1.5" fill="#58a6ff" opacity="0.48" />
    </svg>
  )
}
