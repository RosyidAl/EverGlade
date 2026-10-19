package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detail_bahan_pemupukan", uniqueConstraints = {
    @UniqueConstraint(name = "uq_pemupukan_bahan", columnNames = {"pemupukan_id", "bahan_id"})
})
public class DetailBahanPemupukan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pemupukan_id", nullable = false)
    @JsonIgnore
    private PerlakuanPemupukan pemupukan;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bahan_id", nullable = false)
    private InventarisBahan bahan;

    @Column(name = "dosis_per_liter_atau_pot", nullable = false, precision = 6, scale = 2)
    private BigDecimal dosisPerLiterAtauPot;

    @Column(name = "total_terpakai", nullable = false, precision = 8, scale = 2)
    private BigDecimal totalTerpakai;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PerlakuanPemupukan getPemupukan() { return pemupukan; }
    public void setPemupukan(PerlakuanPemupukan pemupukan) { this.pemupukan = pemupukan; }

    public InventarisBahan getBahan() { return bahan; }
    public void setBahan(InventarisBahan bahan) { this.bahan = bahan; }

    public BigDecimal getDosisPerLiterAtauPot() { return dosisPerLiterAtauPot; }
    public void setDosisPerLiterAtauPot(BigDecimal dosisPerLiterAtauPot) { this.dosisPerLiterAtauPot = dosisPerLiterAtauPot; }

    public BigDecimal getTotalTerpakai() { return totalTerpakai; }
    public void setTotalTerpakai(BigDecimal totalTerpakai) { this.totalTerpakai = totalTerpakai; }
}