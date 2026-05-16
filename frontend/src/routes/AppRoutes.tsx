import { Navigate, Route, Routes } from 'react-router-dom'
import { AppLayout } from '../layouts/AppLayout'
import { DashboardLayout } from '../layouts/DashboardLayout'
import { AttendeeDashboardPage } from '../pages/attendee/AttendeeDashboardPage'
import { PurchasedTicketsPage } from '../pages/attendee/PurchasedTicketsPage'
import { PurchaseConfirmationPage } from '../pages/attendee/PurchaseConfirmationPage'
import { PurchasePage } from '../pages/attendee/PurchasePage'
import { TicketDetailsPage } from '../pages/attendee/TicketDetailsPage'
import { LoginPage } from '../pages/auth/LoginPage'
import { RegisterPage } from '../pages/auth/RegisterPage'
import { CreateEventPage } from '../pages/organizer/CreateEventPage'
import { EditEventPage } from '../pages/organizer/EditEventPage'
import { EventsManagementPage } from '../pages/organizer/EventsManagementPage'
import { OrganizerDashboardPage } from '../pages/organizer/OrganizerDashboardPage'
import { ReportsPage } from '../pages/organizer/ReportsPage'
import { TicketSalesPage } from '../pages/organizer/TicketSalesPage'
import { TicketTypesPage } from '../pages/organizer/TicketTypesPage'
import { EventDetailsPage } from '../pages/public/EventDetailsPage'
import { EventListingPage } from '../pages/public/EventListingPage'
import { LandingPage } from '../pages/public/LandingPage'
import { EventSelectionPage } from '../pages/staff/EventSelectionPage'
import { StaffDashboardPage } from '../pages/staff/StaffDashboardPage'
import { TicketScannerPage } from '../pages/staff/TicketScannerPage'
import { ProtectedRoute } from './ProtectedRoute'

export function AppRoutes(): React.JSX.Element {
  return (
    <Routes>
      <Route element={<AppLayout />}>
        <Route index element={<LandingPage />} />
        <Route path="events" element={<EventListingPage />} />
        <Route path="events/:eventId" element={<EventDetailsPage />} />
        <Route path="login" element={<LoginPage />} />
        <Route path="register" element={<RegisterPage />} />

        <Route element={<ProtectedRoute roles={['ROLE_ATTENDEE', 'ROLE_ADMIN']} />}>
          <Route path="events/:eventId/purchase/:ticketTypeId" element={<PurchasePage />} />
          <Route path="purchase/confirmation" element={<PurchaseConfirmationPage />} />
          <Route path="attendee" element={<DashboardLayout section="attendee" />}>
            <Route index element={<AttendeeDashboardPage />} />
            <Route path="tickets" element={<PurchasedTicketsPage />} />
            <Route path="tickets/:ticketId" element={<TicketDetailsPage />} />
          </Route>
        </Route>

        <Route element={<ProtectedRoute roles={['ROLE_ORGANIZER', 'ROLE_ADMIN']} />}>
          <Route path="organizer" element={<DashboardLayout section="organizer" />}>
            <Route index element={<OrganizerDashboardPage />} />
            <Route path="events" element={<EventsManagementPage />} />
            <Route path="events/new" element={<CreateEventPage />} />
            <Route path="events/:eventId/edit" element={<EditEventPage />} />
            <Route path="events/:eventId/ticket-types" element={<TicketTypesPage />} />
            <Route path="events/:eventId/sales" element={<TicketSalesPage />} />
            <Route path="events/:eventId/reports" element={<ReportsPage />} />
          </Route>
        </Route>

        <Route element={<ProtectedRoute roles={['ROLE_STAFF', 'ROLE_ADMIN']} />}>
          <Route path="staff" element={<DashboardLayout section="staff" />}>
            <Route index element={<StaffDashboardPage />} />
            <Route path="events" element={<EventSelectionPage />} />
            <Route path="events/:eventId/scan" element={<TicketScannerPage />} />
          </Route>
        </Route>

        <Route path="*" element={<Navigate to="/" replace />} />
      </Route>
    </Routes>
  )
}
