/**
 * Markdown rendering costs ~50 kB gzipped and is only reached on the notes
 * tab and behind the reveal on a review card — never on the list page that
 * loads first. Split out for the same reason CodeView is.
 */
import { Suspense, lazy } from 'react'

const MarkdownView = lazy(() => import('./MarkdownView'))

export default function Markdown({ children }: { children: string }) {
  return (
    <Suspense fallback={
      <div className="whitespace-pre-wrap text-[13px] leading-relaxed text-[#6e7681]">
        {children}
      </div>
    }>
      <MarkdownView>{children}</MarkdownView>
    </Suspense>
  )
}
