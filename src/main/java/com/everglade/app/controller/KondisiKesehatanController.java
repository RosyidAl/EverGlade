package com.everglade.app.controller;

import com.everglade.app.entity.KondisiKesehatan;
import com.everglade.app.repository.KondisiKesehatanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kondisi-kesehatan")
public class KondisiKesehatanController {

    @Autowired
    private KondisiKesehatanRepository repository;

    @GetMapping
    public List<KondisiKesehatan> ambilSemua() {
        return repository.findAll();
    }

    @PostMapping
    public KondisiKesehatan tambah(@RequestBody KondisiKesehatan baru) {
        return repository.save(baru);
    }
}