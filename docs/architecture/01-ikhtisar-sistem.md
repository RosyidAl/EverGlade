# 01 · Ikhtisar Sistem

## Bentuk Aplikasi

EverGlade adalah **monolit Spring Boot tunggal** yang sekaligus:

1. Melayani **API JSON** di `/api/**` (13 controller),
2. Melayani **frontend statis** (`src/main/resources/static/index.html`) sebagai welcome page di `/`,
3. Menjadi satu-satunya pembicara dengan database.

Tidak ada microservice, tidak ada gateway, tidak ada message queue. Deployment = satu image Docker + satu container MySQL.

## Stack

| Lapisan | Teknologi | Catatan |
|---|---|---|
| Runtime | Java 21 (Temurin), Maven Wrapper | `-XX:+UseG1GC`, `MaxRAMPercentage=75` di Docker |
| Framework | Spring Boot 4.1.1 | starter: web, data-jpa; TANPA security, TANPA thymeleaf |
| ORM | Hibernate 7 (via spring-data-jpa) | `ddl-auto=validate` — skema hanya boleh berubah lewat Flyway |
| Database | MySQL 8.4 | database `db_manajemen_tanaman` |
| Migrasi | Flyway | satu migrasi: `V1__initial_table.sql` |
| Frontend | Vanilla SPA (satu file) | Tailwind CDN, Three.js r128, Chart.js (vendored `js/chart.min.js`), font Quicksand |
| Integrasi eksternal | Open-Meteo | prakiraan cuaca, tanpa API key |

## Alur Data Utama

1. Browser memuat `/` → `index.html` (~5.100 baris) + CDN.
2. `init()` di index.html memanggil `fetch('/api/tanaman?hanyaAktif=true')` dan `fetch('/api/cuaca/current')`.
3. Controller menerima permintaan → (kadang via Service) → Repository (Spring Data JPA) → MySQL.
4. Response JSON dirender langsung ke DOM / tabel / chart tanpa framework.

## Port & Alamatan

| Konteks | Port | Keterangan |
|---|---|---|
| Lokal (`run.cmd` / `spring-boot:run`) | 8080 | default Spring Boot, tanpa context-path |
| Docker lokal/VPS (host) | 8085 | `127.0.0.1:8085 -> 8080` di docker-compose |
| Di dalam container | 8080 | `EXPOSE 8080` di Dockerfile |

## Aplikasi vs Data: Siapa Mempengaruhi Siapa

- **Skema database tidak pernah diubah Hibernate.** `ddl-auto=validate` membuat aplikasi menolak start bila entity dan skema Flyway tidak cocok. Aturan: setiap perubahan tabel = migrasi baru `V2__...`, `V3__...`, dst.
- **Frontend tidak punya state server.** Semua keadaan hidup di database + DOM; refresh halaman = ambil ulang semuanya.
- **Tidak ada autentikasi.** Semua endpoint `/api/**` terbuka; halaman login yang ada di `main` masih berupa mockup (lihat ADR-0005).
