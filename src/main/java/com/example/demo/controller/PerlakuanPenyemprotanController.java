package com.example.demo.controller;

import com.example.demo.entity.PerlakuanPenyemprotan;
import com.example.demo.repository.PerlakuanPenyemprotanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perlakuan-penyemprotan")
public class PerlakuanPenyemprotanController {

    @Autowired
    private PerlakuanPenyemprotanRepository repository;

    @GetMapping
    public List<PerlakuanPenyemprotan> ambilSemua() {
        return repository.findAll();
    }

    @PostMapping
    public PerlakuanPenyemprotan tambah(@RequestBody PerlakuanPenyemprotan baru) {
        return repository.save(baru);
    }
}
