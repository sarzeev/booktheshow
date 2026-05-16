import { createContext } from 'react'
import type { LoginRequest, RegisterRequest, Role, UserResponse } from '../api/types'

export interface AuthContextValue {
  user: UserResponse | null
  isAuthenticated: boolean
  isLoading: boolean
  login: (request: LoginRequest) => Promise<void>
  register: (request: RegisterRequest) => Promise<void>
  logout: () => Promise<void>
  hasRole: (roles: Role[]) => boolean
}

export const AuthContext = createContext<AuthContextValue | null>(null)
