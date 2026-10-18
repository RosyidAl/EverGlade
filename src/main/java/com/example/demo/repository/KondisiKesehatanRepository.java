package com.example.demo.repository;

import com.example.demo.entity.KondisiKesehatan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KondisiKesehatanRepository extends JpaRepository<KondisiKesehatan, Long> {
}