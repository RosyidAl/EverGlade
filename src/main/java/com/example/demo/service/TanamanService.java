package com.example.demo.service;

import com.example.demo.entity.Tanaman;
import com.example.demo.exception.BusinessValidationException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.TanamanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TanamanService {

    @Autowired
    private TanamanRepository repository;

    public List<Tanaman> ambilSemua(Boolean hanyaAktif) {
        if (Boolean.TRUE.equals(hanyaAktif)) {
            return repository.findByStatusAktif(true);
        }
        return repository.findAll();
    }

    public Tanaman ambilBerdasarkanId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tanaman tidak ditemukan dengan ID: " + id));
    }

    @Transactional
    public Tanaman simpan(Tanaman tanaman) {
        if (tanaman.getTanggalTanam() == null) {
            tanaman.setTanggalTanam(LocalDate.now());
        }
        if (tanaman.getStatusAktif() == null) {
            tanaman.setStatusAktif(true);
        }
        return repository.save(tanaman);
    }

    @Transactional
    public Tanaman perbarui(Long id, Tanaman dataBaru) {
        Tanaman existing = ambilBerdasarkanId(id);
        existing.setNamaTanaman(dataBaru.getNamaTanaman());
        existing.setJenisVarietas(dataBaru.getJenisVarietas());
        existing.setLokasiTanam(dataBaru.getLokasiTanam());
        existing.setTanggalTanam(dataBaru.getTanggalTanam());
        existing.setCatatan(dataBaru.getCatatan());
        return repository.save(existing);
    }

    @Transactional
    public Tanaman nonaktifkan(Long id, String keterangan) {
        Tanaman existing = ambilBerdasarkanId(id);
        if (!Boolean.TRUE.equals(existing.getStatusAktif())) {
            throw new BusinessValidationException("Tanaman ini sudah dalam status non-aktif!");
        }
        existing.setStatusAktif(false);
        existing.setTanggalSelesai(LocalDate.now());
        existing.setKeteranganSelesai(keterangan != null ? keterangan : "SELESAI");
        return repository.save(existing);
    }

    @Transactional
    public Tanaman aktifkanKembali(Long id) {
        Tanaman existing = ambilBerdasarkanId(id);
        existing.setStatusAktif(true);
        existing.setTanggalSelesai(null);
        existing.setKeteranganSelesai(null);
        return repository.save(existing);
    }
}
