package com.example.demo.controller;

import com.example.demo.entity.PerlakuanPenyiraman;
import com.example.demo.repository.PerlakuanPenyiramanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perlakuan-penyiraman")
public class PerlakuanPenyiramanController {

    @Autowired
    private PerlakuanPenyiramanRepository repository;

    @GetMapping
    public List<PerlakuanPenyiraman> ambilSemua() {
        return repository.findAll();
    }

    @PostMapping
    public PerlakuanPenyiraman tambah(@RequestBody PerlakuanPenyiraman baru) {
        return repository.save(baru);
    }
}
