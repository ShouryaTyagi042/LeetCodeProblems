import CodeMirror from '@uiw/react-codemirror'
import { java } from '@codemirror/lang-java'
import { githubDark } from '@uiw/codemirror-theme-github'

export default function CodeView({ code }: { code: string }) {
  return (
    <CodeMirror
      value={code}
      theme={githubDark}
      extensions={[java()]}
      editable={false}
      basicSetup={{ lineNumbers: true, foldGutter: true, highlightActiveLine: false }}
      style={{ fontSize: 12.5 }}
    />
  )
}
