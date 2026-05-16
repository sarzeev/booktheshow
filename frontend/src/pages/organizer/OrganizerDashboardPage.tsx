import { CalendarPlus, LineChart, Settings } from 'lucide-react'
import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { getApiErrorMessage } from '../../api/client'
import type { EventResponse } from '../../api/types'
import { ErrorState } from '../../components/ErrorState'
import { EventCard } from '../../components/EventCard'
import { LoadingState } from '../../components/LoadingState'
import { MetricCard } from '../../components/MetricCard'
import { eventService } from '../../services/eventService'

export function OrganizerDashboardPage(): React.JSX.Element {
  const [events, setEvents] = useState<EventResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    eventService
      .listOrganizerEvents(0, 3)
      .then((page) => setEvents(page.content))
      .catch((requestError) => setError(getApiErrorMessage(requestError)))
      .finally(() => setLoading(false))
  }, [])

  return (
    <section className="dashboard-page">
      <div className="section-header">
        <div>
          <span className="eyebrow">Organizer dashboard</span>
          <h1>Manage events and revenue</h1>
        </div>
        <Link className="button primary" to="/organizer/events/new">
          Create event
        </Link>
      </div>
      <div className="metric-grid">
        <MetricCard label="Active workspace" value="Organizer" detail="Manage only events you own" />
        <MetricCard label="Inventory" value="Live" detail="Remaining tickets update after each purchase" />
        <MetricCard label="Validation" value="Tracked" detail="Staff scans feed attendance reports" />
      </div>
      <div className="quick-actions">
        <Link to="/organizer/events/new">
          <CalendarPlus aria-hidden="true" />
          Create event
        </Link>
        <Link to="/organizer/events">
          <Settings aria-hidden="true" />
          Manage events
        </Link>
        <Link to="/organizer/events">
          <LineChart aria-hidden="true" />
          View all events
        </Link>
      </div>
      {loading ? <LoadingState label="Loading events" /> : null}
      {error ? <ErrorState message={error} /> : null}
      <div className="event-grid compact-grid">{events.map((event) => <EventCard key={event.id} event={event} mode="management" />)}</div>
    </section>
  )
}
