package com.example.demo.controller;

import com.example.demo.entity.InventarisBahan;
import com.example.demo.repository.InventarisBahanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventaris-bahan")
public class InventarisBahanController {

    @Autowired
    private InventarisBahanRepository repository;

    @GetMapping
    public List<InventarisBahan> ambilSemua() {
        return repository.findAll();
    }

    @PostMapping
    public InventarisBahan tambah(@RequestBody InventarisBahan baru) {
        return repository.save(baru);
    }
}
