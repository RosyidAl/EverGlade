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

    @GetMapping
    public List<LogPemeriksaan> ambilSemua() {
        return service.ambilSemua();
    }

    @GetMapping("/{id}")
    public LogPemeriksaan ambilBerdasarkanId(@PathVariable Long id) {
        return service.ambilBerdasarkanId(id);
    }

    @GetMapping("/tanaman/{tanamanId}")
    public List<LogPemeriksaan> ambilBerdasarkanTanaman(@PathVariable Long tanamanId) {
        return service.ambilBerdasarkanTanamanId(tanamanId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LogPemeriksaan tambah(@RequestBody LogPemeriksaan baru) {
        return service.simpanLog(baru);
    }

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
