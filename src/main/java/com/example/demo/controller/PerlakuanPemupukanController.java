package com.example.demo.controller;

import com.example.demo.entity.PerlakuanPemupukan;
import com.example.demo.repository.PerlakuanPemupukanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perlakuan-pemupukan")
public class PerlakuanPemupukanController {

    @Autowired
    private PerlakuanPemupukanRepository repository;

    @GetMapping
    public List<PerlakuanPemupukan> ambilSemua() {
        return repository.findAll();
    }

    @PostMapping
    public PerlakuanPemupukan tambah(@RequestBody PerlakuanPemupukan baru) {
        return repository.save(baru);
    }
}
