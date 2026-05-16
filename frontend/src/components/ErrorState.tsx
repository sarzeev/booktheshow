interface ErrorStateProps {
  message: string
  onRetry?: () => void
}

export function ErrorState({ message, onRetry }: ErrorStateProps): React.JSX.Element {
  return (
    <div className="state-panel error-panel" role="alert">
      <strong>{message}</strong>
      {onRetry ? (
        <button className="button ghost" type="button" onClick={onRetry}>
          Retry
        </button>
      ) : null}
    </div>
  )
}
