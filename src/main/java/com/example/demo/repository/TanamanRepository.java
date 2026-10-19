package com.example.demo.repository;

import com.example.demo.entity.Tanaman;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TanamanRepository extends JpaRepository<Tanaman, Long> {
    List<Tanaman> findByStatusAktif(Boolean statusAktif);
}
