package com.example.demo.controller;

import com.example.demo.entity.Cuaca;
import com.example.demo.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuaca")
public class CuacaController {

    @Autowired
    private WeatherService weatherService;

    /**
     * Mengambil riwayat seluruh rekaman cuaca di database.
     *
     * @return daftar rekaman cuaca
     */
    @GetMapping
    public List<Cuaca> ambilSemua() {
        return weatherService.ambilSemua();
    }

    /**
     * Mengambil dan menampilkan data cuaca terkini dari Open-Meteo API.
     * Lokasi default mengarah ke Jl. Jaya Wijaya, Mojosongo, Banjarsari, Surakarta.
     *
     * @param latitude koordinat latitude opsional
     * @param longitude koordinat longitude opsional
     * @return data cuaca terkini yang berhasil disinkronkan dan disimpan
     */
    @GetMapping("/current")
    public ResponseEntity<Cuaca> cuacaTerkini(
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude) {
        Cuaca cuaca = weatherService.fetchCurrentWeather(latitude, longitude);
        if (cuaca != null) {
            return ResponseEntity.ok(cuaca);
        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    /**
     * Memicu sinkronisasi manual cuaca terkini dari Open-Meteo dan menyimpannya ke database.
     *
     * @param latitude koordinat latitude opsional
     * @param longitude koordinat longitude opsional
     * @return objek Cuaca yang baru tersimpan
     */
    @PostMapping("/sync")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Cuaca> sinkronkanCuaca(
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude) {
        Cuaca cuaca = weatherService.fetchCurrentWeather(latitude, longitude);
        if (cuaca != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(cuaca);
        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    /**
     * Menyimpan data cuaca secara manual jika pengguna ingin mencatat sendiri.
     *
     * @param baru data cuaca manual
     * @return entitas cuaca tersimpan
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cuaca tambahManual(@RequestBody Cuaca baru) {
        return weatherService.simpanManual(baru);
    }
}
