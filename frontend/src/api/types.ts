export type Role = 'ROLE_ADMIN' | 'ROLE_ORGANIZER' | 'ROLE_ATTENDEE' | 'ROLE_STAFF'

export type EventStatus = 'DRAFT' | 'PUBLISHED' | 'CANCELLED' | 'COMPLETED'
export type TicketStatus = 'ACTIVE' | 'USED' | 'CANCELLED' | 'EXPIRED'
export type TicketSaleStatus = 'PENDING' | 'COMPLETED' | 'FAILED' | 'REFUNDED'
export type TicketValidationStatus = 'SUCCESS' | 'FAILED' | 'DUPLICATE' | 'INVALID'
export type ValidationMethod = 'QR_SCAN' | 'MANUAL_ENTRY'

export interface ApiResponse<T> {
  success: boolean
  message: string
  data: T
  timestamp: string
}

export interface Page<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
}

export interface UserResponse {
  id: string
  firstName: string
  lastName: string
  email: string
  enabled: boolean
  createdAt: string
  roles: Role[]
}

export interface AuthResponse {
  accessToken: string
  refreshToken: string
  tokenType: string
  expiresInMs: number
  user: UserResponse
}

export interface LoginRequest {
  email: string
  password: string
}

export interface RegisterRequest {
  firstName: string
  lastName: string
  email: string
  password: string
  roles: Role[]
}

export interface TicketTypeResponse {
  id: string
  name: string
  description: string | null
  price: number
  totalAvailable: number
  remainingTickets: number
}

export interface EventResponse {
  id: string
  name: string
  description: string | null
  venue: string
  imageUrl: string | null
  startDateTime: string
  endDateTime: string
  salesEndDate: string
  status: EventStatus
  organizerId: string
  ticketTypes: TicketTypeResponse[]
}

export interface CreateTicketTypeRequest {
  name: string
  description: string
  price: number
  totalAvailable: number
}

export interface CreateEventRequest {
  name: string
  description: string
  venue: string
  imageUrl: string
  startDateTime: string
  endDateTime: string
  salesEndDate: string
  status: EventStatus
  ticketTypes: CreateTicketTypeRequest[]
}

export interface UpdateTicketTypeRequest extends CreateTicketTypeRequest {
  id?: string
}

export interface UpdateEventRequest extends Omit<CreateEventRequest, 'ticketTypes'> {
  id: string
  ticketTypes: UpdateTicketTypeRequest[]
}

export interface PurchaseResponse {
  saleId: string
  ticketId: string
  qrCodeId: string
  eventId: string
  ticketTypeId: string
  amount: number
  saleStatus: TicketSaleStatus
  ticketStatus: TicketStatus
  purchaseDateTime: string
}

export interface TicketResponse {
  id: string
  status: TicketStatus
  createdDateTime: string
  attendeeId: string
  ticketTypeId: string
  ticketSaleId: string
  qrCodeId: string
}

export interface QrCodeImageResponse {
  qrCodeId: string
  ticketId: string
  qrCodeData: string
}

export interface TicketValidationRequest {
  id: string
  validationMethod: ValidationMethod
}

export interface TicketValidationResponse {
  id: string
  status: TicketValidationStatus
  validationTime: string
  validationMethod: ValidationMethod
  ticketId: string
  validatedById: string
}

export interface DashboardSummaryResponse {
  eventId: string
  eventName: string
  revenue: number
  completedSales: number
  ticketsSold: number
  activeTickets: number
  usedTickets: number
  validationAttempts: number
  successfulValidations: number
  duplicateValidations: number
  inventory: TicketInventoryResponse[]
}

export interface TicketInventoryResponse {
  ticketTypeId: string
  name: string
  price: number
  totalAvailable: number
  remainingTickets: number
  soldTickets: number
}

export interface SalesReportResponse {
  eventId: string
  eventName: string
  revenue: number
  pendingSales: number
  completedSales: number
  failedSales: number
  refundedSales: number
  attendeeCount: number
}

export interface ValidationReportResponse {
  eventId: string
  eventName: string
  totalAttempts: number
  successfulValidations: number
  failedValidations: number
  duplicateValidations: number
  invalidValidations: number
}
