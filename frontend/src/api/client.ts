import axios, { AxiosError, type AxiosRequestConfig } from 'axios'
import { clearAuthSession, getAccessToken, getRefreshToken, saveAuthSession } from './tokenStore'
import type { ApiResponse, AuthResponse } from './types'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8081/api/v1'

interface RetryableRequest extends AxiosRequestConfig {
  _retry?: boolean
}

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

apiClient.interceptors.request.use((config) => {
  const token = getAccessToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

apiClient.interceptors.response.use(
  (response) => response,
  async (error: AxiosError) => {
    const originalRequest = error.config as RetryableRequest | undefined
    const isAuthEndpoint = originalRequest?.url?.startsWith('/auth/')
    if (error.response?.status === 401 && originalRequest && !originalRequest._retry && !isAuthEndpoint) {
      originalRequest._retry = true
      const refreshToken = getRefreshToken()
      if (!refreshToken) {
        clearAuthSession()
        window.dispatchEvent(new Event('booktheshow:logout'))
        return Promise.reject(error)
      }

      try {
        const response = await axios.post<ApiResponse<AuthResponse>>(`${API_BASE_URL}/auth/refresh`, {
          refreshToken,
        })
        saveAuthSession(response.data.data)
        originalRequest.headers = {
          ...originalRequest.headers,
          Authorization: `Bearer ${response.data.data.accessToken}`,
        }
        return apiClient(originalRequest)
      } catch (refreshError) {
        clearAuthSession()
        window.dispatchEvent(new Event('booktheshow:logout'))
        return Promise.reject(refreshError)
      }
    }
    return Promise.reject(error)
  },
)

export function unwrap<T>(response: { data: ApiResponse<T> }): T {
  return response.data.data
}

export function getApiErrorMessage(error: unknown): string {
  if (axios.isAxiosError(error)) {
    const data = error.response?.data as { message?: string; error?: string } | undefined
    return data?.message ?? data?.error ?? error.message
  }
  if (error instanceof Error) {
    return error.message
  }
  return 'Something went wrong'
}
