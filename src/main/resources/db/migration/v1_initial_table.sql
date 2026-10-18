CREATE DATABASE IF NOT EXISTS db_manajemen_tanaman;
USE db_manajemen_tanaman;

-- ==========================================
-- 1. ENTITAS MASTER & REFERENSI MANDIRI
-- ==========================================

-- Tabel Utama Tanaman
CREATE TABLE tanaman (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nama_tanaman VARCHAR(100) NOT NULL,
    jenis_varietas VARCHAR(100),
    lokasi_tanam VARCHAR(100),
    tanggal_tanam DATE NOT NULL,
    status_aktif BOOLEAN NOT NULL DEFAULT TRUE,
    catatan TEXT
) ENGINE=InnoDB;

-- Tabel Cuaca (Diambil dari Weather API / Mandiri)
CREATE TABLE cuaca (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    waktu_catat DATETIME NOT NULL,
    kondisi VARCHAR(50) NOT NULL, -- Contoh: Clear, Rain, Cloudy
    suhu_celsius DECIMAL(4,1) NOT NULL,
    kelembapan_persen INT NOT NULL,
    curah_hujan_mm DECIMAL(5,2) DEFAULT 0.00,
    kecepatan_angin_kmh DECIMAL(5,2) DEFAULT 0.00
) ENGINE=InnoDB;

-- Tabel Inventaris Bahan (Pupuk, Pestisida, Perekat)
CREATE TABLE inventaris_bahan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nama_produk VARCHAR(100) NOT NULL,
    tipe_bahan ENUM('PUPUK', 'PESTISIDA', 'ADJUVAN_PEREKAT') NOT NULL,
    kategori_detail VARCHAR(50), -- Contoh: Akarisida, Fungisida, POC, NPK
    bentuk_fisik ENUM('CAIR', 'PADAT_BUBUK', 'GRANUL') NOT NULL,
    stok_saat_ini DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    satuan_stok ENUM('ML', 'GRAM') NOT NULL, -- Diseragamkan ke satuan dasar terkecil
    tanggal_kadaluarsa DATE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE, -- Soft-delete agar riwayat aman
    catatan TEXT
) ENGINE=InnoDB;

-- ==========================================
-- 2. ENTITAS JANGKAR TRANSAKSI (SESI)
-- ==========================================

