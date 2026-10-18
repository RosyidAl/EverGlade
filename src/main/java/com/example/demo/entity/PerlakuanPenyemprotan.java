package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "perlakuan_penyemprotan")
public class PerlakuanPenyemprotan {

    @Id
    @Column(name = "perlakuan_id")
    private Long perlakuanId;

    public Long getPerlakuanId() { return perlakuanId; }
    public void setPerlakuanId(Long perlakuanId) { this.perlakuanId = perlakuanId; }

    @Column(name = "volume_air_semprot_ml", nullable = false)
    private Integer volumeAirSemprotMl;

    public Integer getVolumeAirSemprotMl() { return volumeAirSemprotMl; }
    public void setVolumeAirSemprotMl(Integer volumeAirSemprotMl) { this.volumeAirSemprotMl = volumeAirSemprotMl; }

}
