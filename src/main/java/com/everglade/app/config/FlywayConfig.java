package com.everglade.app.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jpa.autoconfigure.EntityManagerFactoryDependsOnPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Konfigurasi inisialisasi Flyway database migration untuk Spring Boot.
 * Memastikan skrip migrasi dijalankan sebelum EntityManagerFactory Hibernate melakukan validasi skema.
 */
@Configuration
public class FlywayConfig {

    @Value("${spring.flyway.baseline-on-migrate:true}")
    private boolean baselineOnMigrate;

    @Value("${spring.flyway.baseline-version:1}")
    private String baselineVersion;

    /**
     * Membuat bean Flyway dan mengeksekusi migrasi saat inisialisasi bean.
     */
    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .baselineOnMigrate(baselineOnMigrate)
                .baselineVersion(baselineVersion)
                .locations("classpath:db/migration")
                .load();
    }

    /**
     * Memastikan EntityManagerFactory JPA bergantung pada bean Flyway
     * agar seluruh skrip migrasi selesai dieksekusi sebelum validasi skema Hibernate.
     */
    @Bean
    public static EntityManagerFactoryDependsOnPostProcessor flywayEntityManagerFactoryDependsOnPostProcessor() {
        return new EntityManagerFactoryDependsOnPostProcessor(Flyway.class);
    }
}

