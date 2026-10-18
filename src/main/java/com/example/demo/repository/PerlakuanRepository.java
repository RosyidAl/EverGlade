package com.example.demo.repository;

import com.example.demo.entity.Perlakuan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerlakuanRepository extends JpaRepository<Perlakuan, Long> {
}
