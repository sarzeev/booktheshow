import { CalendarDays, MapPin } from 'lucide-react'
import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { getApiErrorMessage } from '../../api/client'
import type { EventResponse } from '../../api/types'
import { ErrorState } from '../../components/ErrorState'
import { LoadingState } from '../../components/LoadingState'
import { StatusBadge } from '../../components/StatusBadge'
import { useAuth } from '../../hooks/useAuth'
import { eventService } from '../../services/eventService'
import { formatCurrency, formatDateTime } from '../../utils/format'
import heroImage from '../../assets/hero.png'

export function EventDetailsPage(): React.JSX.Element {
  const { eventId = '' } = useParams()
  const { isAuthenticated, hasRole } = useAuth()
  const canPurchase = isAuthenticated && hasRole(['ROLE_ATTENDEE', 'ROLE_ADMIN'])
  const [event, setEvent] = useState<EventResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    eventService
      .getPublishedEvent(eventId)
      .then(setEvent)
      .catch((requestError) => setError(getApiErrorMessage(requestError)))
      .finally(() => setLoading(false))
  }, [eventId])

  if (loading) {
    return <LoadingState label="Loading event" />
  }

  if (error || !event) {
    return <ErrorState message={error || 'Event not found'} />
  }

  return (
    <section className="details-page">
      <img className="details-hero" src={event.imageUrl || heroImage} alt={event.name} />
      <div className="details-content">
        <StatusBadge status={event.status} />
        <h1>{event.name}</h1>
        <p>{event.description}</p>
        <div className="event-meta large">
          <span>
            <CalendarDays size={18} aria-hidden="true" />
            {formatDateTime(event.startDateTime)}
          </span>
          <span>
            <MapPin size={18} aria-hidden="true" />
            {event.venue}
          </span>
        </div>
        <section className="ticket-options">
          <h2>Ticket types</h2>
          {event.ticketTypes.map((ticketType) => (
            <article className="ticket-option" key={ticketType.id}>
              <div>
                <strong>{ticketType.name}</strong>
                <span>{ticketType.description}</span>
              </div>
              <div>
                <strong>{formatCurrency(ticketType.price)}</strong>
                <span>{ticketType.remainingTickets} available</span>
              </div>
              {canPurchase ? (
                <Link className="button primary" to={`/events/${event.id}/purchase/${ticketType.id}`}>
                  Purchase
                </Link>
              ) : (
                <Link
                  className="button primary"
                  to="/login"
                  state={{ from: { pathname: `/events/${event.id}/purchase/${ticketType.id}` } }}
                >
                  {isAuthenticated ? 'Attendee account required' : 'Login to purchase'}
                </Link>
              )}
            </article>
          ))}
        </section>
      </div>
    </section>
  )
}
