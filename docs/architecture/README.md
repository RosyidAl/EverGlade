# Arsitektur EverGlade

Dokumentasi arsitektur sistem manajemen tanaman dan pemantauan mikroklimat kebun cabai rawit. Seluruh isi di sini dijabarkan langsung dari kode (`demo/`) — bukan rencana.

## Peta Dokumen

| Dokumen | Isi |
|---|---|
| [01 — Ikhtisar Sistem](01-ikhtisar-sistem.md) | Stack, bentuk aplikasi, dan cara semua bagian tersambung |
| [02 — Backend](02-backend.md) | Layer Spring Boot: controller → service → repository, exception, DTO |
| [03 — Database](03-database.md) | MySQL, Flyway, 13 tabel, dan hubungan antar entitas |
| [04 — Frontend](04-frontend.md) | SPA satu file, design system "clear glass", navigasi tab, titik wiring JS |
| [05 — CI/CD & Deployment](05-cicd-dan-deployment.md) | GitHub Actions, self-hosted runner, Docker, VPS |
| [06 — Konfigurasi & Lingkungan](06-konfigurasi-dan-lingkungan.md) | Properties, profil Azure, variabel compose, port |

## Gambar Besar

```
                Browser (1 tab SPA: index.html)
                        │  fetch /api/**  (JSON, same-origin)
                        ▼
┌─────────────────────────────────────────────────────┐
│  Spring Boot 4.1.1 · Java 21  (port 8080)           │
│                                                     │
│  @RestController /api/** ──► Service (4 buah)       │
│        │      \────────────► Repository (13)        │
│        ▼                     │                      │
│  WeatherService ──► api.open-meteo.com (eksternal)  │
│                              ▼                      │
│                    MySQL 8.4 (Flyway: V1)           │
└─────────────────────────────────────────────────────┘
```

- **Satu proses backend monolit.** Halaman pembuka `/` me-redirect ke `login.html` (halaman mockup login & registrasi), lalu beralih ke UI dashboard utama di `src/main/resources/static/index.html`. Tidak ada template engine dan tidak ada build step frontend.
- **API murni JSON** di bawah `/api/**`; halaman dilayani Spring Boot sebagai static resource.
- **Integrasi server eksternal:** backend mengintegrasikan Open-Meteo untuk telemetri cuaca. Sementara itu, browser memuat dependensi eksternal via CDN (Tailwind, Three.js, Google Fonts — rincian lengkap di [04 — Frontend](04-frontend.md)).

## Cara Membaca Kode Pertama Kali

1. `EverGladeApplication.java` — titik masuk.
2. `controller/TanamanController.java` — contoh pola API paling lengkap.
3. `db/migration/V1__initial_table.sql` — seluruh skema database.
4. `static/index.html` — cari `switchMainTab()` untuk navigasi dan `init()` untuk bootstrap.
5. `docs/adr/` — keputusan desain yang sudah final (jangan diubah isinya, tambah ADR baru).

## Konvensi Penamaan

Kode mengikuti istilah domain di `GLOSSARY.md` (HUD, Dock, Action Trigger, Telemetry Tile, Plant Node). Entitas/tabel memakai bahasa Indonesia lowercase (`tanaman`, `perlakuan_penyiraman`), class Java memakai nama domain yang sama (`Tanaman`, `PerlakuanPenyiraman`).
