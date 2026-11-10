-- V3__fix_tanaman_lifecycle_columns.sql
-- Robustly adds lifecycle columns that V2 may have failed to apply on Azure MySQL.
-- Uses a stored procedure (more reliable DDL than PREPARE/EXECUTE on managed MySQL).

DELIMITER //

CREATE PROCEDURE everglade_fix_lifecycle_cols()
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_NAME = 'tanaman'
          AND COLUMN_NAME = 'tanggal_selesai'
    ) THEN
        ALTER TABLE tanaman ADD COLUMN tanggal_selesai DATE NULL;
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_NAME = 'tanaman'
          AND COLUMN_NAME = 'keterangan_selesai'
    ) THEN
        ALTER TABLE tanaman ADD COLUMN keterangan_selesai VARCHAR(100) NULL;
    END IF;
END //

DELIMITER ;

CALL everglade_fix_lifecycle_cols();

DROP PROCEDURE IF EXISTS everglade_fix_lifecycle_cols;
