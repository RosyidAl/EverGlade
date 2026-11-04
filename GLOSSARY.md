# EverGlade Domain

Sistem manajemen tanaman dan pemantauan mikroklimat kebun cabai rawit berbasis antarmuka 3D interaktif.

## Interface & HUD Language

**HUD (Heads-Up Display)**:
Lapisan antarmuka kaca bening transparan yang mengambang di atas canvas 3D kebun untuk kontrol dan pemantauan cepat tanpa menutupi visual tanaman.
_Avoid_: UI Overlay, Dashboard view, Screen menu

**Dock**:
Kluster tombol aksi cepat dan kontrol kamera yang mengambang di bagian bawah layar.
_Avoid_: Bottom bar, Toolbar, Footer menu

**Action Trigger**:
Elemen interaktif pada Dock untuk memicu aksi fisik operasional kebun (penyiraman air, pemberian nutrisi cair, semprot anti-hama).
_Avoid_: Button, Action icon, Task clicker

**Telemetry Tile**:
Kartu indikator metrik mikroklimat (suhu, kelembaban udara, kelembaban tanah, intensitas cahaya) yang menampilkan nilai sensor secara langsung.
_Avoid_: Sensor card, Metric box, Stat widget

**Plant Node**:
Representasi 3D dan entitas data profil untuk setiap tanaman cabai rawit yang dipantau dalam sistem.
_Avoid_: Plant object, Item, Record

**Tab View Navigasi**:
Struktur navigasi utama tingkat atas yang membagi konsol kebun menjadi dua mode tampilan fokus: *Kebun 3D* (visualizer interaktif lapang) dan *Manajemen Tanaman* (dashboard kerja berbasis tabel data operasional).
_Avoid_: Page switch, Multi-window, Screen tab

**Alokasi Polybag Otomatis**:
Mekanisme pengikatan cerdas yang secara otomatis mencarikan dan menetapkan slot nomor polybag kosong (PB-01 s/d PB-40) saat pengguna menambahkan tanaman cabai baru tanpa mewajibkan input manual.
_Avoid_: Auto slot, ID picker, Manual polybag entry
