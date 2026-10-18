package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "inventaris_bahan")
public class InventarisBahan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @Column(name = "nama_produk", nullable = false)
    private String namaProduk;

    public String getNamaProduk() { return namaProduk; }
    public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }

    @Column(name = "tipe_bahan", nullable = false)
    private String tipeBahan;

    public String getTipeBahan() { return tipeBahan; }
    public void setTipeBahan(String tipeBahan) { this.tipeBahan = tipeBahan; }

    @Column(name = "kategori_detail")
    private String kategoriDetail;

    public String getKategoriDetail() { return kategoriDetail; }
    public void setKategoriDetail(String kategoriDetail) { this.kategoriDetail = kategoriDetail; }

    @Column(name = "bentuk_fisik", nullable = false)
    private String bentukFisik;

    public String getBentukFisik() { return bentukFisik; }
    public void setBentukFisik(String bentukFisik) { this.bentukFisik = bentukFisik; }

    @Column(name = "stok_saat_ini", nullable = false)
    private BigDecimal stokSaatIni;

    public BigDecimal getStokSaatIni() { return stokSaatIni; }
    public void setStokSaatIni(BigDecimal stokSaatIni) { this.stokSaatIni = stokSaatIni; }

    @Column(name = "satuan_stok", nullable = false)
    private String satuanStok;

    public String getSatuanStok() { return satuanStok; }
    public void setSatuanStok(String satuanStok) { this.satuanStok = satuanStok; }

    @Column(name = "tanggal_kadaluarsa")
    private LocalDate tanggalKadaluarsa;

    public LocalDate getTanggalKadaluarsa() { return tanggalKadaluarsa; }
    public void setTanggalKadaluarsa(LocalDate tanggalKadaluarsa) { this.tanggalKadaluarsa = tanggalKadaluarsa; }

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    @Column(name = "catatan")
    private String catatan;

    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }

}
