package com.example.demo.controller;

import com.example.demo.entity.Tanaman;
import com.example.demo.repository.TanamanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tanaman")
public class TanamanController {

    @Autowired
    private TanamanRepository repository;

    @GetMapping
    public List<Tanaman> ambilSemua() {
        return repository.findAll();
    }

    @PostMapping
    public Tanaman tambah(@RequestBody Tanaman baru) {
        return repository.save(baru);
    }
}
