import { apiClient, unwrap } from '../api/client'
import type { AuthResponse, LoginRequest, RegisterRequest } from '../api/types'

export const authService = {
  async login(request: LoginRequest): Promise<AuthResponse> {
    return unwrap(await apiClient.post('/auth/login', request))
  },

  async register(request: RegisterRequest): Promise<AuthResponse> {
    return unwrap(await apiClient.post('/auth/register', request))
  },

  async refresh(refreshToken: string): Promise<AuthResponse> {
    return unwrap(await apiClient.post('/auth/refresh', { refreshToken }))
  },

  async logout(refreshToken: string): Promise<void> {
    await apiClient.post('/auth/logout', { refreshToken })
  },
}
