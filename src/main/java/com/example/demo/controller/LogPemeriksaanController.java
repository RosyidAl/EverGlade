package com.example.demo.controller;

import com.example.demo.entity.LogPemeriksaan;
import com.example.demo.repository.LogPemeriksaanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/log-pemeriksaan")
public class LogPemeriksaanController {

    @Autowired
    private LogPemeriksaanRepository repository;

    @GetMapping
    public List<LogPemeriksaan> ambilSemua() {
        return repository.findAll();
    }

    @PostMapping
    public LogPemeriksaan tambah(@RequestBody LogPemeriksaan baru) {
        return repository.save(baru);
    }
}
