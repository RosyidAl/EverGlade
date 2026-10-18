package com.example.demo.controller;

import com.example.demo.entity.Cuaca;
import com.example.demo.repository.CuacaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuaca")
public class CuacaController {

    @Autowired
    private CuacaRepository repository;

    @GetMapping
    public List<Cuaca> ambilSemua() {
        return repository.findAll();
    }

    @PostMapping
    public Cuaca tambah(@RequestBody Cuaca baru) {
        return repository.save(baru);
    }
}
