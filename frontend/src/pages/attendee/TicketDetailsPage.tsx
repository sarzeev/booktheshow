import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { getApiErrorMessage } from '../../api/client'
import type { QrCodeImageResponse, TicketResponse } from '../../api/types'
import { ErrorState } from '../../components/ErrorState'
import { LoadingState } from '../../components/LoadingState'
import { QRDisplay } from '../../components/QRDisplay'
import { StatusBadge } from '../../components/StatusBadge'
import { ticketService } from '../../services/ticketService'
import { formatDateTime } from '../../utils/format'

export function TicketDetailsPage(): React.JSX.Element {
  const { ticketId = '' } = useParams()
  const [ticket, setTicket] = useState<TicketResponse | null>(null)
  const [qr, setQr] = useState<QrCodeImageResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    Promise.all([ticketService.get(ticketId), ticketService.getQrData(ticketId)])
      .then(([ticketResponse, qrResponse]) => {
        setTicket(ticketResponse)
        setQr(qrResponse)
      })
      .catch((requestError) => setError(getApiErrorMessage(requestError)))
      .finally(() => setLoading(false))
  }, [ticketId])

  if (loading) {
    return <LoadingState label="Loading ticket" />
  }

  if (error || !ticket || !qr) {
    return <ErrorState message={error || 'Ticket not found'} />
  }

  return (
    <section className="dashboard-page">
      <div className="section-header">
        <div>
          <span className="eyebrow">Ticket QR</span>
          <h1>Entry ticket</h1>
        </div>
        <StatusBadge status={ticket.status} />
      </div>
      <article className="ticket-detail-card">
        <QRDisplay value={qr.qrCodeData} title={`Ticket ${ticket.id}`} />
        <div className="summary-grid">
          <span>Ticket ID</span>
          <strong>{ticket.id}</strong>
          <span>Issued</span>
          <strong>{formatDateTime(ticket.createdDateTime)}</strong>
          <span>QR ID</span>
          <strong>{qr.qrCodeId}</strong>
        </div>
      </article>
    </section>
  )
}
