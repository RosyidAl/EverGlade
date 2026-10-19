package com.example.demo.repository;

import com.example.demo.entity.InventarisBahan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface InventarisBahanRepository extends JpaRepository<InventarisBahan, Long> {
    /**
     * Mencari bahan berdasarkan status aktif.
     *
     * @param isActive status aktif yang harus cocok
     * @return daftar bahan dengan status yang diminta
     */
    List<InventarisBahan> findByIsActive(Boolean isActive);
    /**
     * Mencari bahan berdasarkan status aktif dan batas atas stok inklusif.
     *
     * @param isActive status aktif yang harus cocok
     * @param threshold batas maksimum stok dalam satuan stok masing-masing bahan
     * @return daftar bahan yang memenuhi status dan batas stok
     */
    List<InventarisBahan> findByIsActiveAndStokSaatIniLessThanEqual(Boolean isActive, BigDecimal threshold);
}
