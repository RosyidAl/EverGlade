package com.example.demo.controller;

import com.example.demo.entity.DetailCampuranSemprot;
import com.example.demo.repository.DetailCampuranSemprotRepository;
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
