package com.example.demo.repository;

import com.example.demo.entity.Tanaman;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TanamanRepository extends JpaRepository<Tanaman, Long> {
}
