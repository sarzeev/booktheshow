import { API_BASE_URL, apiClient, unwrap } from '../api/client'
import type { Page, QrCodeImageResponse, TicketResponse } from '../api/types'

export const ticketService = {
  async list(page = 0, size = 10): Promise<Page<TicketResponse>> {
    return unwrap(await apiClient.get('/tickets', { params: { page, size, sort: 'createdDateTime,desc' } }))
  },

  async get(ticketId: string): Promise<TicketResponse> {
    return unwrap(await apiClient.get(`/tickets/${ticketId}`))
  },

  async getQrData(ticketId: string): Promise<QrCodeImageResponse> {
    return unwrap(await apiClient.get(`/tickets/${ticketId}/qr-code/data`))
  },

  qrImageUrl(ticketId: string): string {
    return `${API_BASE_URL}/tickets/${ticketId}/qr-code`
  },
}
