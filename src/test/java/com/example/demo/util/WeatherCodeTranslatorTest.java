package com.example.demo.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherCodeTranslatorTest {

    @Test
    @DisplayName("Penerjemahan kode cuaca WMO dasar")
    void testTranslate_KodeUmum() {
        assertEquals("Cerah", WeatherCodeTranslator.translate(0));
        assertEquals("Cerah Berawan", WeatherCodeTranslator.translate(1));
        assertEquals("Sebagian Berawan", WeatherCodeTranslator.translate(2));
        assertEquals("Mendung", WeatherCodeTranslator.translate(3));
        assertEquals("Berkabut", WeatherCodeTranslator.translate(45));
        assertEquals("Hujan Ringan", WeatherCodeTranslator.translate(61));
        assertEquals("Hujan Sedang", WeatherCodeTranslator.translate(63));
        assertEquals("Hujan Lebat", WeatherCodeTranslator.translate(65));
        assertEquals("Badai Petir", WeatherCodeTranslator.translate(95));
    }

    @Test
    @DisplayName("Penanganan kode null atau tidak dikenal")
    void testTranslate_KodeNullAtauUnknown() {
        assertEquals("Tidak Diketahui", WeatherCodeTranslator.translate(null));
        assertEquals("Cuaca Khusus (999)", WeatherCodeTranslator.translate(999));
    }
}
