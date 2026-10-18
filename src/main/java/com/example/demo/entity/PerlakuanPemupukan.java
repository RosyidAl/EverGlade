package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "perlakuan_pemupukan")
@PrimaryKeyJoinColumn(name = "perlakuan_id")
@DiscriminatorValue("PEMUPUKAN")
public class PerlakuanPemupukan extends Perlakuan {

    @Enumerated(EnumType.STRING)
    @Column(name = "metode_pupuk", nullable = false)
    private MetodePupuk metodePupuk;

    @Column(name = "volume_air_kocor_ml")
    private Integer volumeAirKocorMl;

    public MetodePupuk getMetodePupuk() { return metodePupuk; }
    public void setMetodePupuk(MetodePupuk metodePupuk) { this.metodePupuk = metodePupuk; }

    public Integer getVolumeAirKocorMl() { return volumeAirKocorMl; }
    public void setVolumeAirKocorMl(Integer volumeAirKocorMl) { this.volumeAirKocorMl = volumeAirKocorMl; }
}