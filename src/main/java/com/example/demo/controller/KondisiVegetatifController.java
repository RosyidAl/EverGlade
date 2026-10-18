package com.example.demo.controller;

import com.example.demo.entity.KondisiVegetatif;
import com.example.demo.repository.KondisiVegetatifRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kondisi-vegetatif")
public class KondisiVegetatifController {

    @Autowired
    private KondisiVegetatifRepository repository;

    @GetMapping
    public List<KondisiVegetatif> ambilSemua() {
        return repository.findAll();
    }

    @PostMapping
    public KondisiVegetatif tambah(@RequestBody KondisiVegetatif baru) {
        return repository.save(baru);
    }
}