import { CheckCircle2 } from 'lucide-react'
import { Link, useLocation } from 'react-router-dom'
import type { PurchaseResponse } from '../../api/types'
import { formatCurrency, formatDateTime } from '../../utils/format'

export function PurchaseConfirmationPage(): React.JSX.Element {
  const location = useLocation()
  const purchase = location.state as PurchaseResponse | null

  if (!purchase) {
    return (
      <section className="content-section narrow">
        <h1>Purchase confirmation</h1>
        <p>No recent purchase was found in this browser session.</p>
        <Link className="button primary" to="/attendee/tickets">
          View tickets
        </Link>
      </section>
    )
  }

  return (
    <section className="content-section narrow">
      <article className="confirmation-panel">
        <CheckCircle2 size={52} aria-hidden="true" />
        <span className="eyebrow">Confirmed</span>
        <h1>Your ticket is ready</h1>
        <p>Sale {purchase.saleId}</p>
        <div className="summary-grid">
          <span>Amount paid</span>
          <strong>{formatCurrency(purchase.amount)}</strong>
          <span>Purchased</span>
          <strong>{formatDateTime(purchase.purchaseDateTime)}</strong>
          <span>Ticket status</span>
          <strong>{purchase.ticketStatus}</strong>
        </div>
        <Link className="button primary" to={`/attendee/tickets/${purchase.ticketId}`}>
          Open ticket QR
        </Link>
      </article>
    </section>
  )
}
