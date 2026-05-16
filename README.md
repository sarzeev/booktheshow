# BookTheShow

Production-grade event ticket platform by **Sarjeev**.

Organizers publish events and ticket tiers, attendees purchase QR tickets, and staff validate entry at the door. Authentication uses Spring Security JWT (access + refresh tokens) against a local PostgreSQL database.

## Stack

| Layer | Technology |
|-------|------------|
| Backend | Spring Boot 3.4, Java 21, Spring Security, JPA, PostgreSQL |
| Frontend | React 19, Vite, TypeScript, React Router, Axios |
| Auth | JWT access + refresh tokens (no OAuth / Keycloak) |

## Prerequisites

- Java 21
- Maven 3.9+
- Node.js 20+
- PostgreSQL running locally

## Database setup

```sql
CREATE DATABASE booktheshow;
```

Default connection (see `src/main/resources/application.properties`):

- URL: `jdbc:postgresql://localhost:5432/booktheshow`
- User: `postgres`
- Password: `postgres`

## Run the backend

```bash
mvn spring-boot:run
```

API base: `http://localhost:8081/api/v1`

Optional JWT secret override:

```bash
set BOOKTHESHOW_JWT_SECRET=your-256-bit-or-longer-secret
```

## Run the frontend

```bash
cd frontend
cp .env.example .env
npm install
npm run dev
```

App: `http://localhost:5173`

CORS is preconfigured for `http://localhost:5173` and `http://127.0.0.1:5173`.

## Roles

| Role | Capabilities |
|------|----------------|
| `ROLE_ATTENDEE` | Browse events, purchase tickets, view QR tickets |
| `ROLE_ORGANIZER` | Create/manage events, ticket types, sales & validation reports |
| `ROLE_STAFF` | Scan QR codes and validate tickets at events |
| `ROLE_ADMIN` | Full access |

Register at `/register` and choose a role (attendee, organizer, or staff).

## Main API groups

- `POST /api/v1/auth/*` — register, login, refresh, logout
- `GET /api/v1/published-events` — public event catalog
- `POST /api/v1/published-events/{eventId}/ticket-types/{ticketTypeId}/purchase` — purchase
- `GET /api/v1/tickets` — attendee tickets & QR data
- `GET/POST/PUT/DELETE /api/v1/events` — organizer event management
- `GET /api/v1/events/{eventId}/dashboard/*` — sales & validation reports
- `POST /api/v1/events/{eventId}/ticket-validations` — staff validation

Architecture and workflows: `docs/build-event-ticket-platform.pdf`.

## Project layout

```
src/main/java/com/sarjeev/booktheshow/   # Spring Boot backend
frontend/src/                             # React SPA
docs/build-event-ticket-platform.pdf      # Architecture reference
```
