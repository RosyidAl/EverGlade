package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "perlakuan_penyemprotan")
@PrimaryKeyJoinColumn(name = "perlakuan_id")
@DiscriminatorValue("PENYEMPROTAN")
public class PerlakuanPenyemprotan extends Perlakuan {

    @Column(name = "volume_air_semprot_ml", nullable = false)
    private Integer volumeAirSemprotMl;

    @OneToMany(mappedBy = "penyemprotan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailCampuranSemprot> detailCampuranList = new ArrayList<>();

    public Integer getVolumeAirSemprotMl() { return volumeAirSemprotMl; }
    public void setVolumeAirSemprotMl(Integer volumeAirSemprotMl) { this.volumeAirSemprotMl = volumeAirSemprotMl; }

    public List<DetailCampuranSemprot> getDetailCampuranList() { return detailCampuranList; }
    public void setDetailCampuranList(List<DetailCampuranSemprot> detailCampuranList) { this.detailCampuranList = detailCampuranList; }

    public void addDetailCampuran(DetailCampuranSemprot detail) {
        if (detail != null) {
            detailCampuranList.add(detail);
            detail.setPenyemprotan(this);
        }
    }
}