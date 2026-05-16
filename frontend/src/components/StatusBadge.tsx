import { statusLabel } from '../utils/format'

interface StatusBadgeProps {
  status: string
}

export function StatusBadge({ status }: StatusBadgeProps): React.JSX.Element {
  return <span className={`status-badge status-${status.toLowerCase()}`}>{statusLabel(status)}</span>
}
