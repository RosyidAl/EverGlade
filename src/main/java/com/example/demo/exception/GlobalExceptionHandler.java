package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Mengubah kesalahan sumber daya tidak ditemukan menjadi respons HTTP 404.
     *
     * @param ex kesalahan pencarian sumber daya
     * @return respons kesalahan dengan pesan dari exception
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    /**
     * Mengubah kekurangan stok menjadi respons HTTP 400 dengan rincian persediaan.
     *
     * @param ex kesalahan berisi identitas bahan serta stok tersedia dan dibutuhkan
     * @return respons kesalahan beserta rincian kekurangan stok
     */
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientStock(InsufficientStockException ex) {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("bahanId", ex.getBahanId());
        details.put("namaProduk", ex.getNamaProduk());
        details.put("stokTersedia", ex.getStokTersedia());
        details.put("stokDibutuhkan", ex.getStokDibutuhkan());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), details);
    }

    /**
     * Mengubah pelanggaran aturan bisnis menjadi respons HTTP 400.
     *
     * @param ex kesalahan validasi bisnis
     * @return respons kesalahan dengan pesan validasi
     */
    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(BusinessValidationException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    /**
     * Mengubah argumen tidak valid menjadi respons HTTP 400.
     *
     * @param ex kesalahan argumen
     * @return respons kesalahan dengan pesan dari exception
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    /**
     * Mengubah kesalahan lainnya menjadi respons HTTP 500.
     *
     * @param ex kesalahan yang tidak ditangani secara khusus
     * @return respons kesalahan server yang menyertakan pesan dari exception
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi kesalahan pada server: " + ex.getMessage(), null);
    }

    /**
     * Menyusun respons kesalahan dengan waktu, status, jenis kesalahan, dan pesan.
     *
     * @param status status HTTP respons
     * @param message pesan kesalahan
     * @param details rincian tambahan; null untuk menghilangkan field details
     * @return respons JSON kesalahan dengan status yang diminta
     */
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message, Object details) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        if (details != null) {
            body.put("details", details);
        }
        return ResponseEntity.status(status).body(body);
    }
}
