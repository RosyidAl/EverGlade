package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detail_campuran_semprot", uniqueConstraints = {
    @UniqueConstraint(name = "uq_semprot_bahan", columnNames = {"penyemprotan_id", "bahan_id"})
})
public class DetailCampuranSemprot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "penyemprotan_id", nullable = false)
    @JsonIgnore
    private PerlakuanPenyemprotan penyemprotan;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bahan_id", nullable = false)
    private InventarisBahan bahan;

    @Column(name = "dosis_per_liter", nullable = false, precision = 6, scale = 2)
    private BigDecimal dosisPerLiter;

    @Column(name = "total_terpakai", nullable = false, precision = 8, scale = 2)
    private BigDecimal totalTerpakai;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PerlakuanPenyemprotan getPenyemprotan() { return penyemprotan; }
    public void setPenyemprotan(PerlakuanPenyemprotan penyemprotan) { this.penyemprotan = penyemprotan; }

    public InventarisBahan getBahan() { return bahan; }
    public void setBahan(InventarisBahan bahan) { this.bahan = bahan; }

    public BigDecimal getDosisPerLiter() { return dosisPerLiter; }
    public void setDosisPerLiter(BigDecimal dosisPerLiter) { this.dosisPerLiter = dosisPerLiter; }

    public BigDecimal getTotalTerpakai() { return totalTerpakai; }
    public void setTotalTerpakai(BigDecimal totalTerpakai) { this.totalTerpakai = totalTerpakai; }
}