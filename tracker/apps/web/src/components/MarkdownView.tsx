/**
 * Renders a note as markdown. react-markdown builds React elements rather
 * than a string of HTML, so there is no dangerouslySetInnerHTML and raw HTML
 * in a note is not executed — it is escaped and shown as text.
 *
 * The project has no typography plugin, so element styling is scoped here
 * with arbitrary variants rather than a `prose` class. Sizes stay close to
 * the surrounding UI: a note is a paragraph in a panel, not a document, so
 * an h1 is a bold line rather than a page title.
 */
import ReactMarkdown from 'react-markdown'
import remarkGfm from 'remark-gfm'

const prose = [
  'text-[13px] leading-relaxed text-[#c9d1d9]',
  // blocks stack with a consistent rhythm, and never lead with a gap
  '[&>*]:mt-2 [&>*:first-child]:mt-0',
  // headings
  '[&_h1]:text-[15px] [&_h1]:font-semibold [&_h1]:text-[#e6edf3]',
  '[&_h2]:text-[14px] [&_h2]:font-semibold [&_h2]:text-[#e6edf3]',
  '[&_h3]:text-[13px] [&_h3]:font-semibold [&_h3]:text-[#e6edf3]',
  '[&_h4]:text-[13px] [&_h4]:font-semibold [&_h4]:text-[#8b949e]',
  // lists
  '[&_ul]:list-disc [&_ul]:pl-5 [&_ol]:list-decimal [&_ol]:pl-5',
  '[&_li]:mt-0.5 [&_li>ul]:mt-0.5 [&_li>ol]:mt-0.5',
  // task lists read better without the bullet beside the box
  '[&_li:has(>input[type=checkbox])]:list-none [&_li:has(>input[type=checkbox])]:-ml-5',
  '[&_input[type=checkbox]]:mr-1.5 [&_input[type=checkbox]]:align-middle',
  // code
  '[&_code]:rounded [&_code]:bg-[#0d1117] [&_code]:px-1 [&_code]:py-0.5',
  '[&_code]:font-mono [&_code]:text-[12px] [&_code]:text-[#79c0ff]',
  '[&_pre]:overflow-x-auto [&_pre]:rounded-md [&_pre]:border [&_pre]:border-[#262d36]',
  '[&_pre]:bg-[#0d1117] [&_pre]:p-3',
  // a fenced block is already framed, so the inner code should not be chipped
  '[&_pre>code]:bg-transparent [&_pre>code]:p-0 [&_pre>code]:text-[#c9d1d9]',
  // the rest
  '[&_a]:text-[#58a6ff] [&_a]:underline',
  '[&_strong]:font-semibold [&_strong]:text-[#e6edf3]',
  '[&_blockquote]:border-l-2 [&_blockquote]:border-[#30363d] [&_blockquote]:pl-3',
  '[&_blockquote]:text-[#8b949e]',
  '[&_hr]:border-[#262d36]',
  // tables scroll rather than pushing the panel wide
  '[&_table]:block [&_table]:w-full [&_table]:overflow-x-auto [&_table]:border-collapse',
  '[&_th]:border [&_th]:border-[#262d36] [&_th]:bg-[#161b22] [&_th]:px-2 [&_th]:py-1',
  '[&_th]:text-left [&_th]:font-semibold [&_th]:text-[#e6edf3]',
  '[&_td]:border [&_td]:border-[#262d36] [&_td]:px-2 [&_td]:py-1',
].join(' ')

export default function MarkdownView({ children }: { children: string }) {
  return (
    <div className={prose}>
      <ReactMarkdown
        remarkPlugins={[remarkGfm]}
        components={{
          // A note may link out to an editorial or a problem statement;
          // those should not replace the app.
          a: ({ node: _node, ...props }) => (
            <a {...props} target="_blank" rel="noreferrer noopener" />
          ),
        }}
      >
        {children}
      </ReactMarkdown>
    </div>
  )
}
