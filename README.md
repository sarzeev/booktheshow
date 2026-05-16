# BookTheShow

Production-grade event ticketing platform with QR-based ticket validation, JWT authentication, role-based access control, and full-stack Spring Boot + React architecture.


# Overview

BookTheShow is a modern full-stack event management and digital ticketing platform designed for real-world event workflows.

The platform supports:
* Event publishing and management
* Ticket inventory handling
* QR-based digital tickets
* Staff-side ticket scanning and validation
* Role-based dashboards
* JWT authentication with refresh tokens
* Organizer analytics and reporting
* Attendee purchase lifecycle

The project was built using a production-style layered architecture with scalable backend patterns and modular frontend design.

---

# Features

## Authentication & Security

* JWT Access + Refresh Token authentication
* Spring Security integration
* Role-based authorization
* Secure protected routes
* Token refresh workflow
* Logout + session invalidation

### Supported Roles

| Role      | Capabilities                       |
| --------- | ---------------------------------- |
| ADMIN     | System administration              |
| ORGANIZER | Create and manage events           |
| ATTENDEE  | Browse and purchase tickets        |
| STAFF     | Validate tickets and scan QR codes |

---

## Organizer Features

* Create and edit events
* Publish/unpublish events
* Manage ticket inventory
* Configure ticket types
* View sales analytics
* Monitor validation statistics
* Dashboard metrics and reports

---

## Attendee Features

* Browse published events
* Purchase tickets
* View purchased tickets
* Access digital QR tickets
* Ticket management dashboard

---

## Staff Features

* QR code scanner
* Manual ticket validation
* Duplicate detection prevention
* Validation status tracking
* Event selection workflow

---

# Tech Stack

## Backend

| Technology      | Usage                          |
| --------------- | ------------------------------ |
| Java 21         | Core backend language          |
| Spring Boot 3.4 | Backend framework              |
| Spring Security | Authentication & authorization |
| JWT             | Stateless authentication       |
| Spring Data JPA | ORM & persistence              |
| PostgreSQL      | Relational database            |
| Maven           | Dependency management          |
| ZXing           | QR generation                  |

---

## Frontend

| Technology   | Usage               |
| ------------ | ------------------- |
| React 19     | Frontend framework  |
| TypeScript   | Type-safe frontend  |
| Vite         | Frontend tooling    |
| Axios        | API communication   |
| React Router | Client-side routing |
| Context API  | State management    |

---

# System Architecture

```text
Frontend (React + Vite)
        ↓
REST API Layer
        ↓
Spring Boot Backend
        ↓
Service Layer
        ↓
JPA / Hibernate
        ↓
PostgreSQL Database
```

---

# Backend Architecture

The backend follows a layered enterprise-style architecture:

```text
controllers/
services/
services/impl/
repositories/
entities/
requests/
responses/
security/
filters/
validators/
exceptions/
config/
```

### Key Architectural Components

* DTO-based request/response handling
* Repository abstraction layer
* Service-driven business logic
* Centralized exception handling
* JWT authentication filter
* Validation layer
* Role-based security configuration

---

# QR Validation Workflow

## Ticket Purchase Flow

```text
Attendee Purchases Ticket
            ↓
Ticket Generated
            ↓
QR Code Created
            ↓
QR Linked To Ticket
            ↓
Attendee Accesses Digital Ticket
```

---

## Validation Flow

```text
Staff Scans QR
        ↓
QR Payload Parsed
        ↓
Ticket Retrieved
        ↓
Validation Status Checked
        ↓
Duplicate Prevention
        ↓
Entry Approved / Rejected
```

---

# Database Design

## Core Entities

* User
* Role
* Event
* TicketType
* Ticket
* TicketSale
* TicketValidation
* QrCode
* RefreshToken

---

# Screenshots

## Landing Page

