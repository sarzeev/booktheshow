import type { AuthResponse } from './types'

const ACCESS_TOKEN_KEY = 'booktheshow.accessToken'
const REFRESH_TOKEN_KEY = 'booktheshow.refreshToken'
const USER_KEY = 'booktheshow.user'

export function getAccessToken(): string | null {
  return localStorage.getItem(ACCESS_TOKEN_KEY)
}

export function getRefreshToken(): string | null {
  return localStorage.getItem(REFRESH_TOKEN_KEY)
}

export function saveAuthSession(auth: AuthResponse): void {
  localStorage.setItem(ACCESS_TOKEN_KEY, auth.accessToken)
  localStorage.setItem(REFRESH_TOKEN_KEY, auth.refreshToken)
  localStorage.setItem(USER_KEY, JSON.stringify(auth.user))
}

export function readStoredUser<T>(): T | null {
  const rawUser = localStorage.getItem(USER_KEY)
  if (!rawUser) {
    return null
  }
  try {
    return JSON.parse(rawUser) as T
  } catch {
    clearAuthSession()
    return null
  }
}

export function clearAuthSession(): void {
  localStorage.removeItem(ACCESS_TOKEN_KEY)
  localStorage.removeItem(REFRESH_TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
}
