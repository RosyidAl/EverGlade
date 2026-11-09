package com.everglade.app.controller;

import com.everglade.app.entity.DetailCampuranSemprot;
import com.everglade.app.repository.DetailCampuranSemprotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detail-campuran-semprot")
public class DetailCampuranSemprotController {

    @Autowired
    private DetailCampuranSemprotRepository repository;

    @GetMapping
    public List<DetailCampuranSemprot> ambilSemua() {
        return repository.findAll();
    }

    @PostMapping
    public DetailCampuranSemprot tambah(@RequestBody DetailCampuranSemprot baru) {
        return repository.save(baru);
    }
}
