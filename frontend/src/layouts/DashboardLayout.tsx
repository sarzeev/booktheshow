import { CalendarPlus, LayoutDashboard, QrCode, TicketCheck, Tickets } from 'lucide-react'
import { NavLink, Outlet } from 'react-router-dom'

interface DashboardLayoutProps {
  section: 'attendee' | 'organizer' | 'staff'
}

const navItems = {
  attendee: [
    { to: '/attendee', label: 'Overview', icon: LayoutDashboard },
    { to: '/attendee/tickets', label: 'Tickets', icon: Tickets },
  ],
  organizer: [
    { to: '/organizer', label: 'Dashboard', icon: LayoutDashboard },
    { to: '/organizer/events', label: 'Events', icon: CalendarPlus },
  ],
  staff: [
    { to: '/staff', label: 'Dashboard', icon: LayoutDashboard },
    { to: '/staff/events', label: 'Validate', icon: QrCode },
  ],
}

export function DashboardLayout({ section }: DashboardLayoutProps): React.JSX.Element {
  return (
    <div className="dashboard-shell">
      <aside className="sidebar">
        <span className="eyebrow">{section}</span>
        <nav aria-label={`${section} dashboard`}>
          {navItems[section].map((item) => {
            const Icon = item.icon
            return (
              <NavLink key={item.to} to={item.to} end>
                <Icon size={18} aria-hidden="true" />
                {item.label}
              </NavLink>
            )
          })}
          {section === 'organizer' ? (
            <NavLink to="/organizer/events/new">
              <TicketCheck size={18} aria-hidden="true" />
              Create event
            </NavLink>
          ) : null}
        </nav>
      </aside>
      <section className="dashboard-content">
        <Outlet />
      </section>
    </div>
  )
}
