package com.example.demo.exception;

public class BusinessValidationException extends RuntimeException {
    /**
     * Membuat kesalahan untuk pelanggaran aturan bisnis.
     *
     * @param message penjelasan aturan bisnis yang dilanggar
     */
    public BusinessValidationException(String message) {
        super(message);
    }
}
