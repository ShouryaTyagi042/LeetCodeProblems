/**
 * Inline stroke icons, in the same hand-rolled spirit as Logo.tsx — an icon
 * package would be a dependency and a bundle chunk for the dozen glyphs this
 * app actually uses.
 *
 * All of them are 24x24, stroked in currentColor, so they take the colour and
 * hover state of whatever they sit in and line up with the text around them.
 * They are decorative: every icon-only control carries its own aria-label, so
 * the SVGs are aria-hidden and never announced twice.
 */
import type { ReactNode } from 'react'

interface IconProps {
  size?: number
  className?: string
}

function Svg({ size = 14, className, children }: IconProps & { children: ReactNode }) {
  return (
    <svg
      width={size}
      height={size}
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth={2}
      strokeLinecap="round"
      strokeLinejoin="round"
      aria-hidden="true"
      focusable="false"
      className={className}
    >
      {children}
    </svg>
  )
}

export const ArrowLeft = (p: IconProps) => (
  <Svg {...p}><path d="M19 12H5" /><path d="M12 19l-7-7 7-7" /></Svg>
)

export const ChevronLeft = (p: IconProps) => (
  <Svg {...p}><path d="M15 18l-6-6 6-6" /></Svg>
)

export const ChevronRight = (p: IconProps) => (
  <Svg {...p}><path d="M9 18l6-6-6-6" /></Svg>
)

export const ChevronDown = (p: IconProps) => (
  <Svg {...p}><path d="M6 9l6 6 6-6" /></Svg>
)

export const ArrowUp = (p: IconProps) => (
  <Svg {...p}><path d="M12 19V5" /><path d="M5 12l7-7 7 7" /></Svg>
)

export const ArrowDown = (p: IconProps) => (
  <Svg {...p}><path d="M12 5v14" /><path d="M19 12l-7 7-7-7" /></Svg>
)

export const X = (p: IconProps) => (
  <Svg {...p}><path d="M18 6L6 18" /><path d="M6 6l12 12" /></Svg>
)

export const ExternalLink = (p: IconProps) => (
  <Svg {...p}>
    <path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6" />
    <path d="M15 3h6v6" />
    <path d="M10 14L21 3" />
  </Svg>
)

/** Marks a problem that has notes written up. */
export const Pencil = (p: IconProps) => (
  <Svg {...p}>
    <path d="M12 20h9" />
    <path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4 12.5-12.5z" />
  </Svg>
)

/** Marks the optional topic/difficulty hint on the review card. */
export const Lightbulb = (p: IconProps) => (
  <Svg {...p}>
    <path d="M9 18h6" />
    <path d="M10 22h4" />
    <path d="M15.09 14c.18-.98.65-1.74 1.41-2.5A4.65 4.65 0 0 0 18 8 6 6 0 0 0 6 8c0 1 .23 2.23 1.5 3.5A4.61 4.61 0 0 1 8.91 14" />
  </Svg>
)
