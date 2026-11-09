# 02 · Backend

## Struktur Paket

```
src/main/java/com/everglade/app/
├── EverGladeApplication.java // titik masuk
├── controller/               // 13 @RestController (/api/**) + 1 @Controller (HomeController)
├── service/                  // 4 service: Tanaman, InventarisBahan, LogPemeriksaan, Weather
├── repository/               // 13 interface Spring Data JPA
├── entity/                   // 13 @Entity + 8 enum domain (tanpa @Entity)
├── dto/
│   ├── request/              // model payload masukan
│   └── weather/              // model respons Open-Meteo
├── exception/
│   └── GlobalExceptionHandler.java
└── util/
    └── WeatherCodeTranslator.java  // kode cuaca WMO → label Indonesia
```

## Daftar Controller

| Controller | Base Mapping | Keterangan |
|---|---|---|
| `TanamanController` | `/api/tanaman` | CRUD inti + `/{id}?keterangan=PANEN` (aksi panen) |
| `CuacaController` | `/api/cuaca` | `/current` mem-proxy Open-Meteo |
| `LogPemeriksaanController` | `/api/log-pemeriksaan` | + `/tanaman/{tanamanId}` |
| `InventarisBahanController` | `/api/inventaris-bahan` | stok bahan |
| `KondisiTanamanController` | `/api/kondisi-tanaman` | |
| `KondisiVegetatifController` | `/api/kondisi-vegetatif` | |
| `KondisiKesehatanController` | `/api/kondisi-kesehatan` | |
| `PerlakuanController` | `/api/perlakuan` | |
| `PerlakuanPenyiramanController` | `/api/perlakuan-penyiraman` | |
| `PerlakuanPemupukanController` | `/api/perlakuan-pemupukan` | |
| `PerlakuanPenyemprotanController` | `/api/perlakuan-penyemprotan` | |
| `DetailBahanPemupukanController` | `/api/detail-bahan-pemupukan` | |
| `DetailCampuranSemprotController` | `/api/detail-campuran-semprot` | |

## Pola Lapisan (dan Realitanya)

**Pola ideal:** `Controller → Service → Repository → Entity`.

**Realitanya di kode ini (penting dipahami sebelum menambah fitur):**

- Hanya **4 service** yang ada: `TanamanService`, `InventarisBahanService`, `LogPemeriksaanService`, `WeatherService`. Service dipakai ketika ada logika bisnis lintas tabel (validasi stok, komputasi status, dll).
- **9 controller lain adalah REST controller (`@RestController`) yang meng-autowire Repository langsung** (mis. `KondisiTanamanController` memegang `KondisiTanamanRepository`) — CRUD murni tanpa aturan bisnis. Ini disengaja agar CRUD sederhana tetap tipis. Selain itu, terdapat `HomeController` (`@Controller`) yang menangani `GET /` dengan me-redirect ke `/login.html` tanpa memerlukan service maupun repository.
- **Kaidah menambah fitur:**
  - CRUD polos → controller + repository (ikuti pola yang ada).
  - Ada aturan bisnis / lintas tabel → buat/ekspansi service, jangan menaruh logika di controller.
  - Gunakan `dto/request` untuk payload masukan; jangan bind entity langsung dari JSON.

## Penanganan Error

`exception/GlobalExceptionHandler.java` memetakan exception ke JSON:

| Exception | HTTP |
|---|---|
| `ResourceNotFoundException` | 404 |
| `InsufficientStockException` | 400 (stok kurang; dipakai alur pemupukan/penyemprotan) |
| `BusinessValidationException` | 400 |
| `IllegalArgumentException` | 400 |
| `Exception` (catch-all) | **500** |

**Catatan yang diketahui:** catch-all juga menangkap `NoResourceFoundException` (URL statis tidak ada), sehingga salah ketik path menghasilkan 500 alih-alih 404. Diketahui dan sengaja didokumentasikan; perbaikan kecil bisa berupa `@ExceptionHandler(NoResourceFoundException.class)` → 404.

## Konvensi Kode

- Field injection dengan `@Autowired` (konsisten dengan kode lama — jangan campur gaya di satu file).
- Javadoc **berbahasa Indonesia** dengan `@param`/`@return` (lihat `TanamanController`).
- Nama class = nama domain Indonesia: `Tanaman`, `PerlakuanPenyiraman`, `DetailBahanPemupukan`.
- Enum domain (`FaseTumbuh`, `TingkatKeparahan`, `MetodePupuk`, ...) hidup di paket `entity` tanpa `@Entity`.
