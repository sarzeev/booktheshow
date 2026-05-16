import { apiClient, unwrap } from '../api/client'
import type { PurchaseResponse } from '../api/types'

export const purchaseService = {
  async purchase(eventId: string, ticketTypeId: string): Promise<PurchaseResponse> {
    return unwrap(await apiClient.post(`/published-events/${eventId}/ticket-types/${ticketTypeId}/purchase`))
  },
}
