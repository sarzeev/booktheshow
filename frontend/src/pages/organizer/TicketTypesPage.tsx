import { useEffect, useState } from 'react'
import { useForm } from 'react-hook-form'
import { useParams } from 'react-router-dom'
import { getApiErrorMessage } from '../../api/client'
import type { CreateTicketTypeRequest, TicketTypeResponse } from '../../api/types'
import { ErrorState } from '../../components/ErrorState'
import { LoadingState } from '../../components/LoadingState'
import { ticketTypeService } from '../../services/ticketTypeService'
import { formatCurrency } from '../../utils/format'

export function TicketTypesPage(): React.JSX.Element {
  const { eventId = '' } = useParams()
  const [ticketTypes, setTicketTypes] = useState<TicketTypeResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const { register, handleSubmit, reset, formState: { isSubmitting } } = useForm<CreateTicketTypeRequest>({
    defaultValues: { name: 'STANDARD', description: '', price: 0, totalAvailable: 100 },
  })

  function load(): void {
    ticketTypeService
      .list(eventId)
      .then((page) => setTicketTypes(page.content))
      .catch((requestError) => setError(getApiErrorMessage(requestError)))
      .finally(() => setLoading(false))
  }

  useEffect(load, [eventId])

  async function create(values: CreateTicketTypeRequest): Promise<void> {
    setError('')
    try {
      await ticketTypeService.create(eventId, values)
      reset({ name: 'STANDARD', description: '', price: 0, totalAvailable: 100 })
      load()
    } catch (requestError) {
      setError(getApiErrorMessage(requestError))
    }
  }

  async function remove(ticketTypeId: string): Promise<void> {
    setError('')
    try {
      await ticketTypeService.remove(eventId, ticketTypeId)
      load()
    } catch (requestError) {
      setError(getApiErrorMessage(requestError))
    }
  }

  return (
    <section className="dashboard-page">
      <div className="section-header">
        <div>
          <span className="eyebrow">Ticket types</span>
          <h1>Manage event inventory</h1>
        </div>
      </div>
      {error ? <ErrorState message={error} /> : null}
      <form className="inline-form" onSubmit={handleSubmit(create)}>
        <select {...register('name')}>
          <option value="STANDARD">STANDARD</option>
          <option value="PREMIUM">PREMIUM</option>
          <option value="VIP">VIP</option>
        </select>
        <input aria-label="Description" {...register('description')} />
        <input aria-label="Price" type="number" min="0" step="0.01" {...register('price', { valueAsNumber: true })} />
        <input aria-label="Quantity" type="number" min="1" {...register('totalAvailable', { valueAsNumber: true })} />
        <button className="button primary" type="submit" disabled={isSubmitting}>
          Add
        </button>
      </form>
      {loading ? <LoadingState label="Loading ticket types" /> : null}
      <div className="data-table">
        {ticketTypes.map((ticketType) => (
          <div className="table-row" key={ticketType.id}>
            <span>{ticketType.name}</span>
            <span>{formatCurrency(ticketType.price)}</span>
            <span>{ticketType.remainingTickets} / {ticketType.totalAvailable}</span>
            <button className="button danger" type="button" onClick={() => remove(ticketType.id)}>
              Delete
            </button>
          </div>
        ))}
      </div>
    </section>
  )
}
