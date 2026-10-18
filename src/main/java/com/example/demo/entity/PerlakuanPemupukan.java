package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "perlakuan_pemupukan")
public class PerlakuanPemupukan {

    @Id
    @Column(name = "perlakuan_id")
    private Long perlakuanId;

    public Long getPerlakuanId() { return perlakuanId; }
    public void setPerlakuanId(Long perlakuanId) { this.perlakuanId = perlakuanId; }

    @Column(name = "metode_pupuk", nullable = false)
    private String metodePupuk;

    public String getMetodePupuk() { return metodePupuk; }
    public void setMetodePupuk(String metodePupuk) { this.metodePupuk = metodePupuk; }

    @Column(name = "volume_air_kocor_ml")
    private Integer volumeAirKocorMl;

    public Integer getVolumeAirKocorMl() { return volumeAirKocorMl; }
    public void setVolumeAirKocorMl(Integer volumeAirKocorMl) { this.volumeAirKocorMl = volumeAirKocorMl; }

}