-- Tabel Sesi Pemeriksaan Harian
CREATE TABLE log_pemeriksaan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tanaman_id BIGINT NOT NULL,
    cuaca_id BIGINT NULL, -- Nullable jika API gagal atau tidak dikaitkan
    waktu_pemeriksaan DATETIME NOT NULL,
    catatan_umum TEXT,
    CONSTRAINT fk_log_tanaman FOREIGN KEY (tanaman_id) 
        REFERENCES tanaman(id) ON DELETE CASCADE,
    CONSTRAINT fk_log_cuaca FOREIGN KEY (cuaca_id) 
        REFERENCES cuaca(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ==========================================
-- 3. HIERARKI KONDISI TANAMAN (JOINED TABLE)
-- ==========================================

-- Superclass Kondisi Tanaman
CREATE TABLE kondisi_tanaman (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    log_pemeriksaan_id BIGINT NOT NULL,
    tipe_kondisi VARCHAR(30) NOT NULL, -- Discriminator: 'VEGETATIF', 'KESEHATAN'
    status_umum ENUM('OPTIMAL', 'PERLU_PERHATIAN', 'KRITIS') NOT NULL,
    catatan TEXT,
    CONSTRAINT fk_kondisi_log FOREIGN KEY (log_pemeriksaan_id) 
        REFERENCES log_pemeriksaan(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Subclass 1: Kondisi Vegetatif (Pertumbuhan Fisik)
CREATE TABLE kondisi_vegetatif (
    kondisi_id BIGINT PRIMARY KEY,
    tinggi_cm DECIMAL(6,2),
    fase_tumbuh ENUM('SEMAI', 'VEGETATIF', 'BERBUNGA', 'BERBUAH') NOT NULL,
    muncul_tunas_baru BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_vegetatif_induk FOREIGN KEY (kondisi_id) 
        REFERENCES kondisi_tanaman(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Subclass 2: Kondisi Kesehatan (Hama, Penyakit, Defisiensi)
CREATE TABLE kondisi_kesehatan (
    kondisi_id BIGINT PRIMARY KEY,
    jenis_gangguan ENUM('HAMA', 'PENYAKIT_JAMUR_BAKTERI', 'DEFISIENSI_NUTRISI') NOT NULL,
    nama_gejala_atau_hama VARCHAR(100) NOT NULL,
    tingkat_keparahan ENUM('RINGAN', 'SEDANG', 'PARAH') NOT NULL,
    bagian_terdampak VARCHAR(100), -- Contoh: Bawah Daun, Pucuk, Batang
    CONSTRAINT fk_kesehatan_induk FOREIGN KEY (kondisi_id) 
        REFERENCES kondisi_tanaman(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ==========================================
-- 4. HIERARKI PERLAKUAN (JOINED TABLE)
-- ==========================================

-- Superclass Perlakuan
CREATE TABLE perlakuan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    log_pemeriksaan_id BIGINT NOT NULL,
    tipe_perlakuan VARCHAR(30) NOT NULL, -- Discriminator: 'PENYIRAMAN', 'PEMUPUKAN', 'PENYEMPROTAN'
    catatan TEXT,
    CONSTRAINT fk_perlakuan_log FOREIGN KEY (log_pemeriksaan_id) 
        REFERENCES log_pemeriksaan(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Subclass 1: Penyiraman Air Murni
CREATE TABLE perlakuan_penyiraman (
    perlakuan_id BIGINT PRIMARY KEY,
    volume_ml INT NOT NULL,
    CONSTRAINT fk_siram_induk FOREIGN KEY (perlakuan_id) 
        REFERENCES perlakuan(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Subclass 2: Pemupukan (Mendukung Kocor / Tabur / Tunggal maupun Campuran)
CREATE TABLE perlakuan_pemupukan (
    perlakuan_id BIGINT PRIMARY KEY,
    metode_pupuk ENUM('KOCOR', 'TABUR') NOT NULL,
    volume_air_kocor_ml INT DEFAULT 0, -- Diisi jika metode = KOCOR
    CONSTRAINT fk_pupuk_induk FOREIGN KEY (perlakuan_id) 
        REFERENCES perlakuan(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Detail Bahan Pemupukan (Mengakomodasi jika pupuk dicampur saat kocor/tabur)
CREATE TABLE detail_bahan_pemupukan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pemupukan_id BIGINT NOT NULL,
    bahan_id BIGINT NOT NULL,
    dosis_per_liter_atau_pot DECIMAL(6,2) NOT NULL,
    total_terpakai DECIMAL(8,2) NOT NULL, -- Nilai pasti (ML/GRAM) yang memotong stok inventaris
    CONSTRAINT fk_detail_pupuk FOREIGN KEY (pemupukan_id) 
        REFERENCES perlakuan_pemupukan(perlakuan_id) ON DELETE CASCADE,
    CONSTRAINT fk_pupuk_inventaris FOREIGN KEY (bahan_id) 
        REFERENCES inventaris_bahan(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- Subclass 3: Penyemprotan (Pestisida / Pupuk Daun - Tank Mix)
CREATE TABLE perlakuan_penyemprotan (
    perlakuan_id BIGINT PRIMARY KEY,
    volume_air_semprot_ml INT NOT NULL, -- Kapasitas air pelarut di tangki
    CONSTRAINT fk_semprot_induk FOREIGN KEY (perlakuan_id) 
        REFERENCES perlakuan(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Detail Bahan Semprotan (Tank Mix)
CREATE TABLE detail_campuran_semprot (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    penyemprotan_id BIGINT NOT NULL,
    bahan_id BIGINT NOT NULL,
    dosis_per_liter DECIMAL(6,2) NOT NULL, -- Contoh: 1.5 (ml/L atau gr/L)
    total_terpakai DECIMAL(8,2) NOT NULL,  -- Hasil kalkulasi (dosis_per_liter * liter air) untuk potong stok
    CONSTRAINT fk_detail_semprot FOREIGN KEY (penyemprotan_id) 
        REFERENCES perlakuan_penyemprotan(perlakuan_id) ON DELETE CASCADE,
    CONSTRAINT fk_semprot_inventaris FOREIGN KEY (bahan_id) 
        REFERENCES inventaris_bahan(id) ON DELETE RESTRICT
) ENGINE=InnoDB;