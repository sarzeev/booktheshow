import { QrCode, ShieldCheck } from 'lucide-react'
import { Link } from 'react-router-dom'
import { MetricCard } from '../../components/MetricCard'

export function StaffDashboardPage(): React.JSX.Element {
  return (
    <section className="dashboard-page">
      <div className="section-header">
        <div>
          <span className="eyebrow">Staff dashboard</span>
          <h1>Validate attendee entry</h1>
        </div>
        <Link className="button primary" to="/staff/events">
          Select event
        </Link>
      </div>
      <div className="metric-grid">
        <MetricCard label="QR scan" value="Camera ready" detail="Validate generated ticket QR codes" />
        <MetricCard label="Manual fallback" value="Supported" detail="Enter ticket IDs when scanning is not possible" />
      </div>
      <div className="quick-actions">
        <Link to="/staff/events">
          <QrCode aria-hidden="true" />
          Open scanner
        </Link>
        <Link to="/staff/events">
          <ShieldCheck aria-hidden="true" />
          Validation history
        </Link>
      </div>
    </section>
  )
}
