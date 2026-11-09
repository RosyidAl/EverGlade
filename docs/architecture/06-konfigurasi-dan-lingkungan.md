# 06 · Konfigurasi & Lingkungan

## Sumber Konfigurasi (urutan kemenangan Spring Boot)

1. **Argumen Command Line** (misalnya `--spring.datasource.url=...`) — memiliki prioritas tertinggi.
2. **Variabel Environment sistem / OS** (termasuk dari `docker-compose.yml`, misalnya `SPRING_DATASOURCE_URL`).
3. **Konfigurasi spesifik profil** (`application-<profil>.properties`).
4. **`application.properties` bawaan** — konfigurasi dasar.

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
| `DB_PASSWORD` | `everglade_db_secret` | ⚠️ Inisialisasi akun MySQL hanya berlaku saat volume dibuat pertama kali. Untuk database lama, operator harus merotasi password akun MySQL dan menyinkronkannya dengan `.env` sebelum menjalankan Compose. |
| `APP_PORT` | `8085` | port host untuk container app |
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://db:3306/...` | host `db` = nama service compose; `useSSL=false`, `allowPublicKeyRetrieval=true`, TZ Jakarta |

Untuk keamanan di server produksi, kredensial produksi sebaiknya dimuat dari sumber persisten di luar checkout workspace runner (bukan sekadar fallback membuat `.env` dari `.env.example`), memastikan container aplikasi dan database tetap memakai kredensial yang sinkron di setiap siklus checkout/deploy.

## Checklist Lingkungan Baru (mis. laptop anggota tim baru)

1. Java 21 (`JAVA_HOME` mengarah ke JDK 21 — lihat `run.cmd`).
2. MySQL lokal berjalan; buat database `db_manajemen_tanaman`.
3. Salin `.env.example` menjadi `.env` dan isi kredensial MySQL lokal Anda.
4. Jalankan `sh ./mvnw spring-boot:run` dari root repositori (Linux/macOS) atau `mvnw.cmd spring-boot:run` / `run.cmd` (Windows) → buka `http://localhost:8080`.
5. Flyway akan otomatis menerapkan `V1__` bila database masih kosong.

## Keamanan: Status Saat Ini

- **Tanpa Spring Security** — semua `/api/**` terbuka; halaman login masih mockup frontend-only (ADR-0005).
- Kredensial MySQL lokal kini dikonfigurasi melalui placeholder variabel environment di `application.properties` dan file lokal `.env` (gitignored).
- ⚠️ Terdapat kredensial committed pada `application-azure.properties` (`spring.datasource.username` dan `password`); jika profil ini aktif atau dipakai, kredensial tersebut wajib dihapus dan dirotasi.
- Kredensial weather tidak diperlukan (Open-Meteo gratis tanpa key).
