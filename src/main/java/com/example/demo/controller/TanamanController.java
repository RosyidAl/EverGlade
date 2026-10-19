package com.example.demo.controller;

import com.example.demo.entity.Tanaman;
import com.example.demo.service.TanamanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tanaman")
public class TanamanController {

    @Autowired
    private TanamanService service;

    /**
     * Mengambil daftar tanaman dengan filter aktif opsional.
     *
     * @param hanyaAktif true untuk hanya mengambil tanaman aktif; false atau null untuk semua tanaman
     * @return daftar tanaman
     */
    @GetMapping
    public List<Tanaman> ambilSemua(@RequestParam(required = false) Boolean hanyaAktif) {
        return service.ambilSemua(hanyaAktif);
    }

    /**
     * Mengambil tanaman berdasarkan ID.
     *
     * @param id ID tanaman
     * @return tanaman yang ditemukan
     */
    @GetMapping("/{id}")
    public Tanaman ambilBerdasarkanId(@PathVariable Long id) {
        return service.ambilBerdasarkanId(id);
    }

    /**
     * Menyimpan tanaman dan merespons dengan status HTTP 201.
     *
     * @param baru data tanaman yang akan disimpan
     * @return tanaman yang tersimpan
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tanaman tambah(@RequestBody Tanaman baru) {
        return service.simpan(baru);
    }

    /**
     * Memperbarui informasi tanaman tanpa mengubah status atau data penyelesaiannya.
     *
     * @param id ID tanaman
     * @param dataBaru informasi pengganti tanaman
     * @return tanaman yang diperbarui
     */
    @PutMapping("/{id}")
    public Tanaman perbarui(@PathVariable Long id, @RequestBody Tanaman dataBaru) {
        return service.perbarui(id, dataBaru);
    }

    /**
     * Menonaktifkan tanaman serta mencatat tanggal dan alasan selesai.
     *
     * @param id ID tanaman
     * @param keterangan alasan selesai; nilai bawaan parameter HTTP adalah SELESAI
     * @return respons HTTP 200 berisi tanaman yang dinonaktifkan
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Tanaman> nonaktifkan(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "SELESAI") String keterangan) {
        Tanaman nonaktif = service.nonaktifkan(id, keterangan);
        return ResponseEntity.ok(nonaktif);
    }

    /**
     * Mengaktifkan kembali tanaman dan menghapus tanggal serta alasan selesai.
     *
     * @param id ID tanaman
     * @return respons HTTP 200 berisi tanaman yang diaktifkan
     */
    @PatchMapping("/{id}/aktifkan")
    public ResponseEntity<Tanaman> aktifkanKembali(@PathVariable Long id) {
        Tanaman aktif = service.aktifkanKembali(id);
        return ResponseEntity.ok(aktif);
    }
}
