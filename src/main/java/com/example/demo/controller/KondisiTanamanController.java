package com.example.demo.controller;

import com.example.demo.entity.KondisiTanaman;
import com.example.demo.repository.KondisiTanamanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kondisi-tanaman")
public class KondisiTanamanController {

    @Autowired
    private KondisiTanamanRepository repository;

    @GetMapping
    public List<KondisiTanaman> ambilSemua() {
        return repository.findAll();
    }

    @PostMapping
    public KondisiTanaman tambah(@RequestBody KondisiTanaman baru) {
        return repository.save(baru);
    }
}
