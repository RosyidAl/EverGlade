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

    @GetMapping
    public List<Tanaman> ambilSemua(@RequestParam(required = false) Boolean hanyaAktif) {
        return service.ambilSemua(hanyaAktif);
    }

    @GetMapping("/{id}")
    public Tanaman ambilBerdasarkanId(@PathVariable Long id) {
        return service.ambilBerdasarkanId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tanaman tambah(@RequestBody Tanaman baru) {
        return service.simpan(baru);
    }

    @PutMapping("/{id}")
    public Tanaman perbarui(@PathVariable Long id, @RequestBody Tanaman dataBaru) {
        return service.perbarui(id, dataBaru);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Tanaman> nonaktifkan(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "SELESAI") String keterangan) {
        Tanaman nonaktif = service.nonaktifkan(id, keterangan);
        return ResponseEntity.ok(nonaktif);
    }

    @PatchMapping("/{id}/aktifkan")
    public ResponseEntity<Tanaman> aktifkanKembali(@PathVariable Long id) {
        Tanaman aktif = service.aktifkanKembali(id);
        return ResponseEntity.ok(aktif);
    }
}
