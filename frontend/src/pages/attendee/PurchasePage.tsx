import { useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { getApiErrorMessage } from '../../api/client'
import type { EventResponse, PurchaseResponse, TicketTypeResponse } from '../../api/types'
import { ErrorState } from '../../components/ErrorState'
import { LoadingState } from '../../components/LoadingState'
import { eventService } from '../../services/eventService'
import { purchaseService } from '../../services/purchaseService'
import { formatCurrency, formatDateTime } from '../../utils/format'

export function PurchasePage(): React.JSX.Element {
  const { eventId = '', ticketTypeId = '' } = useParams()
  const navigate = useNavigate()
  const [event, setEvent] = useState<EventResponse | null>(null)
  const [ticketType, setTicketType] = useState<TicketTypeResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [purchasing, setPurchasing] = useState(false)
  const [error, setError] = useState('')

  useEffect(() => {
    eventService
      .getPublishedEvent(eventId)
      .then((loadedEvent) => {
        setEvent(loadedEvent)
        setTicketType(loadedEvent.ticketTypes.find((type) => type.id === ticketTypeId) ?? null)
      })
      .catch((requestError) => setError(getApiErrorMessage(requestError)))
      .finally(() => setLoading(false))
  }, [eventId, ticketTypeId])

  async function purchase(): Promise<void> {
    setPurchasing(true)
    setError('')
    try {
      const response: PurchaseResponse = await purchaseService.purchase(eventId, ticketTypeId)
      navigate('/purchase/confirmation', { state: response })
    } catch (requestError) {
      setError(getApiErrorMessage(requestError))
    } finally {
      setPurchasing(false)
    }
  }

  if (loading) {
    return <LoadingState label="Preparing checkout" />
  }

  if (!event || !ticketType) {
    return <ErrorState message="Ticket type is unavailable" />
  }

  return (
    <section className="content-section narrow">
      <div className="section-header">
        <div>
          <span className="eyebrow">Checkout</span>
          <h1>Confirm ticket purchase</h1>
        </div>
      </div>
      {error ? <ErrorState message={error} /> : null}
      <article className="purchase-card">
        <div>
          <span className="eyebrow">{ticketType.name}</span>
          <h2>{event.name}</h2>
          <p>{event.venue}</p>
          <p>{formatDateTime(event.startDateTime)}</p>
        </div>
        <div className="purchase-total">
          <span>Total</span>
          <strong>{formatCurrency(ticketType.price)}</strong>
          <small>{ticketType.remainingTickets} tickets remaining</small>
        </div>
      </article>
      <div className="action-row">
        <Link className="button ghost" to={`/events/${event.id}`}>
          Back
        </Link>
        <button className="button primary" type="button" disabled={purchasing} onClick={purchase}>
          {purchasing ? 'Purchasing' : 'Purchase ticket'}
        </button>
      </div>
    </section>
  )
}
