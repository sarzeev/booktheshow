import { apiClient, unwrap } from '../api/client'
import type { DashboardSummaryResponse, SalesReportResponse, ValidationReportResponse } from '../api/types'

export const dashboardService = {
  async summary(eventId: string): Promise<DashboardSummaryResponse> {
    return unwrap(await apiClient.get(`/events/${eventId}/dashboard`))
  },

  async salesReport(eventId: string): Promise<SalesReportResponse> {
    return unwrap(await apiClient.get(`/events/${eventId}/dashboard/sales-report`))
  },

  async validationReport(eventId: string): Promise<ValidationReportResponse> {
    return unwrap(await apiClient.get(`/events/${eventId}/dashboard/validation-report`))
  },
}
