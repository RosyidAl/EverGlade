package com.everglade.app.controller;

import com.everglade.app.entity.Perlakuan;
import com.everglade.app.repository.PerlakuanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perlakuan")
public class PerlakuanController {

    @Autowired
    private PerlakuanRepository repository;

    @GetMapping
    public List<Perlakuan> ambilSemua() {
        return repository.findAll();
    }

    @PostMapping
    public Perlakuan tambah(@RequestBody Perlakuan baru) {
        return repository.save(baru);
    }
}
