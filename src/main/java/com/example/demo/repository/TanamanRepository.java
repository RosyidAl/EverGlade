package com.example.demo.repository;

import com.example.demo.entity.Tanaman;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TanamanRepository extends JpaRepository<Tanaman, Long> {
    /**
     * Mencari tanaman berdasarkan status aktif.
     *
     * @param statusAktif status aktif yang harus cocok
     * @return daftar tanaman dengan status yang diminta
     */
    List<Tanaman> findByStatusAktif(Boolean statusAktif);
}
