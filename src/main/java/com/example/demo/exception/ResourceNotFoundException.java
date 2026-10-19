package com.example.demo.exception;

public class ResourceNotFoundException extends RuntimeException {
    /**
     * Membuat kesalahan ketika sumber daya yang diminta tidak ditemukan.
     *
     * @param message penjelasan sumber daya yang tidak ditemukan
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
