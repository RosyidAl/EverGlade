package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "kondisi_tanaman")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipe_kondisi", discriminatorType = DiscriminatorType.STRING, length = 30)
public abstract class KondisiTanaman {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "log_pemeriksaan_id", nullable = false)
    @JsonIgnore
    private LogPemeriksaan logPemeriksaan;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_umum", nullable = false)
    private StatusUmum statusUmum;

    @Column(name = "catatan", columnDefinition = "TEXT")
    private String catatan;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LogPemeriksaan getLogPemeriksaan() { return logPemeriksaan; }
    public void setLogPemeriksaan(LogPemeriksaan logPemeriksaan) { this.logPemeriksaan = logPemeriksaan; }

    public StatusUmum getStatusUmum() { return statusUmum; }
    public void setStatusUmum(StatusUmum statusUmum) { this.statusUmum = statusUmum; }

    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }
}