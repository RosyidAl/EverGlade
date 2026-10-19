package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tanaman")
public class Tanaman {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nama_tanaman", nullable = false, length = 100)
    private String namaTanaman;

    @Column(name = "jenis_varietas", length = 100)
    private String jenisVarietas;

    @Column(name = "lokasi_tanam", length = 100)
    private String lokasiTanam;

    @Column(name = "tanggal_tanam", nullable = false)
    private LocalDate tanggalTanam;

    @Column(name = "status_aktif", nullable = false)
    private Boolean statusAktif = true;

    @Column(name = "tanggal_selesai")
    private LocalDate tanggalSelesai;

    @Column(name = "keterangan_selesai", length = 100)
    private String keteranganSelesai;

    @Column(name = "catatan", columnDefinition = "TEXT")
    private String catatan;

    @OneToMany(mappedBy = "tanaman")
    @JsonIgnore
    private List<LogPemeriksaan> logPemeriksaanList = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNamaTanaman() { return namaTanaman; }
    public void setNamaTanaman(String namaTanaman) { this.namaTanaman = namaTanaman; }

    public String getJenisVarietas() { return jenisVarietas; }
    public void setJenisVarietas(String jenisVarietas) { this.jenisVarietas = jenisVarietas; }

    public String getLokasiTanam() { return lokasiTanam; }
    public void setLokasiTanam(String lokasiTanam) { this.lokasiTanam = lokasiTanam; }

    public LocalDate getTanggalTanam() { return tanggalTanam; }
    public void setTanggalTanam(LocalDate tanggalTanam) { this.tanggalTanam = tanggalTanam; }

    public Boolean getStatusAktif() { return statusAktif; }
    public void setStatusAktif(Boolean statusAktif) { this.statusAktif = statusAktif; }

    /** Mengambil tanggal penyelesaian tanaman, atau null jika belum dicatat. */
    public LocalDate getTanggalSelesai() { return tanggalSelesai; }
    /** Mengatur tanggal penyelesaian tanaman; null menghapus tanggal tersebut. */
    public void setTanggalSelesai(LocalDate tanggalSelesai) { this.tanggalSelesai = tanggalSelesai; }

    /** Mengambil alasan penyelesaian tanaman, atau null jika belum dicatat. */
    public String getKeteranganSelesai() { return keteranganSelesai; }
    /** Mengatur alasan penyelesaian tanaman; null menghapus alasan tersebut. */
    public void setKeteranganSelesai(String keteranganSelesai) { this.keteranganSelesai = keteranganSelesai; }

    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }

    public List<LogPemeriksaan> getLogPemeriksaanList() { return logPemeriksaanList; }
    public void setLogPemeriksaanList(List<LogPemeriksaan> logPemeriksaanList) { this.logPemeriksaanList = logPemeriksaanList; }
}
