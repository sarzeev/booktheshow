import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { getApiErrorMessage } from '../../api/client'
import type { EventResponse, Page } from '../../api/types'
import { ErrorState } from '../../components/ErrorState'
import { LoadingState } from '../../components/LoadingState'
import { Pagination } from '../../components/Pagination'
import { eventService } from '../../services/eventService'
import { formatDateTime } from '../../utils/format'

export function EventSelectionPage(): React.JSX.Element {
  const [page, setPage] = useState(0)
  const [eventsPage, setEventsPage] = useState<Page<EventResponse> | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    setLoading(true)
    setError('')
    eventService
      .listPublishedEvents('', page, 8)
      .then(setEventsPage)
      .catch((requestError) => setError(getApiErrorMessage(requestError)))
      .finally(() => setLoading(false))
  }, [page])

  return (
    <section className="dashboard-page">
      <div className="section-header">
        <div>
          <span className="eyebrow">Validation setup</span>
          <h1>Select an event</h1>
        </div>
      </div>
      {loading ? <LoadingState label="Loading events" /> : null}
      {error ? <ErrorState message={error} /> : null}
      <div className="data-table">
        {eventsPage?.content.map((event) => (
          <Link className="table-row" key={event.id} to={`/staff/events/${event.id}/scan`}>
            <span>{event.name}</span>
            <span>{event.venue}</span>
            <span>{formatDateTime(event.startDateTime)}</span>
          </Link>
        ))}
      </div>
      <Pagination page={page} totalPages={eventsPage?.totalPages ?? 0} onPageChange={setPage} />
    </section>
  )
}
