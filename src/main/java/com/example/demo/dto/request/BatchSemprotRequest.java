package com.example.demo.dto.request;

import java.math.BigDecimal;
import java.util.List;

public class BatchSemprotRequest {

    private List<Long> tanamanIds;
    private Integer volumeAirSemprotMl;
    private String catatan;
    private List<BahanItem> bahanList;

    public List<Long> getTanamanIds() { return tanamanIds; }
    public void setTanamanIds(List<Long> tanamanIds) { this.tanamanIds = tanamanIds; }

    public Integer getVolumeAirSemprotMl() { return volumeAirSemprotMl; }
    public void setVolumeAirSemprotMl(Integer volumeAirSemprotMl) { this.volumeAirSemprotMl = volumeAirSemprotMl; }

    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }

    public List<BahanItem> getBahanList() { return bahanList; }
    public void setBahanList(List<BahanItem> bahanList) { this.bahanList = bahanList; }

    public static class BahanItem {
        private Long bahanId;
        private BigDecimal dosisPerLiter;
        private BigDecimal totalTerpakai;

        public Long getBahanId() { return bahanId; }
        public void setBahanId(Long bahanId) { this.bahanId = bahanId; }

        public BigDecimal getDosisPerLiter() { return dosisPerLiter; }
        public void setDosisPerLiter(BigDecimal dosisPerLiter) { this.dosisPerLiter = dosisPerLiter; }

        public BigDecimal getTotalTerpakai() { return totalTerpakai; }
        public void setTotalTerpakai(BigDecimal totalTerpakai) { this.totalTerpakai = totalTerpakai; }
    }
}
