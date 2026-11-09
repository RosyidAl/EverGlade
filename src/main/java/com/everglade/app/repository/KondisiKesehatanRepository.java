package com.everglade.app.repository;

import com.everglade.app.entity.KondisiKesehatan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KondisiKesehatanRepository extends JpaRepository<KondisiKesehatan, Long> {
}