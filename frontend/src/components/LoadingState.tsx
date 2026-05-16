interface LoadingStateProps {
  label?: string
}

export function LoadingState({ label = 'Loading' }: LoadingStateProps): React.JSX.Element {
  return (
    <div className="state-panel" role="status" aria-live="polite">
      <span className="spinner" aria-hidden="true" />
      <span>{label}</span>
    </div>
  )
}
