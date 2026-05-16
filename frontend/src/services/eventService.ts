import { apiClient, unwrap } from '../api/client'
import type { CreateEventRequest, EventResponse, Page, UpdateEventRequest } from '../api/types'

export const eventService = {
  async listOrganizerEvents(page = 0, size = 8): Promise<Page<EventResponse>> {
    return unwrap(await apiClient.get('/events', { params: { page, size, sort: 'startDateTime,asc' } }))
  },

  async getOrganizerEvent(eventId: string): Promise<EventResponse> {
    return unwrap(await apiClient.get(`/events/${eventId}`))
  },

  async createEvent(request: CreateEventRequest): Promise<EventResponse> {
    return unwrap(await apiClient.post('/events', request))
  },

  async updateEvent(eventId: string, request: UpdateEventRequest): Promise<EventResponse> {
    return unwrap(await apiClient.put(`/events/${eventId}`, request))
  },

  async deleteEvent(eventId: string): Promise<void> {
    await apiClient.delete(`/events/${eventId}`)
  },

  async listPublishedEvents(query = '', page = 0, size = 9): Promise<Page<EventResponse>> {
    return unwrap(
      await apiClient.get('/published-events', {
        params: { q: query || undefined, page, size, sort: 'startDateTime,asc' },
      }),
    )
  },

  async getPublishedEvent(eventId: string): Promise<EventResponse> {
    return unwrap(await apiClient.get(`/published-events/${eventId}`))
  },
}
