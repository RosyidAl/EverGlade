package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "perlakuan")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipe_perlakuan", discriminatorType = DiscriminatorType.STRING, length = 30)
public abstract class Perlakuan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "log_pemeriksaan_id", nullable = false)
    private LogPemeriksaan logPemeriksaan;

    @Column(name = "catatan", columnDefinition = "TEXT")
    private String catatan;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LogPemeriksaan getLogPemeriksaan() { return logPemeriksaan; }
    public void setLogPemeriksaan(LogPemeriksaan logPemeriksaan) { this.logPemeriksaan = logPemeriksaan; }

    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }
}