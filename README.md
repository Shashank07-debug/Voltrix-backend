# Voltrix

Voltrix is a Spring Boot–based SaaS "AI website builder," inspired by Lovable.dev. A user describes what they want in a prompt, and Voltrix generates a complete, working React frontend for them — with project management, real-time chat-driven generation, team collaboration, and Stripe-powered billing built around it.

> **Status:** Backend is functionally complete for the core flows below. Frontend / UI is not part of this repo yet.

---

## What's implemented so far

### 🔐 Authentication & Authorization
- JWT-based stateless authentication (signup, login, `me` profile endpoint)
- Custom `JwtAuthFilter` + Spring Security filter chain (`WebSecurityConfig`)
- Role/permission model for project-level access control (`ProjectRole`, `ProjectPermission`, `SecurityExpressions`)
- Method-level security via `@EnableMethodSecurity`

### 📁 Projects & Files
- Full CRUD for projects (`ProjectController`, `ProjectService`)
- Project file tree storage and retrieval, backed by MinIO (S3-compatible object storage) for generated file contents (`FileController`, `ProjectFileService`, `StorageConfig`)
- Project templating support (`ProjectTemplateService`)

### 👥 Team Collaboration
- Invite, update role, and remove project members (`ProjectMemberController`, `ProjectMemberService`)
- Composite-key entities for project membership (`ProjectMemberId`)

### 🤖 AI Code Generation
- Chat-driven, streaming code generation using Spring AI + OpenAI models (`ChatController`, `AiGenerationService`, Server-Sent Events via `text/event-stream`)
- Custom XML-tag-based agent protocol for the LLM to emit generated files (`LlmResponseParser`, `PromptUtils`, `llm/tools/CodeGenerationTools`)
- File-tree context advisor so the model is aware of existing project files when generating/editing (`FileTreeContextAdvisor`)
- Chat session/message/event persistence for full conversation history (`ChatSession`, `ChatMessage`, `ChatEvent`)
- Generated stack target: React 18 + TypeScript + Vite + Tailwind CSS 4 + daisyUI v5

### 💳 Billing & Subscriptions (Stripe)
- Plan listing and current subscription lookup (`BillingController`, `PlanService`, `SubscriptionService`)
- Stripe Checkout session creation and customer billing portal links (`StripePaymentProcessor`, `PaymentProcessor`, `PaymentConfig`)
- Stripe webhook endpoint for payment events (`/webhooks/payment`)

### 📊 Usage Tracking
- Daily usage metering and plan-limit checks (`UsageController`, `UsageService`, `UsageLog`)

### 🛠 Cross-cutting
- Centralized error handling with a consistent API error shape (`GlobalExceptionHandler`, `ApiError`, custom exceptions)
- MapStruct-based DTO ↔ entity mapping across all modules
- OpenAPI/Swagger docs auto-exposed via springdoc (`/v3/api-docs`, `/swagger-ui.html`)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0.0 |
| Data | Spring Data JPA + Hibernate, PostgreSQL (via `pgvector` image) |
| Security | Spring Security 7, JWT (`jjwt`) |
| AI | Spring AI 2.0.0-M1, OpenAI models |
| Object storage | MinIO (S3-compatible) |
| Payments | Stripe Java SDK |
| API docs | springdoc-openapi (OpenAPI 3 + Swagger UI) |
| Build | Maven |

---

## Getting Started

### Prerequisites
- Java 21
- Maven (or use the bundled `./mvnw`)
- Docker (for local Postgres + MinIO)

### 1. Start local infrastructure

```bash
docker compose -f services.docker-compose.yml up -d
```

This starts:
- **PostgreSQL** (`pgvector` image) on port `9010` → maps to container port `5432`
- **MinIO** on port `9000` (S3 API) and `9001` (web console)

### 2. Configure application properties

`src/main/resources/application.properties` is git-ignored (it holds secrets/local config) — create it yourself with your DB, MinIO, Stripe, and OpenAI credentials before running the app.

### 3. Run the app

```bash
./mvnw spring-boot:run
```

The app starts on `http://localhost:8082` by default.

### 4. Explore the API

- Swagger UI: `http://localhost:8082/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8082/v3/api-docs`

---

## API Overview

| Area | Base path | Notes |
|---|---|---|
| Auth | `/api/auth/**` | signup, login, profile — public |
| Projects | `/api/projects/**` | CRUD |
| Project files | `/api/projects/{projectId}/files/**` | file tree & content |
| Project members | `/api/projects/{projectId}/members/**` | invite/manage collaborators |
| Chat / generation | `/api/chat/stream`, `/projects/{projectId}` | SSE-streamed AI code generation |
| Billing | `/api/plans`, `/api/me/subscription`, `/api/payments/**` | Stripe checkout & portal |
| Webhooks | `/webhooks/**` | Stripe event ingestion — public |
| Usage | `/api/usage/**` | daily usage & plan limits |

All routes other than `/api/auth/**`, `/webhooks/**`, and the Swagger/OpenAPI endpoints require a valid JWT.

---

## Project Structure

```
src/main/java/com/shash/projects/lovable_clone/
├── config/         # AI, payment, and storage bean configuration
├── controller/     # REST controllers
├── dto/            # Request/response DTOs, grouped by domain
├── entity/         # JPA entities
├── enums/          # Domain enums
├── error/          # Global exception handling
├── llm/            # LLM prompt/response handling and code-gen tools
├── mapper/         # MapStruct mappers
├── repository/     # Spring Data JPA repositories
├── security/       # JWT auth filter, security config
└── service/        # Business logic (+ impl/ for implementations)
```

---

## Roadmap / Not yet done

- Frontend UI for Voltrix itself (the builder's own dashboard/chat interface)
- Deployment/CI setup
- Automated test coverage

---

## License

Not yet specified.
