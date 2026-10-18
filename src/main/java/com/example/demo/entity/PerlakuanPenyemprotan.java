package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "perlakuan_penyemprotan")
@PrimaryKeyJoinColumn(name = "perlakuan_id")
@DiscriminatorValue("PENYEMPROTAN")
public class PerlakuanPenyemprotan extends Perlakuan {

    @Column(name = "volume_air_semprot_ml", nullable = false)
    private Integer volumeAirSemprotMl;

    public Integer getVolumeAirSemprotMl() { return volumeAirSemprotMl; }
    public void setVolumeAirSemprotMl(Integer volumeAirSemprotMl) { this.volumeAirSemprotMl = volumeAirSemprotMl; }
}