# 5. Login Page Mockup (Frontend-Only Authentication Flow)

## Context
The team is iterating on the UI redesign and identified that the application lacks any authentication surface: there is no user entity, no Spring Security dependency, and every `/api/**` endpoint is open. Rather than blocking UI work on an auth backend that has not been designed yet, we ship a **login page mockup** now so the entry experience can be reviewed by the team and stakeholders early.

## Decision
1. **Standalone static page** — a new self-contained `src/main/resources/static/login.html` (served at `/login.html` by Spring Boot's static handler). It reuses the clear-glass design tokens copied verbatim from `index.html` (`:root` block) so the page reads as part of the same product. `index.html` is not modified.
2. **Login as the entry point** — a minimal `HomeController` redirects `GET /` to `/login.html`. The application itself lives at `/index.html`, so the mockup flow is `/` → login → Masuk → app. The CD health check (`curl -sf http://127.0.0.1:8085/`) is unaffected: curl treats a 302 as success.
3. **Mockup scope** — the page contains two tabs (`Masuk`, `Daftar`) with client-side visual validation only. Submitting simulates a short loading state and redirects to `/index.html`. **No fetch calls to `/api/**`, no session/cookie, no persistence.** The "Lupa password?" link shows a toast stating it is not implemented.
4. **Language & materiality** — UI copy in Indonesian; Quicksand typography, green accent, glass-panel card consistent with the Hallmark design system.

## Consequences
- Teammates hitting `/` will now land on the login mockup first — this is intentional, but should be communicated.
- Real authentication later will require: `spring-boot-starter-security`, a `Pengguna` entity + `V2__create_pengguna.sql` Flyway migration, a service/controller following the existing `entity → repository → service → controller` pattern, and replacing the mockup's redirect with a real POST (e.g. `/api/pengguna/masuk`). The visual design of this page is expected to survive that transition largely unchanged.
