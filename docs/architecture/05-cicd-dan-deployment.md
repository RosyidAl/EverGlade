# 05 · CI/CD & Deployment

## Dua Workflow (`.github/workflows/`)

| Workflow | Trigger | Runner | Kerja |
|---|---|---|---|
| `ci.yml` — Continuous Integration | push ke branch selain `master`, dan semua PR ke `master` | `ubuntu-latest` | Setup JDK 21 (Temurin) → build & test |
| `cd.yml` — Continuous Deployment | push ke `master`, dan manual (`workflow_dispatch`) | **self-hosted** (runner di VPS) | Build & deploy Docker, health check, prune |

**Kaidah penting: push ke `master` = deploy production.** Jangan pernah push WIP ke `master`. Keduanya punya `concurrency: production-deploy` agar deploy tidak tumpang tindih.

## Alur Deploy (`cd.yml`)

```
push master ──► self-hosted runner (service systemd
                 actions.runner.RosyidAl-EverGlade.fatisda-l0125065 di VPS)
                   │  actions/checkout (fetch langsung dari GitHub)
                   ▼
   docker compose up -d --build --remove-orphans
                   │
                   ▼
   health check: curl -sf http://127.0.0.1:8085/ (retry 30×2s)
                   │  gagal = workflow gagal (log app ditampilkan)
                   ▼
   docker image prune -f
```

Durasi khas ±3 menit. `checkout` dilakukan di workspace runner — folder `/root/projects/EverGlade` di VPS adalah salinan kerja terpisah yang tidak otomatis berubah.

## Dockerfile (multi-stage)

1. **Builder:** `eclipse-temurin:21-jdk-alpine` — `mvnw dependency:go-offline` (cache layer) → `mvnw clean package -DskipTests`.
2. **Runtime:** `eclipse-temurin:21-jre-alpine` — user non-root `everglade`, `EXPOSE 8080`, `HEALTHCHECK wget` internal, JVM `G1GC` + `MaxRAMPercentage=75`.

## docker-compose.yml

| Service | Image | Peran |
|---|---|---|
| `db` (`everglade-db`) | `mysql:8.4` | healthcheck `mysqladmin ping`; volume `mysql_data`; `V1__initial_table.sql` di-mount ke `docker-entrypoint-initdb.d/` (hanya efektif saat volume baru) |
| `app` (`everglade-app`) | build dari Dockerfile | `127.0.0.1:8085 -> 8080`; `depends_on: db (service_healthy)`; env `SPRING_DATASOURCE_*` menimpa properties |

Aplikasi hanya ter-expose ke **localhost VPS** (127.0.0.1). Akses publik HTTPS melewati Tailscale Funnel — detail di ADR-0001.

## Peta VPS (`root@100.112.123.101`)

```
/root/projects/EverGlade     ← clone repo untuk operasional compose
~/<runner>/                   ← workspace self-hosted runner (checkout CI)
nginx  :80                    ← reverse proxy / halaman lain milik host
docker: everglade-app (healthy), everglade-db (healthy)
```

## Kebersihan Deploy

- Health check CD memakai `curl -sf` → redirect 302 **lolos** (bukan gagal), error ≥400 gagal.
- `docker image prune -f` berjalan otomatis (`if: always()`) — image lama tidak menumpuk.
- Deploy bersifat deklaratif: seluruh keadaan berasal dari commit `master` + volume `mysql_data`. Tidak ada SSH manual yang dibutuhkan untuk deploy rutin.
