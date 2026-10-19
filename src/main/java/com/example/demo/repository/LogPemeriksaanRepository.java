package com.example.demo.repository;

import com.example.demo.entity.LogPemeriksaan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogPemeriksaanRepository extends JpaRepository<LogPemeriksaan, Long> {
    /**
     * Mencari log tanaman dengan urutan waktu pemeriksaan terbaru terlebih dahulu.
     *
     * @param tanamanId ID tanaman pemilik log
     * @return daftar log tanaman dalam urutan waktu menurun
     */
    List<LogPemeriksaan> findByTanamanIdOrderByWaktuPemeriksaanDesc(Long tanamanId);
}
