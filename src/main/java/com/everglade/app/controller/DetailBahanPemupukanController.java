package com.everglade.app.controller;

import com.everglade.app.entity.DetailBahanPemupukan;
import com.everglade.app.repository.DetailBahanPemupukanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detail-bahan-pemupukan")
public class DetailBahanPemupukanController {

    @Autowired
    private DetailBahanPemupukanRepository repository;

    @GetMapping
    public List<DetailBahanPemupukan> ambilSemua() {
        return repository.findAll();
    }

    @PostMapping
    public DetailBahanPemupukan tambah(@RequestBody DetailBahanPemupukan baru) {
        return repository.save(baru);
    }
}
