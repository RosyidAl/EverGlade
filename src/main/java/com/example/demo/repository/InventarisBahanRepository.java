package com.example.demo.repository;

import com.example.demo.entity.InventarisBahan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface InventarisBahanRepository extends JpaRepository<InventarisBahan, Long> {
    List<InventarisBahan> findByIsActive(Boolean isActive);
    List<InventarisBahan> findByIsActiveAndStokSaatIniLessThanEqual(Boolean isActive, BigDecimal threshold);
}
