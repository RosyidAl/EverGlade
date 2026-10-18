package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tanaman")
public class Tanaman {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @Column(name = "nama_tanaman", nullable = false)
    private String namaTanaman;

    public String getNamaTanaman() { return namaTanaman; }
    public void setNamaTanaman(String namaTanaman) { this.namaTanaman = namaTanaman; }

    @Column(name = "jenis_varietas")
    private String jenisVarietas;

    public String getJenisVarietas() { return jenisVarietas; }
    public void setJenisVarietas(String jenisVarietas) { this.jenisVarietas = jenisVarietas; }

    @Column(name = "lokasi_tanam")
    private String lokasiTanam;

    public String getLokasiTanam() { return lokasiTanam; }
    public void setLokasiTanam(String lokasiTanam) { this.lokasiTanam = lokasiTanam; }

    @Column(name = "tanggal_tanam", nullable = false)
    private LocalDate tanggalTanam;

    public LocalDate getTanggalTanam() { return tanggalTanam; }
    public void setTanggalTanam(LocalDate tanggalTanam) { this.tanggalTanam = tanggalTanam; }

    @Column(name = "status_aktif", nullable = false)
    private Boolean statusAktif;

    public Boolean getStatusAktif() { return statusAktif; }
    public void setStatusAktif(Boolean statusAktif) { this.statusAktif = statusAktif; }

    @Column(name = "catatan")
    private String catatan;

    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }

}
