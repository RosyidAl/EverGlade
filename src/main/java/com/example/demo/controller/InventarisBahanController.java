package com.example.demo.controller;

import com.example.demo.entity.InventarisBahan;
import com.example.demo.service.InventarisBahanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/inventaris-bahan")
public class InventarisBahanController {

    @Autowired
    private InventarisBahanService service;

    @GetMapping
    public List<InventarisBahan> ambilSemua(@RequestParam(required = false) Boolean hanyaAktif) {
        return service.ambilSemua(hanyaAktif);
    }

    @GetMapping("/{id}")
    public InventarisBahan ambilBerdasarkanId(@PathVariable Long id) {
        return service.ambilBerdasarkanId(id);
    }

    @GetMapping("/stok-menipis")
    public List<InventarisBahan> ambilStokMenipis(
            @RequestParam(required = false, defaultValue = "100.00") BigDecimal batas) {
        return service.ambilStokMenipis(batas);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventarisBahan tambah(@RequestBody InventarisBahan baru) {
        return service.simpan(baru);
    }

    @PutMapping("/{id}")
    public InventarisBahan perbarui(@PathVariable Long id, @RequestBody InventarisBahan dataBaru) {
        return service.perbarui(id, dataBaru);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> arsipkan(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/tambah-stok")
    public InventarisBahan tambahStok(
            @PathVariable Long id,
            @RequestParam BigDecimal jumlah) {
        return service.tambahStok(id, jumlah);
    }

    @PostMapping("/{id}/kurangi-stok")
    public InventarisBahan kurangiStok(
            @PathVariable Long id,
            @RequestParam BigDecimal jumlah) {
        return service.kurangiStok(id, jumlah);
    }
}
