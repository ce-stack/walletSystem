# Real-Time Wallet Transaction System

A fintech-style backend system built with Java Spring Boot to handle wallet creation, OTP-protected money transfers, ledger-based accounting, idempotency protection, and reliable async event publishing through RabbitMQ.

This project focuses on backend consistency, reliability, and financial transaction safety rather than UI work.

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Security
- JWT Authentication
- Spring Data JPA
- Hibernate
- PostgreSQL
- Flyway Migrations
- Docker Compose
- RabbitMQ
- Spring AMQP
- Scheduled Workers
- REST APIs

## Core Flow

```text
Register User
→ Receive JWT
→ Create Wallet
→ Start Transfer
→ Verify OTP
→ Execute Transfer
→ Create Transaction
→ Create Ledger Entries
→ Save Idempotency Key
→ Save Outbox Event
→ Publish Event to RabbitMQ
→ Consume Event
```

## Main Features

### Authentication

- User registration
- JWT-based access control
- Protected wallet and transfer APIs

### Wallet Management

- Create wallet for a user
- Store balance, phone number, currency, and wallet status
- Validate wallet ownership and wallet state before transfer

### OTP-Protected Transfer

- Transfer flow starts before final execution
- OTP verification is required before balance update
- Transfer succeeds only after successful OTP validation

### Transaction Safety

The transfer process runs inside a database transaction.

It handles:

- Balance validation
- Sender and receiver wallet checks
- Wallet locking
- Atomic balance update
- Rollback on failure

### Ledger-Based Accounting

Every successful transfer creates financial ledger records.

For transfer operations:

```text
Sender wallet   → DEBIT
Receiver wallet → CREDIT
```

This creates a clear audit trail and avoids relying only on the current wallet balance.

### Idempotency Keys

The transfer API supports idempotency keys to prevent duplicate transfers.

If the same request is retried with the same key:

- The transfer is not executed twice
- Duplicate balance deduction is prevented
- The original transaction result can be reused

### Outbox Pattern

After a successful transfer, an outbox event is saved in the same database transaction.

This prevents the common failure case where:

```text
Database commit succeeds
but message publishing fails
```

Outbox events move through:

```text
PENDING → PUBLISHED → FAILED
```

### RabbitMQ Async Processing

A scheduled worker reads pending outbox events and publishes them to RabbitMQ.

RabbitMQ then routes the event through:

```text
Exchange: wallet.events.exchange
Queue: transfer.completed.queue
Routing Key: transfer.completed
```

A consumer listens to the queue and processes the transfer completed event.

## Reliability Patterns Used

- Database transaction boundaries
- Wallet locking
- Ledger entries
- Idempotency key validation
- Outbox pattern
- Async message publishing
- Retry count tracking
- RabbitMQ consumer processing
- Flyway-controlled database schema

## Dockerized Runtime

The project can run without installing Java, PostgreSQL, or RabbitMQ locally.

Docker Compose starts:

- Spring Boot backend
- PostgreSQL database
- RabbitMQ broker
- RabbitMQ management dashboard

## Required Environment Variables

### Database

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/wallet_system
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=secret
```

### Flyway and JPA

```env
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
SPRING_FLYWAY_ENABLED=true
SPRING_FLYWAY_LOCATIONS=classpath:db/migration
```

### RabbitMQ

```env
SPRING_RABBITMQ_HOST=rabbitmq
SPRING_RABBITMQ_PORT=5672
SPRING_RABBITMQ_USERNAME=wallet
SPRING_RABBITMQ_PASSWORD=wallet123
```

### JWT

```env
JWT_SECRET=change-this-secret-to-a-long-secure-key
JWT_EXPIRATION_MS=86400000
```

## Run With Docker

```bash
docker compose up --build
```

The services will be available at:

```text
Backend API: http://localhost:8080
RabbitMQ UI: http://localhost:15672
PostgreSQL: localhost:5432
```

RabbitMQ login:

```text
Username: wallet
Password: wallet123
```

## API Flow For Testing

### 1. Register

```text
POST /api/auth/register
```

### 2. Create Wallet

```text
POST /api/wallets/create
Authorization: Bearer <JWT>
```

### 3. Start Transfer

```text
POST /api/wallets/transfer?idempotency_keys=<unique-key>
Authorization: Bearer <JWT>
```

### 4. Verify OTP

```text
POST /api/otp/verify?idempotency_keys=<same-key>
Authorization: Bearer <JWT>
```

## What To Verify After A Successful Transfer

Check the database tables:

```text
transactions
ledger_entries
idempotency_keys
outbox_events
```

Expected result:

- One transaction record
- Two ledger entries
- One idempotency key
- One outbox event marked as `PUBLISHED`
- RabbitMQ consumer receives the transfer event

## Project Scope

This is not a production banking system.

It is a backend portfolio project designed to demonstrate fintech-style consistency patterns, reliable money movement, auditability, duplicate request protection, and asynchronous event processing.
