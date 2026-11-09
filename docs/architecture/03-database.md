# 03 · Database

## Prinsip

- **Flyway adalah satu-satunya jalan** untuk mengubah skema. Hibernate hanya memvalidasi (`spring.jpa.hibernate.ddl-auto=validate`) — aplikasi menolak start bila entity ≠ skema. Harap dicatat bahwa mode `validate` Hibernate tidak memverifikasi seluruh konstrain database (seperti foreign key constraints).
- Migrasi ada di `src/main/resources/db/migration/`. Saat ini hanya `V1__initial_table.sql` (13 `CREATE TABLE`). Migrasi berikutnya wajib bernama `V2__...`, `V3__...`, berurutan.
- File `V1__initial_table.sql` juga di-mount ke `docker-entrypoint-initdb.d/` container MySQL (lihat `docker-compose.yml`) sehingga database baru otomatis terisi skema. Pastikan migrasi tertunda selalu diterapkan sebelum validasi skema oleh Hibernate.

## Daftar Tabel (13)

```
tanaman (inti — polybag PB-xx)
├── kondisi_tanaman        (status umum per tanaman)
├── kondisi_vegetatif      (tinggi, daun, fase tumbuh)
├── kondisi_kesehatan      (gangguan, tingkat keparahan)
├── log_pemeriksaan        (riwayat pemeriksaan rutin)
├── perlakuan              (induk aksi perawatan)
│   ├── perlakuan_penyiraman
│   ├── perlakuan_pemupukan
│   │   └── detail_bahan_pemupukan ──► inventaris_bahan (stok)
│   └── perlakuan_penyemprotan
│       └── detail_campuran_semprot
└── cuaca                  (snapshot cuaca yang disimpan)
inventaris_bahan           (stok bahan; SatuanStok)
```

## Entitas & Enum

- 13 class `@Entity` di `entity/`, memetakan 1:1 ke tabel di atas.
- 8 enum domain (tanpa `@Entity`): `BentukFisik`, `FaseTumbuh`, `JenisGangguan`, `MetodePupuk`, `SatuanStok`, `StatusUmum`, `TingkatKeparahan`, `TipeBahan`.
- Beberapa enum dipetakan ke kolom `VARCHAR`, tetapi ada yang menggunakan tipe native `ENUM` di MySQL (seperti `inventaris_bahan.tipe_bahan`). Jika definisi kolom MySQL berupa native `ENUM` dan belum mencakup nilai baru, pengembang **wajib** membuat skrip migrasi `ALTER TABLE` untuk memperbarui daftar nilai ENUM tersebut. Selalu periksa pula konsumennya (dropdown di frontend dan validasi di service layer).

## Aturan Main Saat Mengubah Skema

1. Buat migrasi baru `V2__<deskripsi>.sql` — **jangan pernah menyunting `V1__`** (Flyway checksum akan menolak start).
2. Sesuaikan entity Java + relasi.
3. Jalankan aplikasi; `validate` akan memberi tahu bila ada ketimpangan tipe/kolom. Lakukan pengecekan terpisah untuk memastikan konstrain foreign key dan indeks yang diperlukan sudah benar-benar terbentuk di skema database.
4. Ingat: `docker-entrypoint-initdb.d` hanya berjalan saat volume `mysql_data` **baru dibuat** — database lama di VPS hanya mengikuti Flyway.

## Hal yang Sengaja Belum Ada

- Tabel `pengguna`/user — autentikasi belum diimplementasikan (mockup login frontend-only, ADR-0005).
- Foreign key ON DELETE cascade seragam — periksa `V1__` sebelum menghapus data induk.
- Soft delete — `tanaman` memakai penanda aktif (`hanyaAktif=true` di API), bukan kolom `deleted_at`.
