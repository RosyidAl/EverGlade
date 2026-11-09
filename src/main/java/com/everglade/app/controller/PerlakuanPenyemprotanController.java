package com.everglade.app.controller;

import com.everglade.app.entity.PerlakuanPenyemprotan;
import com.everglade.app.repository.PerlakuanPenyemprotanRepository;
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
