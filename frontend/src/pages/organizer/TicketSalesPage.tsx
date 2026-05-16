import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { getApiErrorMessage } from '../../api/client'
import type { DashboardSummaryResponse } from '../../api/types'
import { ErrorState } from '../../components/ErrorState'
import { LoadingState } from '../../components/LoadingState'
import { MetricCard } from '../../components/MetricCard'
import { dashboardService } from '../../services/dashboardService'
import { formatCurrency } from '../../utils/format'

export function TicketSalesPage(): React.JSX.Element {
  const { eventId = '' } = useParams()
  const [summary, setSummary] = useState<DashboardSummaryResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    dashboardService
      .summary(eventId)
      .then(setSummary)
      .catch((requestError) => setError(getApiErrorMessage(requestError)))
      .finally(() => setLoading(false))
  }, [eventId])

  if (loading) {
    return <LoadingState label="Loading sales metrics" />
  }

  if (!summary) {
    return <ErrorState message={error || 'Sales data unavailable'} />
  }

  return (
    <section className="dashboard-page">
      <div className="section-header">
        <div>
          <span className="eyebrow">Ticket sales</span>
          <h1>{summary.eventName}</h1>
        </div>
      </div>
      {error ? <ErrorState message={error} /> : null}
      <div className="metric-grid">
        <MetricCard label="Revenue" value={formatCurrency(summary.revenue)} />
        <MetricCard label="Completed sales" value={summary.completedSales} />
        <MetricCard label="Tickets sold" value={summary.ticketsSold} />
        <MetricCard label="Used tickets" value={summary.usedTickets} />
      </div>
      <div className="data-table">
        {summary.inventory.map((item) => (
          <div className="table-row" key={item.ticketTypeId}>
            <span>{item.name}</span>
            <span>{formatCurrency(item.price)}</span>
            <span>{item.soldTickets} sold</span>
            <span>{item.remainingTickets} remaining</span>
          </div>
        ))}
      </div>
    </section>
  )
}
