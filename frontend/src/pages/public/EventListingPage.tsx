import { Search } from 'lucide-react'
import { useEffect, useState } from 'react'
import { getApiErrorMessage } from '../../api/client'
import type { EventResponse, Page } from '../../api/types'
import { ErrorState } from '../../components/ErrorState'
import { EventCard } from '../../components/EventCard'
import { LoadingState } from '../../components/LoadingState'
import { Pagination } from '../../components/Pagination'
import { eventService } from '../../services/eventService'

export function EventListingPage(): React.JSX.Element {
  const [query, setQuery] = useState('')
  const [page, setPage] = useState(0)
  const [eventsPage, setEventsPage] = useState<Page<EventResponse> | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    setLoading(true)
    setError('')
    eventService
      .listPublishedEvents(query, page, 9)
      .then(setEventsPage)
      .catch((requestError) => setError(getApiErrorMessage(requestError)))
      .finally(() => setLoading(false))
  }, [page, query])

  return (
    <section className="content-section">
      <div className="section-header">
        <div>
          <span className="eyebrow">Attendee flow</span>
          <h1>Browse published events</h1>
        </div>
      </div>
      <div className="search-panel">
        <Search size={18} aria-hidden="true" />
        <input aria-label="Search events" value={query} onChange={(event) => { setPage(0); setQuery(event.target.value) }} />
      </div>
      {loading ? <LoadingState label="Searching events" /> : null}
      {error ? <ErrorState message={error} /> : null}
      <div className="event-grid">
        {eventsPage?.content.map((event) => (
          <EventCard key={event.id} event={event} />
        ))}
      </div>
      <Pagination page={page} totalPages={eventsPage?.totalPages ?? 0} onPageChange={setPage} />
    </section>
  )
}
