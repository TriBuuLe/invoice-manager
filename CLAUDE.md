
# CLAUDE.md — Invoice SaaS Project Guide

## Project Overview
Invoice SaaS is a multi-tenant billing and invoice management web application.

## Tech Stack
- Backend: Spring Boot 4.0.6, Java 21, Maven
- Frontend: Angular 21, SCSS
- Database: PostgreSQL 16 (Docker)
- Migrations: Flyway
- Auth: JWT (access + refresh tokens)
- Payments: Stripe
- Email: SendGrid
- File Storage: AWS S3

## Project Structure
- Backend package: com.invoicesaas.backend
- Backend runs on: localhost:8080
- Frontend runs on: localhost:4200
- DB name: invoicedb | user: invoiceuser | pass: invoicepass | port: 5432

## Backend Coding Rules
- Always use Lombok annotations to reduce boilerplate (@Data, @Builder, @NoArgsConstructor, etc.)
- Use DTOs for all API requests and responses — never expose JPA entities directly
- Every endpoint must be prefixed with /api/v1/
- Use ResponseEntity for all controller return types
- Keep controllers thin — all business logic goes in the service layer
- Use constructor injection only, never @Autowired field injection
- One file at a time unless explicitly told otherwise
- Ask before making any business logic assumptions
- Do not over-engineer — keep it simple and clean
- Flyway migration files must follow naming convention: V{number}__{description}.sql

## Frontend Coding Rules
- Use Angular standalone components
- Use Angular reactive forms, not template-driven
- Use HttpClient with a base API service for all backend calls
- Use Angular Router with lazy-loaded feature modules
- Keep components dumb where possible — logic goes in services
- One component/service at a time unless explicitly told otherwise

## Build Order — Follow This Exactly

### PHASE 1 — DATABASE SCHEMA (Flyway migrations)
- V1 - users, workspaces, workspace_members
- V2 - clients
- V3 - invoices, invoice_line_items
- V4 - payments
- V5 - products (catalog)
- V6 - tax_rates
- V7 - expenses
- V8 - notifications
- V9 - settings

### PHASE 2 — BACKEND (for each feature: Entity → Repository → DTO → Service → Controller)
- 2a. Auth & Multi-tenancy: register, login, JWT, refresh tokens, password reset, workspace CRUD, invite members, roles (Owner/Admin/Member)
- 2b. Client Management: CRUD clients with profile (name, email, phone, billing address, currency)
- 2c. Invoice Management: CRUD invoices, line items, auto-calculate subtotal/tax/discount/total, invoice numbering (INV-0042), statuses (Draft→Sent→Paid→Overdue→Cancelled), duplicate invoice, send via email, PDF generation, overdue auto-detection background job
- 2d. Recurring Invoices: mark as recurring (weekly/monthly/quarterly/yearly), auto-generate next invoice on schedule, pause/cancel series
- 2e. Payments: Stripe integration, payment links, webhook handler, manual payment recording, partial payments, payment history
- 2f. Products & Services Catalog: CRUD reusable line items with default price
- 2g. Tax Management: CRUD tax rates, apply per line item or invoice, tax-inclusive vs tax-exclusive toggle
- 2h. Dashboard & Analytics: total revenue, outstanding amount, overdue count, recent activity, monthly revenue chart, top clients by revenue
- 2i. Notifications & Alerts: email reminders before due date, paid alerts, in-app notification center, overdue escalation
- 2j. Settings: business profile, invoice branding, default payment terms, default tax rate, SendGrid config
- 2k. Export & Reporting: CSV export for invoices and expenses, date range filter, P&L summary
- 2l. Expenses (stretch): log expenses, category/amount/date/receipt upload, profit view, CSV export

### PHASE 3 — ANGULAR FRONTEND (for each feature: service → component → routing)
- 3a. Auth pages: login, register, forgot password
- 3b. Dashboard page
- 3c. Client management pages
- 3d. Invoice list, create, detail pages
- 3e. Payment pages
- 3f. Products catalog pages
- 3g. Tax management pages
- 3h. Settings pages
- 3i. Notifications center
- 3j. Reports & export pages
- 3k. Expenses pages (stretch)

## Feature Breakdown

### Auth & Multi-tenancy
- Register / login / logout
- JWT-based auth with refresh tokens
- Workspace/org concept — one user can own a workspace, invite team members
- Role-based access: Owner, Admin, Member
- Password reset via email

### Client Management
- Create/edit/delete clients
- Client profile: name, email, phone, billing address, currency preference
- Client portal (stretch): read-only link for clients to view their invoices

### Invoice Management
- Create invoices with line items (description, quantity, unit price)
- Auto-calculated subtotal, tax, discount, total
- Invoice numbering (auto-increment, e.g. INV-0042)
- Due date and issue date
- Invoice status: Draft → Sent → Paid → Overdue → Cancelled
- Edit invoices (only allowed in Draft status)
- Duplicate an invoice
- Send invoice via email directly from the app
- PDF generation and download
- Overdue auto-detection via daily background job

### Recurring Invoices
- Mark an invoice as recurring (weekly / monthly / quarterly / yearly)
- Auto-generates next invoice on schedule
- Pause / cancel recurring series

### Payments
- Stripe integration — attach a payment link to each invoice
- Webhook handler — marks invoice as Paid when Stripe confirms
- Manual payment recording (cash, bank transfer, etc.)
- Partial payment tracking
- Payment history per invoice

### Products & Services Catalog
- Save reusable line items (e.g. "Consulting — $150/hr")
- Pull from catalog when building invoices

### Tax Management
- Create named tax rates (e.g. HST 13%, GST 5%)
- Apply one or multiple taxes per line item or per invoice
- Tax-inclusive vs tax-exclusive toggle

### Dashboard & Analytics
- Total revenue (this month, YTD)
- Outstanding (unpaid) amount
- Overdue invoices count + amount
- Recent activity feed
- Revenue chart (monthly bar chart)
- Top clients by revenue

### Notifications & Alerts
- Email reminder to client X days before due date
- Email alert to owner when invoice is paid
- In-app notification center
- Overdue escalation email

### Settings
- Business profile (name, logo, address, default currency)
- Invoice branding (logo on PDF, accent color, footer note)
- Default payment terms (Net 15, Net 30, etc.)
- Default tax rate
- SendGrid API key config

### Export & Reporting
- Export invoices to CSV
- Filter by date range, client, status
- Simple P&L summary export

### Expenses (Stretch)
- Log business expenses with category, amount, date, receipt upload
- Profit = Revenue - Expenses view
- Export expenses to CSVSonnet 4.6