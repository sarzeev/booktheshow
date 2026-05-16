import { TicketCheck, Tickets } from 'lucide-react'
import { Link } from 'react-router-dom'
import { MetricCard } from '../../components/MetricCard'

export function AttendeeDashboardPage(): React.JSX.Element {
  return (
    <section className="dashboard-page">
      <div className="section-header">
        <div>
          <span className="eyebrow">Attendee dashboard</span>
          <h1>Your event wallet</h1>
        </div>
        <Link className="button primary" to="/events">
          Browse events
        </Link>
      </div>
      <div className="metric-grid">
        <MetricCard label="Ticket access" value="QR ready" detail="Open a ticket to display its QR code" />
        <MetricCard label="Entry support" value="Manual fallback" detail="Ticket IDs work when scanning is unavailable" />
      </div>
      <div className="quick-actions">
        <Link to="/attendee/tickets">
          <Tickets aria-hidden="true" />
          Purchased tickets
        </Link>
        <Link to="/events">
          <TicketCheck aria-hidden="true" />
          Find another event
        </Link>
      </div>
    </section>
  )
}
