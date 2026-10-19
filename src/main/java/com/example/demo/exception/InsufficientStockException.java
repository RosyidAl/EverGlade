package com.example.demo.exception;

import java.math.BigDecimal;

public class InsufficientStockException extends RuntimeException {

    private final Long bahanId;
    private final String namaProduk;
    private final BigDecimal stokTersedia;
    private final BigDecimal stokDibutuhkan;

    public InsufficientStockException(Long bahanId, String namaProduk, BigDecimal stokTersedia, BigDecimal stokDibutuhkan) {
        super(String.format("Stok untuk bahan '%s' (ID: %d) tidak mencukupi! Tersedia: %s, Dibutuhkan: %s",
                namaProduk, bahanId, stokTersedia, stokDibutuhkan));
        this.bahanId = bahanId;
        this.namaProduk = namaProduk;
        this.stokTersedia = stokTersedia;
        this.stokDibutuhkan = stokDibutuhkan;
    }

    public Long getBahanId() { return bahanId; }
    public String getNamaProduk() { return namaProduk; }
    public BigDecimal getStokTersedia() { return stokTersedia; }
    public BigDecimal getStokDibutuhkan() { return stokDibutuhkan; }
}
