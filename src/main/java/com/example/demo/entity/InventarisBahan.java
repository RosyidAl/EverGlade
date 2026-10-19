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

    @Column(name = "nama_produk", nullable = false, length = 100)
    private String namaProduk;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipe_bahan", nullable = false)
    private TipeBahan tipeBahan;

    @Column(name = "kategori_detail", length = 50)
    private String kategoriDetail;

    @Enumerated(EnumType.STRING)
    @Column(name = "bentuk_fisik", nullable = false)
    private BentukFisik bentukFisik;

    @Column(name = "stok_saat_ini", nullable = false, precision = 10, scale = 2)
    private BigDecimal stokSaatIni;

    @Enumerated(EnumType.STRING)
    @Column(name = "satuan_stok", nullable = false)
    private SatuanStok satuanStok;

    @Column(name = "tanggal_kadaluarsa")
    private LocalDate tanggalKadaluarsa;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "catatan", columnDefinition = "TEXT")
    private String catatan;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNamaProduk() { return namaProduk; }
    public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }

    public TipeBahan getTipeBahan() { return tipeBahan; }
    public void setTipeBahan(TipeBahan tipeBahan) { this.tipeBahan = tipeBahan; }

    public String getKategoriDetail() { return kategoriDetail; }
    public void setKategoriDetail(String kategoriDetail) { this.kategoriDetail = kategoriDetail; }

    public BentukFisik getBentukFisik() { return bentukFisik; }
    public void setBentukFisik(BentukFisik bentukFisik) { this.bentukFisik = bentukFisik; }

    public BigDecimal getStokSaatIni() { return stokSaatIni; }
    public void setStokSaatIni(BigDecimal stokSaatIni) { this.stokSaatIni = stokSaatIni; }

    public SatuanStok getSatuanStok() { return satuanStok; }
    public void setSatuanStok(SatuanStok satuanStok) { this.satuanStok = satuanStok; }

    public LocalDate getTanggalKadaluarsa() { return tanggalKadaluarsa; }
    public void setTanggalKadaluarsa(LocalDate tanggalKadaluarsa) { this.tanggalKadaluarsa = tanggalKadaluarsa; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }
}
