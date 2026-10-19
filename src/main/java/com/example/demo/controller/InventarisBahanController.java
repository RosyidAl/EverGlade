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

    /**
     * Mengambil daftar bahan inventaris dengan filter aktif opsional.
     *
     * @param hanyaAktif true untuk hanya mengambil bahan aktif; false atau null untuk semua bahan
     * @return daftar bahan inventaris
     */
    @GetMapping
    public List<InventarisBahan> ambilSemua(@RequestParam(required = false) Boolean hanyaAktif) {
        return service.ambilSemua(hanyaAktif);
    }

    /**
     * Mengambil bahan inventaris berdasarkan ID.
     *
     * @param id ID bahan inventaris
     * @return bahan yang ditemukan
     */
    @GetMapping("/{id}")
    public InventarisBahan ambilBerdasarkanId(@PathVariable Long id) {
        return service.ambilBerdasarkanId(id);
    }

    /**
     * Mengambil bahan aktif dengan stok kurang dari atau sama dengan batas.
     *
     * @param batas batas stok; nilai bawaan parameter HTTP adalah 100.00
     * @return daftar bahan yang stoknya menipis
     */
    @GetMapping("/stok-menipis")
    public List<InventarisBahan> ambilStokMenipis(
            @RequestParam(required = false, defaultValue = "100.00") BigDecimal batas) {
        return service.ambilStokMenipis(batas);
    }

    /**
     * Menyimpan bahan inventaris dan merespons dengan status HTTP 201.
     *
     * @param baru data bahan yang akan disimpan
     * @return bahan yang tersimpan
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventarisBahan tambah(@RequestBody InventarisBahan baru) {
        return service.simpan(baru);
    }

    /**
     * Memperbarui informasi bahan tanpa mengubah stok atau status aktifnya.
     *
     * @param id ID bahan inventaris
     * @param dataBaru informasi pengganti bahan
     * @return bahan yang diperbarui
     */
    @PutMapping("/{id}")
    public InventarisBahan perbarui(@PathVariable Long id, @RequestBody InventarisBahan dataBaru) {
        return service.perbarui(id, dataBaru);
    }

    /**
     * Mengarsipkan bahan inventaris dengan menonaktifkannya.
     *
     * @param id ID bahan inventaris
     * @return respons HTTP 204 tanpa isi
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> arsipkan(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Menambahkan jumlah positif ke stok bahan inventaris.
     *
     * @param id ID bahan inventaris
     * @param jumlah jumlah tambahan dalam satuan stok bahan
     * @return bahan dengan stok terbaru
     */
    @PostMapping("/{id}/tambah-stok")
    public InventarisBahan tambahStok(
            @PathVariable Long id,
            @RequestParam BigDecimal jumlah) {
        return service.tambahStok(id, jumlah);
    }

    /**
     * Mengurangi stok bahan aktif setelah memeriksa ketersediaannya.
     *
     * @param id ID bahan inventaris
     * @param jumlah jumlah positif dalam satuan stok bahan
     * @return bahan dengan stok terbaru
     */
    @PostMapping("/{id}/kurangi-stok")
    public InventarisBahan kurangiStok(
            @PathVariable Long id,
            @RequestParam BigDecimal jumlah) {
        return service.kurangiStok(id, jumlah);
    }
}
