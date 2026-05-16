import { LogOut, Menu, Ticket } from 'lucide-react'
import { useState } from 'react'
import { Link, NavLink, Outlet } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'

export function AppLayout(): React.JSX.Element {
  const { user, isAuthenticated, logout, hasRole } = useAuth()
  const [open, setOpen] = useState(false)

  return (
    <div className="app-shell">
      <header className="topbar">
        <Link className="brand" to="/">
          <Ticket size={24} aria-hidden="true" />
          <span>BookTheShow</span>
          <small>Sarjeev</small>
        </Link>
        <button className="icon-button mobile-only" type="button" title="Open navigation" onClick={() => setOpen(!open)}>
          <Menu size={20} aria-hidden="true" />
        </button>
        <nav className={open ? 'nav open' : 'nav'} aria-label="Primary navigation">
          <NavLink to="/events">Events</NavLink>
          {hasRole(['ROLE_ATTENDEE', 'ROLE_ADMIN']) ? <NavLink to="/attendee">My Tickets</NavLink> : null}
          {hasRole(['ROLE_ORGANIZER', 'ROLE_ADMIN']) ? <NavLink to="/organizer">Organizer</NavLink> : null}
          {hasRole(['ROLE_STAFF', 'ROLE_ADMIN']) ? <NavLink to="/staff">Staff</NavLink> : null}
        </nav>
        <div className="topbar-actions">
          {isAuthenticated && user ? (
            <>
              <span className="user-chip">{user.firstName}</span>
              <button className="icon-button" type="button" title="Log out" onClick={logout}>
                <LogOut size={18} aria-hidden="true" />
              </button>
            </>
          ) : (
            <>
              <Link className="button ghost" to="/login">
                Login
              </Link>
              <Link className="button primary" to="/register">
                Register
              </Link>
            </>
          )}
        </div>
      </header>
      <main>
        <Outlet />
      </main>
    </div>
  )
}
