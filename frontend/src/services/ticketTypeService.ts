import { apiClient, unwrap } from '../api/client'
import type { CreateTicketTypeRequest, Page, TicketTypeResponse, UpdateTicketTypeRequest } from '../api/types'

export const ticketTypeService = {
  async list(eventId: string, page = 0, size = 20): Promise<Page<TicketTypeResponse>> {
    return unwrap(await apiClient.get(`/events/${eventId}/ticket-types`, { params: { page, size } }))
  },

  async create(eventId: string, request: CreateTicketTypeRequest): Promise<TicketTypeResponse> {
    return unwrap(await apiClient.post(`/events/${eventId}/ticket-types`, request))
  },

  async update(eventId: string, ticketTypeId: string, request: UpdateTicketTypeRequest): Promise<TicketTypeResponse> {
    return unwrap(await apiClient.put(`/events/${eventId}/ticket-types/${ticketTypeId}`, request))
  },

  async remove(eventId: string, ticketTypeId: string): Promise<void> {
    await apiClient.delete(`/events/${eventId}/ticket-types/${ticketTypeId}`)
  },
}
