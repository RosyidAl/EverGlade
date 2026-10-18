package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detail_campuran_semprot")
public class DetailCampuranSemprot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @Column(name = "penyemprotan_id", nullable = false)
    private Long penyemprotanId;

    public Long getPenyemprotanId() { return penyemprotanId; }
    public void setPenyemprotanId(Long penyemprotanId) { this.penyemprotanId = penyemprotanId; }

    @Column(name = "bahan_id", nullable = false)
    private Long bahanId;

    public Long getBahanId() { return bahanId; }
    public void setBahanId(Long bahanId) { this.bahanId = bahanId; }

    @Column(name = "dosis_per_liter", nullable = false)
    private BigDecimal dosisPerLiter;

    public BigDecimal getDosisPerLiter() { return dosisPerLiter; }
    public void setDosisPerLiter(BigDecimal dosisPerLiter) { this.dosisPerLiter = dosisPerLiter; }

    @Column(name = "total_terpakai", nullable = false)
    private BigDecimal totalTerpakai;

    public BigDecimal getTotalTerpakai() { return totalTerpakai; }
    public void setTotalTerpakai(BigDecimal totalTerpakai) { this.totalTerpakai = totalTerpakai; }

}
