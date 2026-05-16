import { type PropsWithChildren, useCallback, useEffect, useMemo, useState } from 'react'
import { clearAuthSession, getRefreshToken, readStoredUser, saveAuthSession } from '../api/tokenStore'
import type { AuthResponse, LoginRequest, RegisterRequest, Role, UserResponse } from '../api/types'
import { authService } from '../services/authService'
import { AuthContext, type AuthContextValue } from './authContextCore'

export function AuthProvider({ children }: PropsWithChildren): React.JSX.Element {
  const [user, setUser] = useState<UserResponse | null>(() => readStoredUser<UserResponse>())
  const [isLoading, setIsLoading] = useState(true)

  const applySession = useCallback((auth: AuthResponse) => {
    saveAuthSession(auth)
    setUser(auth.user)
  }, [])

  const login = useCallback(
    async (request: LoginRequest) => {
      const auth = await authService.login(request)
      applySession(auth)
    },
    [applySession],
  )

  const register = useCallback(
    async (request: RegisterRequest) => {
      const auth = await authService.register(request)
      applySession(auth)
    },
    [applySession],
  )

  const logout = useCallback(async () => {
    const refreshToken = getRefreshToken()
    try {
      if (refreshToken) {
        await authService.logout(refreshToken)
      }
    } finally {
      clearAuthSession()
      setUser(null)
    }
  }, [])

  const hasRole = useCallback(
    (roles: Role[]) => {
      if (!user) {
        return false
      }
      return user.roles.some((role) => roles.includes(role))
    },
    [user],
  )

  useEffect(() => {
    async function restoreSession(): Promise<void> {
      const refreshToken = getRefreshToken()
      if (!refreshToken) {
        setIsLoading(false)
        return
      }
      try {
        const auth = await authService.refresh(refreshToken)
        applySession(auth)
      } catch {
        clearAuthSession()
        setUser(null)
      } finally {
        setIsLoading(false)
      }
    }

    restoreSession()
    window.addEventListener('booktheshow:logout', logout)
    return () => window.removeEventListener('booktheshow:logout', logout)
  }, [applySession, logout])

  const value = useMemo<AuthContextValue>(
    () => ({
      user,
      isAuthenticated: Boolean(user),
      isLoading,
      login,
      register,
      logout,
      hasRole,
    }),
    [hasRole, isLoading, login, logout, register, user],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
