package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cuaca")
public class Cuaca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @Column(name = "waktu_catat", nullable = false)
    private LocalDateTime waktuCatat;

    public LocalDateTime getWaktuCatat() { return waktuCatat; }
    public void setWaktuCatat(LocalDateTime waktuCatat) { this.waktuCatat = waktuCatat; }

    @Column(name = "kondisi", nullable = false)
    private String kondisi;

    public String getKondisi() { return kondisi; }
    public void setKondisi(String kondisi) { this.kondisi = kondisi; }

    @Column(name = "suhu_celsius", nullable = false)
    private BigDecimal suhuCelsius;

    public BigDecimal getSuhuCelsius() { return suhuCelsius; }
    public void setSuhuCelsius(BigDecimal suhuCelsius) { this.suhuCelsius = suhuCelsius; }

    @Column(name = "kelembapan_persen", nullable = false)
    private Integer kelembapanPersen;

    public Integer getKelembapanPersen() { return kelembapanPersen; }
    public void setKelembapanPersen(Integer kelembapanPersen) { this.kelembapanPersen = kelembapanPersen; }

    @Column(name = "curah_hujan_mm")
    private BigDecimal curahHujanMm;

    public BigDecimal getCurahHujanMm() { return curahHujanMm; }
    public void setCurahHujanMm(BigDecimal curahHujanMm) { this.curahHujanMm = curahHujanMm; }

    @Column(name = "kecepatan_angin_kmh")
    private BigDecimal kecepatanAnginKmh;

    public BigDecimal getKecepatanAnginKmh() { return kecepatanAnginKmh; }
    public void setKecepatanAnginKmh(BigDecimal kecepatanAnginKmh) { this.kecepatanAnginKmh = kecepatanAnginKmh; }

}
