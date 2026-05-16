import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { getApiErrorMessage } from '../../api/client'
import type { SalesReportResponse, ValidationReportResponse } from '../../api/types'
import { ErrorState } from '../../components/ErrorState'
import { LoadingState } from '../../components/LoadingState'
import { MetricCard } from '../../components/MetricCard'
import { dashboardService } from '../../services/dashboardService'
import { formatCurrency } from '../../utils/format'

export function ReportsPage(): React.JSX.Element {
  const { eventId = '' } = useParams()
  const [sales, setSales] = useState<SalesReportResponse | null>(null)
  const [validation, setValidation] = useState<ValidationReportResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    Promise.all([dashboardService.salesReport(eventId), dashboardService.validationReport(eventId)])
      .then(([salesReport, validationReport]) => {
        setSales(salesReport)
        setValidation(validationReport)
      })
      .catch((requestError) => setError(getApiErrorMessage(requestError)))
      .finally(() => setLoading(false))
  }, [eventId])

  if (loading) {
    return <LoadingState label="Loading reports" />
  }

  if (!sales || !validation) {
    return <ErrorState message={error || 'Reports unavailable'} />
  }

  return (
    <section className="dashboard-page">
      <div className="section-header">
        <div>
          <span className="eyebrow">Reports</span>
          <h1>{sales.eventName}</h1>
        </div>
        <Link className="button ghost" to={`/organizer/events/${eventId}/sales`}>
          Sales view
        </Link>
      </div>
      {error ? <ErrorState message={error} /> : null}
      <div className="metric-grid">
        <MetricCard label="Revenue" value={formatCurrency(sales.revenue)} />
        <MetricCard label="Attendees" value={sales.attendeeCount} />
        <MetricCard label="Successful scans" value={validation.successfulValidations} />
        <MetricCard label="Duplicate scans" value={validation.duplicateValidations} />
      </div>
      <div className="report-grid">
        <article className="report-panel">
          <h2>Sales report</h2>
          <p>Pending: {sales.pendingSales}</p>
          <p>Completed: {sales.completedSales}</p>
          <p>Failed: {sales.failedSales}</p>
          <p>Refunded: {sales.refundedSales}</p>
        </article>
        <article className="report-panel">
          <h2>Validation report</h2>
          <p>Total attempts: {validation.totalAttempts}</p>
          <p>Successful: {validation.successfulValidations}</p>
          <p>Failed: {validation.failedValidations}</p>
          <p>Invalid: {validation.invalidValidations}</p>
        </article>
      </div>
    </section>
  )
}
