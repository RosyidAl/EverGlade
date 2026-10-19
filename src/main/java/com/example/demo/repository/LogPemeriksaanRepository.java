package com.example.demo.repository;

import com.example.demo.entity.LogPemeriksaan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogPemeriksaanRepository extends JpaRepository<LogPemeriksaan, Long> {
    List<LogPemeriksaan> findByTanamanIdOrderByWaktuPemeriksaanDesc(Long tanamanId);
}
