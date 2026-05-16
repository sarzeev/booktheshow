import { Plus } from 'lucide-react'
import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { getApiErrorMessage } from '../../api/client'
import type { EventResponse, Page } from '../../api/types'
import { ErrorState } from '../../components/ErrorState'
import { EventCard } from '../../components/EventCard'
import { LoadingState } from '../../components/LoadingState'
import { Pagination } from '../../components/Pagination'
import { eventService } from '../../services/eventService'

export function EventsManagementPage(): React.JSX.Element {
  const [page, setPage] = useState(0)
  const [eventsPage, setEventsPage] = useState<Page<EventResponse> | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    setLoading(true)
    setError('')
    eventService
      .listOrganizerEvents(page, 8)
      .then(setEventsPage)
      .catch((requestError) => setError(getApiErrorMessage(requestError)))
      .finally(() => setLoading(false))
  }, [page])

  return (
    <section className="dashboard-page">
      <div className="section-header">
        <div>
          <span className="eyebrow">Event management</span>
          <h1>Your events</h1>
        </div>
        <Link className="button primary" to="/organizer/events/new">
          <Plus size={18} aria-hidden="true" />
          New event
        </Link>
      </div>
      {loading ? <LoadingState label="Loading organizer events" /> : null}
      {error ? <ErrorState message={error} /> : null}
      <div className="event-grid compact-grid">
        {eventsPage?.content.map((event) => (
          <EventCard key={event.id} event={event} mode="management" />
        ))}
      </div>
      <Pagination page={page} totalPages={eventsPage?.totalPages ?? 0} onPageChange={setPage} />
    </section>
  )
}
