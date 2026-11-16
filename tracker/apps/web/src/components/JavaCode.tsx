/**
 * Static Java highlighting for fenced blocks in notes. A note can hold
 * several snippets, and a read-only CodeMirror per fence would be a full
 * editor each time — so this runs the same Lezer parser CodeView uses and
 * emits plain spans, coloured from the same githubDark style so a snippet in
 * a note matches the solution view.
 *
 * Colours are applied inline rather than through CodeMirror's generated
 * classes, which only exist in the document once an editor has mounted them.
 */
import type { CSSProperties, ReactNode } from 'react'
import { highlightCode, tagHighlighter } from '@lezer/highlight'
import { parser } from '@lezer/java'
import { githubDarkStyle } from '@uiw/codemirror-theme-github'

// Each style entry becomes class `h<index>`, which maps back to its CSS.
const styles = githubDarkStyle ?? []
const highlighter = tagHighlighter(styles.map((s, i) => ({ tag: s.tag, class: `h${i}` })))
const css: CSSProperties[] = styles.map(({ tag: _tag, ...rest }) => rest as CSSProperties)

function styleFor(classes: string): CSSProperties {
  return Object.assign({}, ...classes.split(' ').map((c) => css[Number(c.slice(1))]))
}

export default function JavaCode({ code }: { code: string }) {
  const out: ReactNode[] = []
  highlightCode(
    code,
    parser.parse(code),
    highlighter,
    (text, classes) => {
      out.push(classes
        ? <span key={out.length} style={styleFor(classes)}>{text}</span>
        : text)
    },
    () => out.push('\n'),
  )
  return <code className="language-java">{out}</code>
}
