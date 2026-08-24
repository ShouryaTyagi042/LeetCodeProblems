import type { ReactNode } from 'react'

export const cx = (...c: (string | false | undefined)[]) => c.filter(Boolean).join(' ')

export function Chip({
  children, tone = 'default', onClick, title,
}: {
  children: ReactNode
  tone?: 'default' | 'accent' | 'green' | 'amber' | 'red'
  onClick?: () => void
  title?: string
}) {
  const tones = {
    default: 'bg-[#21262d] text-[#8b949e] border-[#30363d]',
    accent: 'bg-[#0d2d5e] text-[#79c0ff] border-[#1f4e8c]',
    green: 'bg-[#0f2f1a] text-[#56d364] border-[#1f6f34]',
    amber: 'bg-[#3a2c05] text-[#e3b341] border-[#7d5e12]',
    red: 'bg-[#3d1418] text-[#f85149] border-[#7d2429]',
  }
  return (
    <span
      title={title}
      onClick={onClick}
      className={cx(
        'inline-flex items-center rounded-full border px-2 py-0.5 text-[11px] leading-4 whitespace-nowrap',
        tones[tone],
        onClick && 'cursor-pointer hover:brightness-125',
      )}
    >
      {children}
    </span>
  )
}

export function difficultyTone(d?: string | null) {
  return d === 'Easy' ? 'green' : d === 'Medium' ? 'amber' : d === 'Hard' ? 'red' : 'default'
}

export function Field({ label, children }: { label: string; children: ReactNode }) {
  return (
    <label className="block">
      <div className="mb-1 text-[11px] font-medium uppercase tracking-wide text-[#8b949e]">
        {label}
      </div>
      {children}
    </label>
  )
}

export const inputCls =
  'w-full rounded-md border border-[#30363d] bg-[#0d1117] px-3 py-2 text-sm ' +
  'text-[#e6edf3] outline-none placeholder:text-[#6e7681] focus:border-[#58a6ff]'

export function Button({
  children, onClick, variant = 'default', disabled, type = 'button',
}: {
  children: ReactNode
  onClick?: () => void
  variant?: 'default' | 'primary' | 'ghost'
  disabled?: boolean
  type?: 'button' | 'submit'
}) {
  const v = {
    default: 'bg-[#21262d] border-[#30363d] hover:bg-[#30363d]',
    primary: 'bg-[#1f6feb] border-[#1f6feb] text-white hover:bg-[#388bfd]',
    ghost: 'bg-transparent border-transparent hover:bg-[#21262d]',
  }
  return (
    <button
      type={type}
      disabled={disabled}
      onClick={onClick}
      className={cx(
        'rounded-md border px-3 py-1.5 text-sm font-medium transition',
        v[variant],
        disabled && 'cursor-not-allowed opacity-50',
      )}
    >
      {children}
    </button>
  )
}

export function Empty({ children }: { children: ReactNode }) {
  return <div className="py-16 text-center text-sm text-[#6e7681]">{children}</div>
}
