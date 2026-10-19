package com.example.demo.util;

public final class WeatherCodeTranslator {

    private WeatherCodeTranslator() {
        // utility class
    }

    /**
     * Menerjemahkan kode cuaca WMO (World Meteorological Organization) ke deskripsi Bahasa Indonesia.
     *
     * @param code kode cuaca WMO dari Open-Meteo
     * @return deskripsi kondisi cuaca
     */
    public static String translate(Integer code) {
        if (code == null) {
            return "Tidak Diketahui";
        }

        return switch (code) {
            case 0 -> "Cerah";
            case 1 -> "Cerah Berawan";
            case 2 -> "Sebagian Berawan";
            case 3 -> "Mendung";
            case 45, 48 -> "Berkabut";
            case 51 -> "Gerimis Ringan";
            case 53 -> "Gerimis Sedang";
            case 55 -> "Gerimis Lebat";
            case 56, 57 -> "Gerimis Dingin";
            case 61 -> "Hujan Ringan";
            case 63 -> "Hujan Sedang";
            case 65 -> "Hujan Lebat";
            case 66, 67 -> "Hujan Dingin";
            case 71, 73, 75, 77 -> "Salju";
            case 80 -> "Hujan Deras Ringan (Showers)";
            case 81 -> "Hujan Deras Sedang (Showers)";
            case 82 -> "Hujan Deras Sangat Lebat (Showers)";
            case 85, 86 -> "Hujan Salju Lebat";
            case 95 -> "Badai Petir";
            case 96, 99 -> "Badai Petir dan Hujan Es";
            default -> "Cuaca Khusus (" + code + ")";
        };
    }
}
