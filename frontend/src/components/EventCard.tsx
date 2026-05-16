import { CalendarDays, MapPin, Ticket } from 'lucide-react'
import { Link } from 'react-router-dom'
import type { EventResponse } from '../api/types'
import heroImage from '../assets/hero.png'
import { formatDateTime, formatCurrency } from '../utils/format'
import { StatusBadge } from './StatusBadge'

interface EventCardProps {
  event: EventResponse
  mode?: 'public' | 'management'
}

export function EventCard({ event, mode = 'public' }: EventCardProps): React.JSX.Element {
  const lowestPrice = event.ticketTypes.length > 0 ? Math.min(...event.ticketTypes.map((type) => type.price)) : 0
  const remainingTickets = event.ticketTypes.reduce((sum, type) => sum + type.remainingTickets, 0)
  const to = mode === 'public' ? `/events/${event.id}` : `/organizer/events/${event.id}/edit`

  return (
    <article className="event-card">
      <Link to={to} className="event-card-media" aria-label={`Open ${event.name}`}>
        <img src={event.imageUrl || heroImage} alt={event.name} />
      </Link>
      <div className="event-card-body">
        <div className="event-card-topline">
          <StatusBadge status={event.status} />
          <span>{formatCurrency(lowestPrice)} onwards</span>
        </div>
        <h3>{event.name}</h3>
        <p>{event.description || 'Live event experience managed on BookTheShow.'}</p>
        <div className="event-meta">
          <span>
            <CalendarDays size={16} aria-hidden="true" />
            {formatDateTime(event.startDateTime)}
          </span>
          <span>
            <MapPin size={16} aria-hidden="true" />
            {event.venue}
          </span>
          <span>
            <Ticket size={16} aria-hidden="true" />
            {remainingTickets} left
          </span>
        </div>
        {mode === 'management' ? (
          <div className="event-card-actions">
            <Link className="button ghost" to={`/organizer/events/${event.id}/ticket-types`}>
              Ticket types
            </Link>
            <Link className="button ghost" to={`/organizer/events/${event.id}/sales`}>
              Sales
            </Link>
            <Link className="button ghost" to={`/organizer/events/${event.id}/reports`}>
              Reports
            </Link>
          </div>
        ) : null}
      </div>
    </article>
  )
}
