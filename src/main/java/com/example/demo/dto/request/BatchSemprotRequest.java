package com.example.demo.dto.request;

import java.math.BigDecimal;
import java.util.List;

public class BatchSemprotRequest {

    private List<Long> tanamanIds;
    private Integer volumeAirSemprotMl;
    private String catatan;
    private List<BahanItem> bahanList;

    /** Mengambil ID tanaman tujuan penyemprotan massal. */
    public List<Long> getTanamanIds() { return tanamanIds; }
    /** Mengatur ID tanaman tujuan penyemprotan massal. */
    public void setTanamanIds(List<Long> tanamanIds) { this.tanamanIds = tanamanIds; }

    /** Mengambil volume air seluruh tangki dalam mililiter. */
    public Integer getVolumeAirSemprotMl() { return volumeAirSemprotMl; }
    /** Mengatur volume air seluruh tangki dalam mililiter. */
    public void setVolumeAirSemprotMl(Integer volumeAirSemprotMl) { this.volumeAirSemprotMl = volumeAirSemprotMl; }

    /** Mengambil catatan yang akan digunakan untuk setiap log tanaman. */
    public String getCatatan() { return catatan; }
    /** Mengatur catatan yang akan digunakan untuk setiap log tanaman. */
    public void setCatatan(String catatan) { this.catatan = catatan; }

    /** Mengambil daftar bahan untuk seluruh tangki semprot. */
    public List<BahanItem> getBahanList() { return bahanList; }
    /** Mengatur daftar bahan untuk seluruh tangki semprot. */
    public void setBahanList(List<BahanItem> bahanList) { this.bahanList = bahanList; }

    public static class BahanItem {
        private Long bahanId;
        private BigDecimal dosisPerLiter;
        private BigDecimal totalTerpakai;

        /** Mengambil ID bahan inventaris yang digunakan dalam tangki. */
        public Long getBahanId() { return bahanId; }
        /** Mengatur ID bahan inventaris yang digunakan dalam tangki. */
        public void setBahanId(Long bahanId) { this.bahanId = bahanId; }

        /** Mengambil dosis bahan per liter air dalam satuan stok bahan. */
        public BigDecimal getDosisPerLiter() { return dosisPerLiter; }
        /** Mengatur dosis bahan per liter air dalam satuan stok bahan. */
        public void setDosisPerLiter(BigDecimal dosisPerLiter) { this.dosisPerLiter = dosisPerLiter; }

        /** Mengambil total bahan untuk seluruh tangki dalam satuan stok bahan. */
        public BigDecimal getTotalTerpakai() { return totalTerpakai; }
        /** Mengatur total bahan untuk seluruh tangki dalam satuan stok bahan. */
        public void setTotalTerpakai(BigDecimal totalTerpakai) { this.totalTerpakai = totalTerpakai; }
    }
}
