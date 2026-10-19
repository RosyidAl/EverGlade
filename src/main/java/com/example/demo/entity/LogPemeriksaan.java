package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "log_pemeriksaan")
public class LogPemeriksaan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tanaman_id", nullable = false)
    private Tanaman tanaman;

    @ManyToOne
    @JoinColumn(name = "cuaca_id")
    private Cuaca cuaca;

    @Column(name = "waktu_pemeriksaan", nullable = false)
    private LocalDateTime waktuPemeriksaan;

    @Column(name = "catatan_umum", columnDefinition = "TEXT")
    private String catatanUmum;

    @OneToMany(mappedBy = "logPemeriksaan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KondisiTanaman> daftarKondisi = new ArrayList<>();

    @OneToMany(mappedBy = "logPemeriksaan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Perlakuan> daftarPerlakuan = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Tanaman getTanaman() { return tanaman; }
    public void setTanaman(Tanaman tanaman) { this.tanaman = tanaman; }

    public Cuaca getCuaca() { return cuaca; }
    public void setCuaca(Cuaca cuaca) { this.cuaca = cuaca; }

    public LocalDateTime getWaktuPemeriksaan() { return waktuPemeriksaan; }
    public void setWaktuPemeriksaan(LocalDateTime waktuPemeriksaan) { this.waktuPemeriksaan = waktuPemeriksaan; }

    public String getCatatanUmum() { return catatanUmum; }
    public void setCatatanUmum(String catatanUmum) { this.catatanUmum = catatanUmum; }

    public List<KondisiTanaman> getDaftarKondisi() { return daftarKondisi; }
    public void setDaftarKondisi(List<KondisiTanaman> daftarKondisi) { this.daftarKondisi = daftarKondisi; }

    public List<Perlakuan> getDaftarPerlakuan() { return daftarPerlakuan; }
    public void setDaftarPerlakuan(List<Perlakuan> daftarPerlakuan) { this.daftarPerlakuan = daftarPerlakuan; }

    public void addKondisi(KondisiTanaman kondisi) {
        if (kondisi != null) {
            daftarKondisi.add(kondisi);
            kondisi.setLogPemeriksaan(this);
        }
    }

    public void addPerlakuan(Perlakuan perlakuan) {
        if (perlakuan != null) {
            daftarPerlakuan.add(perlakuan);
            perlakuan.setLogPemeriksaan(this);
        }
    }
}