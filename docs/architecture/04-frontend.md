# 04 · Frontend

## Satu File, Nol Build Step

Seluruh UI adalah **`src/main/resources/static/index.html`** (~5.100 baris, ~257 KB): HTML + `<style>` inline + `<script>` inline. Dinyajikan Spring Boot sebagai static resource; tidak ada Thymeleaf, tidak ada bundler, tidak ada npm.

Aset lain hanya `static/js/chart.min.js` (Chart.js vendored) dan tiga CDN: Tailwind (JIT di browser), Three.js r128, Google Fonts Quicksand. **Konsekuensi: halaman butuh internet saat pertama kali dibuka.**

## Anatomi `index.html`

| Bagian | Lokasi (kira-kira) | Isi |
|---|---|---|
| `<style> :root` | baris 24–81 | **Design token** — sumber kebenaran visual |
| Kelas kaca (`.glass`, `.glass-panel`, ...) | 424–477 | Komponen glassmorphism |
| `<nav>` segmented pill | ~549 | Navigasi 3 tab utama |
| `#mgmt-view` | ~581 | Tab 2: workstation CRUD tanaman |
| `#analytics-view` | ~959 | Tab 3: analitik layar penuh |
| `#canvas-container` + HUD | ~1196 | Tab 1: kanvas Three.js + `.app-dock` + `.climate-card` |
| `init()` / `window.onload` | ~1412 / ~5110 | Bootstrap scene + sinkronisasi data |
| `switchMainTab(tab)` | ~4724 | **Satu chokepoint navigasi** |
| Keyboard 1/2/3 | ~4787 | Shortcut tab |

## Navigasi

Bukan routing URL — tiga `<section>` saudara di-toggle lewat kelas Tailwind `hidden`:

- `switchMainTab('3d')`, `('mgmt')`, `('analytics')` menukar kelas tombol aktif, menampilkan section, dan memicu renderer per tab (`renderManagementView()`, `refreshAnalyticsData()`).
- **Aturan menambah tab/halaman baru:** tambah tombol di `<nav>` + section sibling + cabang di `switchMainTab()` — jangan membuat halaman HTML terpisah (lihat ADR-0003/0004).
- **Halaman login** (`login.html`) adalah pengecualian: halaman statis terpisah dengan token yang disalin dari `:root` ini (ADR-0005).

## Design System "Clear Glass"

- Token di `:root` (`--glass-hud`, `--glass-panel`, `--glass-blur`, `--accent: #1e7247`, `--radius-card: 22px`, ...). Header komentar `<style>` mendokumentasikan standar Hallmark (kontras ink/glass terburuk 8.0:1, kaca clear adalah arahan eksplisit pemilik).
- **Kaidah:** warna TIDAK ditulis langsung — selalu lewat `var(--token)` atau arbitrary value Tailwind yang mereferensi token (`bg-[color:var(--glass-hud)]`).
- Mode malam = kelas `body.glass-night` yang mengalihkan beberapa token ke varian `*-night`.
- Font: Quicksand 4 bobot dengan peran tetap (400 teks, 500 label, 600 tombol, 700 angka).

## Titik Wiring yang Wajib Dijaga Saat Mengubah UI

1. `switchMainTab()` — semua pergantian view lewat sini.
2. `init()` — bootstrap Three.js + fetch data pertama; `window.onload` memanggilnya.
3. ID elemen dinamis yang di-update JS (contoh: `#weather-humidity-header`, `#modal-plant-height`, `#tab-mgmt-count`) — jangan di-rename tanpa mencari pemakaiannya di script.
4. Data flow selalu `fetch('/api/...')` same-origin; tidak ada axios, tidak ada state library.

## Interaksi 3D

- Scene dibangun dengan Three.js r128: polybag (`PB-xx`) sebagai mesh yang bisa diklik, slot kosong memicu modal tanam cepat (2 input: Nama + Varietas — ADR-0003).
- Kamera/kontrol kaca: `.app-dock` (sidebar collapse), `.climate-card` (telemetri cuaca).
- Chart.js hanya di `#analytics-view`; diinisialisasi lewat `refreshAnalyticsData()`.
