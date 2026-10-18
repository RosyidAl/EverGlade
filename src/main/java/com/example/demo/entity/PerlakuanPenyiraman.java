package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "perlakuan_penyiraman")
public class PerlakuanPenyiraman {

    @Id
    @Column(name = "perlakuan_id")
    private Long perlakuanId;

    public Long getPerlakuanId() { return perlakuanId; }
    public void setPerlakuanId(Long perlakuanId) { this.perlakuanId = perlakuanId; }

    @Column(name = "volume_ml", nullable = false)
    private Integer volumeMl;

    public Integer getVolumeMl() { return volumeMl; }
    public void setVolumeMl(Integer volumeMl) { this.volumeMl = volumeMl; }

}
