# 06 · Konfigurasi & Lingkungan

## Sumber Konfigurasi (urutan kemenangan Spring)

1. **Variabel environment** (dari `docker-compose.yml`) — paling kuat di deployment.
2. **Profil aktif** (`application-<profil>.properties`).
3. **`application.properties`** — basis.

## `application.properties` (basis, dipakai saat dev lokal)

| Properti | Nilai | Catatan |
|---|---|---|
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/db_manajemen_tanaman` | MySQL lokal |
| `spring.datasource.username/password` | root + password | ⚠️ password tertanam plaintext di repo — utang kebersihan: pindahkan ke env var |
| `spring.jpa.hibernate.ddl-auto` | `validate` | JANGAN diubah ke `update`/`create` — skema hanya lewat Flyway |
| `spring.jpa.properties.hibernate.dialect` | `MySQLDialect` | redundan (Hibernate 7 pilih otomatis, ada WARN kecil) |
| `weather.api.base-url` | `https://api.open-meteo.com/v1` | dipakai `WeatherService` |
| `weather.default.latitude/longitude` | lokasi kebun default | |
| `weather.default.timezone` | `Asia/Jakarta` | |

Tidak ada `server.port` (default 8080) dan tidak ada context-path.

## `application-azure.properties` (profil cloud)

Skema sama, tetapi URL ke `jdbc:mysql://mysql-project.mysql.database.azure.com:3306/...?sslMode=REQUIRED` dan hanya `ddl-auto=validate` (kredensial memakai env var di runtime Azure). Aktifkan dengan `--spring.profiles.active=azure`.

## Variabel di `docker-compose.yml`

| Env | Default | Keterangan |
|---|---|---|
| `DB_NAME` | `db_manajemen_tanaman` | dipakai db & app |
| `DB_USER` | `root` | |
| `DB_PASSWORD` | `everglade_db_secret` | ⚠️ default dev; timpa lewat `.env`/environment VPS |
| `APP_PORT` | `8085` | port host untuk container app |
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://db:3306/...` | host `db` = nama service compose; `useSSL=false`, `allowPublicKeyRetrieval=true`, TZ Jakarta |

Workflow `cd.yml` membuat `.env` dari `.env.example` bila belum ada — sehingga password produksi bisa diisi di VPS tanpa menyentuh repo.

## Checklist Lingkungan Baru (mis. laptop anggota tim baru)

1. Java 21 (`JAVA_HOME` mengarah ke JDK 21 — lihat `run.cmd`).
2. MySQL lokal berjalan; buat database `db_manajemen_tanaman`.
3. Isi kredensial di `application.properties` (atau env var).
4. `mvnw spring-boot:run` (atau `demo\run.cmd` di Windows) → buka `http://localhost:8080`.
5. Flyway akan otomatis menerapkan `V1__` bila database masih kosong.

## Keamanan: Status Saat Ini

- **Tanpa Spring Security** — semua `/api/**` terbuka; halaman login masih mockup frontend-only (ADR-0005).
- Satu kredensial plaintext ada di `application.properties` (repo). Direkomendasikan (PR kecil): pindahkan ke env var + beri nilai default kosong, dan rotasi password database.
- Kredensial weather tidak diperlukan (Open-Meteo gratis tanpa key).
