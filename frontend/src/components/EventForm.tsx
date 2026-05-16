import { Plus, Trash2 } from 'lucide-react'
import { useFieldArray, useForm } from 'react-hook-form'
import type { CreateEventRequest, EventResponse, EventStatus, UpdateEventRequest } from '../api/types'

type EventFormValues = CreateEventRequest

interface EventFormProps {
  initialEvent?: EventResponse
  submitLabel: string
  onSubmit: (values: CreateEventRequest | UpdateEventRequest) => Promise<void>
}

const statuses: EventStatus[] = ['DRAFT', 'PUBLISHED', 'CANCELLED', 'COMPLETED']

export function EventForm({ initialEvent, submitLabel, onSubmit }: EventFormProps): React.JSX.Element {
  const {
    register,
    control,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<EventFormValues>({
    defaultValues: initialEvent
      ? {
          name: initialEvent.name,
          description: initialEvent.description ?? '',
          venue: initialEvent.venue,
          imageUrl: initialEvent.imageUrl ?? '',
          startDateTime: initialEvent.startDateTime.slice(0, 16),
          endDateTime: initialEvent.endDateTime.slice(0, 16),
          salesEndDate: initialEvent.salesEndDate.slice(0, 16),
          status: initialEvent.status,
          ticketTypes: initialEvent.ticketTypes.map((type) => ({
            name: type.name,
            description: type.description ?? '',
            price: type.price,
            totalAvailable: type.totalAvailable,
          })),
        }
      : {
          name: '',
          description: '',
          venue: '',
          imageUrl: '',
          startDateTime: '',
          endDateTime: '',
          salesEndDate: '',
          status: 'DRAFT',
          ticketTypes: [{ name: 'STANDARD', description: '', price: 0, totalAvailable: 100 }],
        },
  })

  const { fields, append, remove } = useFieldArray({ control, name: 'ticketTypes' })

  async function submit(values: EventFormValues): Promise<void> {
    const payload = initialEvent
      ? {
          ...values,
          id: initialEvent.id,
          ticketTypes: values.ticketTypes.map((type, index) => ({ ...type, id: initialEvent.ticketTypes[index]?.id })),
        }
      : values
    await onSubmit(payload)
  }

  return (
    <form className="form-grid" onSubmit={handleSubmit(submit)}>
      <label>
        Event name
        <input {...register('name', { required: 'Event name is required' })} />
        {errors.name ? <span className="field-error">{errors.name.message}</span> : null}
      </label>
      <label>
        Venue
        <input {...register('venue', { required: 'Venue is required' })} />
        {errors.venue ? <span className="field-error">{errors.venue.message}</span> : null}
      </label>
      <label className="span-2">
        Description
        <textarea {...register('description')} rows={4} />
      </label>
      <label className="span-2">
        Image URL
        <input {...register('imageUrl')} />
      </label>
      <label>
        Starts
        <input type="datetime-local" {...register('startDateTime', { required: 'Start time is required' })} />
      </label>
      <label>
        Ends
        <input type="datetime-local" {...register('endDateTime', { required: 'End time is required' })} />
      </label>
      <label>
        Sales end
        <input type="datetime-local" {...register('salesEndDate', { required: 'Sales end is required' })} />
      </label>
      <label>
        Status
        <select {...register('status')}>
          {statuses.map((status) => (
            <option key={status} value={status}>
              {status}
            </option>
          ))}
        </select>
      </label>

      <section className="span-2 form-section">
        <div className="section-header compact">
          <div>
            <span className="eyebrow">Inventory</span>
            <h3>Ticket types</h3>
          </div>
          <button
            className="icon-button"
            type="button"
            title="Add ticket type"
            onClick={() => append({ name: 'STANDARD', description: '', price: 0, totalAvailable: 100 })}
          >
            <Plus size={18} aria-hidden="true" />
          </button>
        </div>
        <div className="ticket-type-editor">
          {fields.map((field, index) => (
            <div className="ticket-type-row" key={field.id}>
              <label>
                Type
                <select {...register(`ticketTypes.${index}.name`)}>
                  <option value="STANDARD">STANDARD</option>
                  <option value="PREMIUM">PREMIUM</option>
                  <option value="VIP">VIP</option>
                </select>
              </label>
              <label>
                Price
                <input type="number" min="0" step="0.01" {...register(`ticketTypes.${index}.price`, { valueAsNumber: true })} />
              </label>
              <label>
                Quantity
                <input type="number" min="1" {...register(`ticketTypes.${index}.totalAvailable`, { valueAsNumber: true })} />
              </label>
              <label>
                Description
                <input {...register(`ticketTypes.${index}.description`)} />
              </label>
              <button className="icon-button danger" type="button" title="Remove ticket type" onClick={() => remove(index)}>
                <Trash2 size={18} aria-hidden="true" />
              </button>
            </div>
          ))}
        </div>
      </section>
      <button className="button primary span-2" type="submit" disabled={isSubmitting}>
        {isSubmitting ? 'Saving' : submitLabel}
      </button>
    </form>
  )
}
