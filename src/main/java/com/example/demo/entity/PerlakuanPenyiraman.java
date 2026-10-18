package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "perlakuan_penyiraman")
@PrimaryKeyJoinColumn(name = "perlakuan_id")
@DiscriminatorValue("PENYIRAMAN")
public class PerlakuanPenyiraman extends Perlakuan {

    @Column(name = "volume_ml", nullable = false)
    private Integer volumeMl;

    public Integer getVolumeMl() { return volumeMl; }
    public void setVolumeMl(Integer volumeMl) { this.volumeMl = volumeMl; }
}