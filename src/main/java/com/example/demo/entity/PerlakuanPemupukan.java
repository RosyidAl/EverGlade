package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "perlakuan_pemupukan")
@PrimaryKeyJoinColumn(name = "perlakuan_id")
@DiscriminatorValue("PEMUPUKAN")
public class PerlakuanPemupukan extends Perlakuan {

    @Enumerated(EnumType.STRING)
    @Column(name = "metode_pupuk", nullable = false)
    private MetodePupuk metodePupuk;

    @Column(name = "volume_air_kocor_ml")
    private Integer volumeAirKocorMl = 0;

    @OneToMany(mappedBy = "pemupukan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailBahanPemupukan> detailBahanList = new ArrayList<>();

    public MetodePupuk getMetodePupuk() { return metodePupuk; }
    public void setMetodePupuk(MetodePupuk metodePupuk) { this.metodePupuk = metodePupuk; }

    public Integer getVolumeAirKocorMl() { return volumeAirKocorMl; }
    public void setVolumeAirKocorMl(Integer volumeAirKocorMl) { this.volumeAirKocorMl = volumeAirKocorMl; }

    public List<DetailBahanPemupukan> getDetailBahanList() { return detailBahanList; }
    public void setDetailBahanList(List<DetailBahanPemupukan> detailBahanList) { this.detailBahanList = detailBahanList; }

    public void addDetailBahan(DetailBahanPemupukan detail) {
        if (detail != null) {
            detailBahanList.add(detail);
            detail.setPemupukan(this);
        }
    }
}