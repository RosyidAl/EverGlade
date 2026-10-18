package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detail_bahan_pemupukan")
public class DetailBahanPemupukan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @Column(name = "pemupukan_id", nullable = false)
    private Long pemupukanId;

    public Long getPemupukanId() { return pemupukanId; }
    public void setPemupukanId(Long pemupukanId) { this.pemupukanId = pemupukanId; }

    @Column(name = "bahan_id", nullable = false)
    private Long bahanId;

    public Long getBahanId() { return bahanId; }
    public void setBahanId(Long bahanId) { this.bahanId = bahanId; }

    @Column(name = "dosis_per_liter_atau_pot", nullable = false)
    private BigDecimal dosisPerLiterAtauPot;

    public BigDecimal getDosisPerLiterAtauPot() { return dosisPerLiterAtauPot; }
    public void setDosisPerLiterAtauPot(BigDecimal dosisPerLiterAtauPot) { this.dosisPerLiterAtauPot = dosisPerLiterAtauPot; }

    @Column(name = "total_terpakai", nullable = false)
    private BigDecimal totalTerpakai;

    public BigDecimal getTotalTerpakai() { return totalTerpakai; }
    public void setTotalTerpakai(BigDecimal totalTerpakai) { this.totalTerpakai = totalTerpakai; }

}
