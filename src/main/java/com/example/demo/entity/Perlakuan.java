package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "perlakuan")
public class Perlakuan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @Column(name = "log_pemeriksaan_id", nullable = false)
    private Long logPemeriksaanId;

    public Long getLogPemeriksaanId() { return logPemeriksaanId; }
    public void setLogPemeriksaanId(Long logPemeriksaanId) { this.logPemeriksaanId = logPemeriksaanId; }

    @Column(name = "tipe_perlakuan", nullable = false)
    private String tipePerlakuan;

    public String getTipePerlakuan() { return tipePerlakuan; }
    public void setTipePerlakuan(String tipePerlakuan) { this.tipePerlakuan = tipePerlakuan; }

    @Column(name = "catatan")
    private String catatan;

    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }

}
