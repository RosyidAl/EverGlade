package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "kondisi_vegetatif")
@PrimaryKeyJoinColumn(name = "kondisi_id")
@DiscriminatorValue("VEGETATIF")
public class KondisiVegetatif extends KondisiTanaman {

    @Column(name = "tinggi_cm", precision = 6, scale = 2)
    private BigDecimal tinggiCm;

    @Enumerated(EnumType.STRING)
    @Column(name = "fase_tumbuh", nullable = false)
    private FaseTumbuh faseTumbuh;

    @Column(name = "muncul_tunas_baru")
    private Boolean munculTunasBaru;

    public BigDecimal getTinggiCm() { return tinggiCm; }
    public void setTinggiCm(BigDecimal tinggiCm) { this.tinggiCm = tinggiCm; }

    public FaseTumbuh getFaseTumbuh() { return faseTumbuh; }
    public void setFaseTumbuh(FaseTumbuh faseTumbuh) { this.faseTumbuh = faseTumbuh; }

    public Boolean getMunculTunasBaru() { return munculTunasBaru; }
    public void setMunculTunasBaru(Boolean munculTunasBaru) { this.munculTunasBaru = munculTunasBaru; }
}