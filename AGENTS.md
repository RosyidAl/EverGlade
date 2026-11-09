# AGENTS.md — EverGlade Project Guidelines

Guidelines, architecture context, and workflows for AI agents working in this repository.

---

## 1. Project Overview & Architecture

- **Project**: EverGlade (Plant Management System / Sistem Manajemen Tanaman)
- **Backend Application**:
  - **Package**: `com.everglade.app`
  - **Framework**: Spring Boot 4.x / Java 21
  - **Data Access**: Spring Data JPA with MySQL (`db_manajemen_tanaman`)
  - **Schema Management**: Flyway DB migrations (`src/main/resources/db/migration/`)
  - **Frontend / Static UI**: `src/main/resources/static/` (HTML/JS web interface)
  - **Configuration**: Environment variables via `.env` (lihat `.env.example`). Maven tidak otomatis memuat `.env`; saat menjalankan Maven secara manual di terminal, ekspor variabel terlebih dahulu (mis. `export DB_PASSWORD=...` atau jalankan script launcher).
- **Agent Customizations**:
  - **Workspace Skills**: `.agents/skills/`
  - **Vendor Repositories**: `.agents/vendor/`
  - **Core Design Standard**: `antislop.md`

---

## 2. Common Development Workflows & Commands

Execute backend commands from the project root directory:

- **Compile**:
  - Linux/WSL: `./mvnw clean compile`
  - Windows: `mvnw.cmd clean compile`
- **Run Tests**:
  - Linux/WSL: `./mvnw test`
  - Windows: `mvnw.cmd test`
- **Run Application**:
  - Linux/WSL: `./mvnw spring-boot:run`
  - Windows: `mvnw.cmd spring-boot:run` or `./run.cmd` / `./start-app.ps1`
- **Run via Docker** (includes MySQL):
  - `docker compose up`
  - Catatan: Pengaturan default menggunakan user `root`. Bila menggunakan non-root `DB_USER` pada volume baru, pastikan user tersebut telah dibuat dan diberikan hak akses penuh ke schema `db_manajemen_tanaman`.
- **Package JAR**:
  - `./mvnw clean package -DskipTests`
- **Database Migrations**:
  - Database schema changes **must** be implemented via new Flyway migration scripts in `src/main/resources/db/migration/` using the versioning format: `V{version}__{description}.sql` (e.g. `V2__add_sensor_data.sql`).
  - Do **not** rely on `hibernate.ddl-auto=update`; `ddl-auto` is set to `validate`.

---

## 3. Code & Engineering Standards

- **Java 21 Idioms**: Use modern Java features appropriately (records for DTOs, sealed interfaces, pattern matching, switch expressions, var for local variables when types are obvious).
- **Layered Architecture**: Respect Spring Boot separation of concerns:
  - Controllers (`@RestController`) handle HTTP mapping and validation.
  - Services (`@Service`) hold business logic and transaction boundaries (`@Transactional`).
  - Repositories (`@Repository` / `JpaRepository`) handle persistence queries.
- **Documentation Integrity**: Preserve all existing comments, docstrings, and tests unless explicitly instructed to modify them.
- **Clickable File Links**: Always use Markdown file links with full paths or relative file links when referencing code in responses.

---

## 4. Skills & Progressive Disclosure

Workspace skills are located in `.agents/skills/`. The agent loads skill descriptions on demand to keep context lean:

- **`antislop`** (`.agents/skills/antislop/` & sub-skills): Clean, humane UI, copy, mobile layouts, and code comments without AI slop.
- **`dashboard-design`** (`.agents/skills/dashboard-design/`): Data-dense, clean dashboard UI, charts, metrics, and KPI cards.
- **`hallmark`** (`.agents/skills/hallmark/`): High-craft web page design guidelines.
- **Global Skills**: Loaded automatically from `~/.gemini/config/skills/` and `~/.agents/skills/` (e.g., `code-review`, `diagnosing-bugs`, `tdd`, `domain-modeling`).

---

<!-- antislop:start -->
## antislop
For UI, copy, people, mobile layout, or code comments work, read `antislop.md` (core) and then the skill for the task:
- UI / visual: `.agents/skills/antislop-ui/SKILL.md`
- Copy & text: `.agents/skills/antislop-copywriting/SKILL.md`
- People: `.agents/skills/antislop-human/SKILL.md`
- Mobile / responsive: `.agents/skills/antislop-layoutmobile/SKILL.md`
- Code comments: `.agents/skills/antislop-code/SKILL.md`
Before starting, follow the core's "Two Usage Modes" section in strict order: explicit session instruction first, then global preference, then ask. A session instruction always wins. For a resolved mode, say `antislop active: <mode> (session override).` or `antislop active: <mode> (global preference).` once before presenting findings or making edits, using the actual mode and source. Acknowledging the user's request without naming the source does not replace this notice.
Only an explicit choice of antislop during or after selects a session mode. A request to review, audit, or avoid file edits does not select a mode; read the global preference in that case. Another skill's mode does not select antislop's mode.
If the mode is unresolved, ask during/after and end the response; wait for the answer before any UI review, planning, or concept. For read-only tasks, put the active-mode notice only at the start of the final answer, never in progress messages. For editing tasks, announce before the first edit and omit it from the final answer.
To update antislop later: download `antislop.md` again, or run `npx antislop-ai --update` if it was installed as skill folders.
<!-- antislop:end -->
