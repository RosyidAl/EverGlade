package com.example.demo.controller;

import com.example.demo.dto.request.BatchSemprotRequest;
import com.example.demo.entity.DetailCampuranSemprot;
import com.example.demo.entity.InventarisBahan;
import com.example.demo.entity.LogPemeriksaan;
import com.example.demo.service.InventarisBahanService;
import com.example.demo.service.LogPemeriksaanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/log-pemeriksaan")
public class LogPemeriksaanController {

    @Autowired
    private LogPemeriksaanService service;

    @Autowired
    private InventarisBahanService inventarisBahanService;

    /**
     * Mengambil seluruh log pemeriksaan tanaman.
     *
     * @return daftar log pemeriksaan
     */
    @GetMapping
    public List<LogPemeriksaan> ambilSemua() {
        return service.ambilSemua();
    }

    /**
     * Mengambil log pemeriksaan berdasarkan ID.
     *
     * @param id ID log pemeriksaan
     * @return log yang ditemukan
     */
    @GetMapping("/{id}")
    public LogPemeriksaan ambilBerdasarkanId(@PathVariable Long id) {
        return service.ambilBerdasarkanId(id);
    }

    /**
     * Mengambil log tanaman dari waktu pemeriksaan terbaru.
     *
     * @param tanamanId ID tanaman yang diperiksa
     * @return daftar log tanaman yang diminta
     */
    @GetMapping("/tanaman/{tanamanId}")
    public List<LogPemeriksaan> ambilBerdasarkanTanaman(@PathVariable Long tanamanId) {
        return service.ambilBerdasarkanTanamanId(tanamanId);
    }

    /**
     * Menyimpan log pemeriksaan beserta perlakuan dan pengurangan stoknya.
     *
     * @param baru log pemeriksaan yang akan disimpan
     * @return log tersimpan dengan status HTTP 201
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LogPemeriksaan tambah(@RequestBody LogPemeriksaan baru) {
        return service.simpanLog(baru);
    }

    /**
     * Memetakan bahan permintaan dan menyimpan penyemprotan satu tangki untuk banyak tanaman.
     *
     * @param request tanaman tujuan, volume tangki, catatan, dan total bahan yang digunakan
     * @return log per tanaman dengan status HTTP 201
     */
    @PostMapping("/batch-penyemprotan")
    @ResponseStatus(HttpStatus.CREATED)
    public List<LogPemeriksaan> tambahBatchPenyemprotan(@RequestBody BatchSemprotRequest request) {
        List<DetailCampuranSemprot> daftarBahan = new ArrayList<>();
        if (request.getBahanList() != null) {
            for (BatchSemprotRequest.BahanItem item : request.getBahanList()) {
                InventarisBahan bahan = inventarisBahanService.ambilBerdasarkanId(item.getBahanId());
                DetailCampuranSemprot detail = new DetailCampuranSemprot();
                detail.setBahan(bahan);
                detail.setDosisPerLiter(item.getDosisPerLiter());
                detail.setTotalTerpakai(item.getTotalTerpakai());
                daftarBahan.add(detail);
            }
        }

        return service.simpanBatchPenyemprotan(
                request.getTanamanIds(),
                request.getVolumeAirSemprotMl(),
                request.getCatatan(),
                daftarBahan
        );
    }
}
