import { Navigate, Outlet, useLocation } from 'react-router-dom'
import type { Role } from '../api/types'
import { LoadingState } from '../components/LoadingState'
import { useAuth } from '../hooks/useAuth'

interface ProtectedRouteProps {
  roles?: Role[]
}

export function ProtectedRoute({ roles = [] }: ProtectedRouteProps): React.JSX.Element {
  const { isAuthenticated, isLoading, hasRole } = useAuth()
  const location = useLocation()

  if (isLoading) {
    return <LoadingState label="Restoring session" />
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace state={{ from: location }} />
  }

  if (roles.length > 0 && !hasRole(roles)) {
    return <Navigate to="/" replace />
  }

  return <Outlet />
}
