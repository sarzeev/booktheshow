import { ArrowRight, ShieldCheck, TicketCheck, Zap } from 'lucide-react'
import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { getApiErrorMessage } from '../../api/client'
import type { EventResponse } from '../../api/types'
import { ErrorState } from '../../components/ErrorState'
import { EventCard } from '../../components/EventCard'
import { LoadingState } from '../../components/LoadingState'
import { eventService } from '../../services/eventService'

export function LandingPage(): React.JSX.Element {
  const [events, setEvents] = useState<EventResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    eventService
      .listPublishedEvents('', 0, 3)
      .then((page) => setEvents(page.content))
      .catch((requestError) => setError(getApiErrorMessage(requestError)))
      .finally(() => setLoading(false))
  }, [])

  return (
    <>
      <section className="hero-section">
        <div className="hero-content">
          <span className="eyebrow">Sarjeev presents</span>
          <h1>BookTheShow</h1>
          <p>Browse live events, sell ticket tiers, issue QR tickets, and validate entry from one production-ready platform.</p>
          <div className="hero-actions">
            <Link className="button primary" to="/events">
              Explore events <ArrowRight size={18} aria-hidden="true" />
            </Link>
            <Link className="button secondary" to="/register">
              Start selling
            </Link>
          </div>
        </div>
      </section>

      <section className="feature-strip">
        <article>
          <TicketCheck aria-hidden="true" />
          <strong>Tiered tickets</strong>
          <span>VIP, premium, standard, and live inventory.</span>
        </article>
        <article>
          <Zap aria-hidden="true" />
          <strong>Fast validation</strong>
          <span>QR scan and manual fallback for staff teams.</span>
        </article>
        <article>
          <ShieldCheck aria-hidden="true" />
          <strong>JWT secured</strong>
          <span>Role-based access for every workflow.</span>
        </article>
      </section>

      <section className="content-section">
        <div className="section-header">
          <div>
            <span className="eyebrow">Discover</span>
            <h2>Featured events</h2>
          </div>
          <Link className="button ghost" to="/events">
            View all
          </Link>
        </div>
        {loading ? <LoadingState label="Loading events" /> : null}
        {error ? <ErrorState message={error} /> : null}
        <div className="event-grid">{events.map((event) => <EventCard key={event.id} event={event} />)}</div>
      </section>
    </>
  )
}
