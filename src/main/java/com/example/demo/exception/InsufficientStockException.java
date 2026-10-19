package com.example.demo.exception;

import java.math.BigDecimal;

public class InsufficientStockException extends RuntimeException {

    private final Long bahanId;
    private final String namaProduk;
    private final BigDecimal stokTersedia;
    private final BigDecimal stokDibutuhkan;

    /**
     * Membuat kesalahan kekurangan stok beserta rincian bahan dan jumlahnya.
     *
     * @param bahanId ID bahan inventaris
     * @param namaProduk nama bahan yang kekurangan stok
     * @param stokTersedia jumlah yang tersedia dalam satuan stok bahan
     * @param stokDibutuhkan jumlah yang diminta dalam satuan stok bahan
     */
    public InsufficientStockException(Long bahanId, String namaProduk, BigDecimal stokTersedia, BigDecimal stokDibutuhkan) {
        super(String.format("Stok untuk bahan '%s' (ID: %d) tidak mencukupi! Tersedia: %s, Dibutuhkan: %s",
                namaProduk, bahanId, stokTersedia, stokDibutuhkan));
        this.bahanId = bahanId;
        this.namaProduk = namaProduk;
        this.stokTersedia = stokTersedia;
        this.stokDibutuhkan = stokDibutuhkan;
    }

    /** Mengambil ID bahan yang stoknya tidak mencukupi. */
    public Long getBahanId() { return bahanId; }
    /** Mengambil nama produk yang stoknya tidak mencukupi. */
    public String getNamaProduk() { return namaProduk; }
    /** Mengambil jumlah stok yang tersedia saat kesalahan terjadi. */
    public BigDecimal getStokTersedia() { return stokTersedia; }
    /** Mengambil jumlah stok yang diminta oleh operasi yang gagal. */
    public BigDecimal getStokDibutuhkan() { return stokDibutuhkan; }
}
