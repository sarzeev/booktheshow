import { apiClient, unwrap } from '../api/client'
import type { Page, TicketValidationRequest, TicketValidationResponse } from '../api/types'

export const validationService = {
  async validate(eventId: string, request: TicketValidationRequest): Promise<TicketValidationResponse> {
    return unwrap(await apiClient.post(`/events/${eventId}/ticket-validations`, request))
  },

  async list(eventId: string, page = 0, size = 20): Promise<Page<TicketValidationResponse>> {
    return unwrap(await apiClient.get(`/events/${eventId}/ticket-validations`, { params: { page, size } }))
  },
}
