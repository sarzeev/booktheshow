import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { getApiErrorMessage } from '../../api/client'
import type { Page, TicketResponse } from '../../api/types'
import { ErrorState } from '../../components/ErrorState'
import { LoadingState } from '../../components/LoadingState'
import { Pagination } from '../../components/Pagination'
import { StatusBadge } from '../../components/StatusBadge'
import { ticketService } from '../../services/ticketService'
import { formatDateTime } from '../../utils/format'

export function PurchasedTicketsPage(): React.JSX.Element {
  const [page, setPage] = useState(0)
  const [ticketsPage, setTicketsPage] = useState<Page<TicketResponse> | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    setLoading(true)
    setError('')
    ticketService
      .list(page, 10)
      .then(setTicketsPage)
      .catch((requestError) => setError(getApiErrorMessage(requestError)))
      .finally(() => setLoading(false))
  }, [page])

  return (
    <section className="dashboard-page">
      <div className="section-header">
        <div>
          <span className="eyebrow">Tickets</span>
          <h1>Purchased tickets</h1>
        </div>
      </div>
      {loading ? <LoadingState label="Loading tickets" /> : null}
      {error ? <ErrorState message={error} /> : null}
      <div className="data-table">
        {ticketsPage?.content.map((ticket) => (
          <Link className="table-row" key={ticket.id} to={`/attendee/tickets/${ticket.id}`}>
            <span>{ticket.id}</span>
            <span>{formatDateTime(ticket.createdDateTime)}</span>
            <StatusBadge status={ticket.status} />
          </Link>
        ))}
      </div>
      <Pagination page={page} totalPages={ticketsPage?.totalPages ?? 0} onPageChange={setPage} />
    </section>
  )
}
