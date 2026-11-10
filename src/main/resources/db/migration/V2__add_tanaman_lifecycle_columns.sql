-- V2__add_tanaman_lifecycle_columns.sql
-- Menambahkan kolom siklus hidup tanaman jika belum ada (idempotent untuk DB eksisting dan DB baru)

SET @dbname = COALESCE(DATABASE(), 'db_manajemen_tanaman');
SET @tablename = 'tanaman';

-- Kolom tanggal_selesai
SET @col = 'tanggal_selesai';
SET @preparedStatement = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA = @dbname AND TABLE_NAME = @tablename AND COLUMN_NAME = @col) > 0,
    'SELECT 1',
    'ALTER TABLE tanaman ADD COLUMN tanggal_selesai DATE NULL'
));
PREPARE stmt FROM @preparedStatement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Kolom keterangan_selesai
SET @col = 'keterangan_selesai';
SET @preparedStatement = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA = @dbname AND TABLE_NAME = @tablename AND COLUMN_NAME = @col) > 0,
    'SELECT 1',
    'ALTER TABLE tanaman ADD COLUMN keterangan_selesai VARCHAR(100) NULL'
));
PREPARE stmt FROM @preparedStatement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
