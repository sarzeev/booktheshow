import { useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { getApiErrorMessage } from '../../api/client'
import type { CreateEventRequest, EventResponse, UpdateEventRequest } from '../../api/types'
import { ErrorState } from '../../components/ErrorState'
import { EventForm } from '../../components/EventForm'
import { LoadingState } from '../../components/LoadingState'
import { eventService } from '../../services/eventService'

export function EditEventPage(): React.JSX.Element {
  const { eventId = '' } = useParams()
  const navigate = useNavigate()
  const [event, setEvent] = useState<EventResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    eventService
      .getOrganizerEvent(eventId)
      .then(setEvent)
      .catch((requestError) => setError(getApiErrorMessage(requestError)))
      .finally(() => setLoading(false))
  }, [eventId])

  async function updateEvent(values: CreateEventRequest | UpdateEventRequest): Promise<void> {
    setError('')
    try {
      const updated = await eventService.updateEvent(eventId, values as UpdateEventRequest)
      setEvent(updated)
    } catch (requestError) {
      setError(getApiErrorMessage(requestError))
    }
  }

  async function deleteEvent(): Promise<void> {
    setError('')
    try {
      await eventService.deleteEvent(eventId)
      navigate('/organizer/events')
    } catch (requestError) {
      setError(getApiErrorMessage(requestError))
    }
  }

  if (loading) {
    return <LoadingState label="Loading event" />
  }

  if (!event) {
    return <ErrorState message={error || 'Event not found'} />
  }

  return (
    <section className="dashboard-page">
      <div className="section-header">
        <div>
          <span className="eyebrow">Edit event</span>
          <h1>{event.name}</h1>
        </div>
        <div className="action-row">
          <Link className="button ghost" to={`/organizer/events/${event.id}/ticket-types`}>
            Ticket types
          </Link>
          <Link className="button ghost" to={`/organizer/events/${event.id}/reports`}>
            Reports
          </Link>
          <button className="button danger" type="button" onClick={deleteEvent}>
            Delete
          </button>
        </div>
      </div>
      {error ? <ErrorState message={error} /> : null}
      <EventForm initialEvent={event} submitLabel="Save event" onSubmit={updateEvent} />
    </section>
  )
}
