package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cuaca")
public class Cuaca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "waktu_catat", nullable = false)
    private LocalDateTime waktuCatat;

    @Column(name = "kondisi", nullable = false, length = 50)
    private String kondisi;

    @Column(name = "suhu_celsius", nullable = false, precision = 4, scale = 1)
    private BigDecimal suhuCelsius;

    @Column(name = "kelembapan_persen", nullable = false)
    private Integer kelembapanPersen;

    @Column(name = "curah_hujan_mm", precision = 5, scale = 2)
    private BigDecimal curahHujanMm = BigDecimal.ZERO;

    @Column(name = "kecepatan_angin_kmh", precision = 5, scale = 2)
    private BigDecimal kecepatanAnginKmh = BigDecimal.ZERO;

    @OneToMany(mappedBy = "cuaca")
    @JsonIgnore
    private List<LogPemeriksaan> logPemeriksaanList = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getWaktuCatat() { return waktuCatat; }
    public void setWaktuCatat(LocalDateTime waktuCatat) { this.waktuCatat = waktuCatat; }

    public String getKondisi() { return kondisi; }
    public void setKondisi(String kondisi) { this.kondisi = kondisi; }

    public BigDecimal getSuhuCelsius() { return suhuCelsius; }
    public void setSuhuCelsius(BigDecimal suhuCelsius) { this.suhuCelsius = suhuCelsius; }

    public Integer getKelembapanPersen() { return kelembapanPersen; }
    public void setKelembapanPersen(Integer kelembapanPersen) { this.kelembapanPersen = kelembapanPersen; }

    public BigDecimal getCurahHujanMm() { return curahHujanMm; }
    public void setCurahHujanMm(BigDecimal curahHujanMm) { this.curahHujanMm = curahHujanMm; }

    public BigDecimal getKecepatanAnginKmh() { return kecepatanAnginKmh; }
    public void setKecepatanAnginKmh(BigDecimal kecepatanAnginKmh) { this.kecepatanAnginKmh = kecepatanAnginKmh; }

    public List<LogPemeriksaan> getLogPemeriksaanList() { return logPemeriksaanList; }
    public void setLogPemeriksaanList(List<LogPemeriksaan> logPemeriksaanList) { this.logPemeriksaanList = logPemeriksaanList; }
}