<img width="1882" height="960" alt="image" src="https://github.com/user-attachments/assets/b57f4765-bc3a-467b-a558-1c57cc55f618" />
<img width="1918" height="968" alt="image" src="https://github.com/user-attachments/assets/2bd1213b-626b-4dd6-90b0-79840caf820f" />
<img width="822" height="737" alt="image" src="https://github.com/user-attachments/assets/44427ee6-6ae3-4cc7-b7fd-7eba182f8126" />

---

## Organizer Dashboard

<img width="1918" height="967" alt="image" src="https://github.com/user-attachments/assets/8447992b-c7d5-4bc3-a4bf-014f92f949f9" />


---

## Ticket Purchase Flow
<img width="1918" height="965" alt="image" src="https://github.com/user-attachments/assets/54ffdf2c-6ab3-4f41-a544-194821b96041" />

<img width="1918" height="965" alt="image" src="https://github.com/user-attachments/assets/ef3797a5-9da2-40aa-a9bb-383bebe00f04" />
<img width="1918" height="960" alt="image" src="https://github.com/user-attachments/assets/cbcbddb4-1a37-44cb-abc1-9bf1f9aafdbf" />


---

## QR Ticket

<img width="1918" height="960" alt="image" src="https://github.com/user-attachments/assets/be92636b-ea94-4e51-9f3d-cd21bdc4146a" />


---

## Staff QR Validation

<img width="1885" height="890" alt="image" src="https://github.com/user-attachments/assets/543462c1-14eb-4748-beeb-359facdaae1d" />

---

# Local Setup

## Prerequisites

* Java 21+
* Node.js 20+
* PostgreSQL 17+
* Maven 3.9+

---

# Database Setup

Create PostgreSQL database:

```sql
CREATE DATABASE booktheshow;
```

---

# Backend Setup

## Configure Database

Update:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/booktheshow
spring.datasource.username=postgres
spring.datasource.password=your_password
```

---

## Run Backend

```bash
mvn spring-boot:run
```

Backend runs on:

```text
http://localhost:8081
```

---

# Frontend Setup

## Navigate to frontend

```bash
cd frontend
```

---

## Install dependencies

```bash
npm install
```

---

## Configure Environment

Create:

```text
frontend/.env
```

Add:

```env
VITE_API_BASE_URL=http://localhost:8081/api/v1
```

---

## Run Frontend

```bash
npm run dev
```

Frontend runs on:

```text
http://localhost:5173
```

---

# API Modules

## Authentication

```text
/api/v1/auth
```

* register
* login
* refresh
* logout

---

## Events

```text
/api/v1/events
```

* create event
* update event
* publish event
* manage inventory

---

## Tickets

```text
/api/v1/tickets
```

* attendee tickets
* QR retrieval
* ticket details

---

## Ticket Validation

```text
/api/v1/events/{id}/ticket-validations
```

* QR validation
* manual validation
* duplicate prevention

---

# Security

The application uses JWT-based stateless authentication.

## Security Features

* JWT access tokens
* Refresh token rotation
* Spring Security filter chain
* Role-based authorization
* Protected frontend routes
* Axios token interceptors
* Automatic token refresh

---

# Project Highlights

* Full-stack production-style architecture
* Real QR-based ticket lifecycle
* Role-driven workflows
* Enterprise backend structure
* JWT + refresh token implementation
* PostgreSQL relational modeling
* Transactional purchase system
* Responsive frontend dashboards

---

# Future Improvements

* Payment gateway integration
* Email notifications
* Cloud deployment
* Redis caching
* WebSocket live updates
* Seat selection system
* Event image uploads
* CI/CD pipelines
* Analytics visualization

---

# Learning Outcomes

This project involved:

* Spring Security
* JWT authentication
* React frontend architecture
* PostgreSQL integration
* REST API design
* QR code workflows
* Transaction management
* Git & GitHub workflows
* Full-stack debugging
* Production-style project structuring

---

# Author

## Sarjeev Sharma

Full-stack developer focused on scalable backend systems, security architecture, and modern web applications.

---

# License

This project is developed for educational and portfolio purposes.
