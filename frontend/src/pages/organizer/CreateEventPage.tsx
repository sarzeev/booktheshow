import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { getApiErrorMessage } from '../../api/client'
import type { CreateEventRequest, UpdateEventRequest } from '../../api/types'
import { ErrorState } from '../../components/ErrorState'
import { EventForm } from '../../components/EventForm'
import { eventService } from '../../services/eventService'

export function CreateEventPage(): React.JSX.Element {
  const navigate = useNavigate()
  const [error, setError] = useState('')

  async function createEvent(values: CreateEventRequest | UpdateEventRequest): Promise<void> {
    setError('')
    try {
      const event = await eventService.createEvent(values as CreateEventRequest)
      navigate(`/organizer/events/${event.id}/edit`)
    } catch (requestError) {
      setError(getApiErrorMessage(requestError))
    }
  }

  return (
    <section className="dashboard-page">
      <div className="section-header">
        <div>
          <span className="eyebrow">Create event</span>
          <h1>Build a ticketed event</h1>
        </div>
      </div>
      {error ? <ErrorState message={error} /> : null}
      <EventForm submitLabel="Create event" onSubmit={createEvent} />
    </section>
  )
}
