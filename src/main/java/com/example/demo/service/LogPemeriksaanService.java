package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.BusinessValidationException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.LogPemeriksaanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LogPemeriksaanService {

    @Autowired
    private LogPemeriksaanRepository repository;

    @Autowired
    private TanamanService tanamanService;

    @Autowired
    private InventarisBahanService inventarisBahanService;

    public List<LogPemeriksaan> ambilSemua() {
        return repository.findAll();
    }

    public LogPemeriksaan ambilBerdasarkanId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Log pemeriksaan tidak ditemukan dengan ID: " + id));
    }

    public List<LogPemeriksaan> ambilBerdasarkanTanamanId(Long tanamanId) {
        // Validasi tanaman ada
        tanamanService.ambilBerdasarkanId(tanamanId);
        return repository.findByTanamanIdOrderByWaktuPemeriksaanDesc(tanamanId);
    }

    @Transactional
    public LogPemeriksaan simpanLog(LogPemeriksaan log) {
        if (log.getTanaman() == null || log.getTanaman().getId() == null) {
            throw new BusinessValidationException("ID Tanaman wajib diisi pada log pemeriksaan!");
        }

        Tanaman tanaman = tanamanService.ambilBerdasarkanId(log.getTanaman().getId());
        log.setTanaman(tanaman);

        if (log.getWaktuPemeriksaan() == null) {
            log.setWaktuPemeriksaan(LocalDateTime.now());
        }

        // 1. Validasi Kondisi: Maksimal 1 kondisi vegetatif dalam 1 log
        if (log.getDaftarKondisi() != null) {
            long vegetatifCount = log.getDaftarKondisi().stream()
                    .filter(k -> k instanceof KondisiVegetatif)
                    .count();
            if (vegetatifCount > 1) {
                throw new BusinessValidationException("Hanya boleh ada maksimal 1 kondisi vegetatif (pertumbuhan) dalam satu log pemeriksaan!");
            }

            for (KondisiTanaman kondisi : log.getDaftarKondisi()) {
                kondisi.setLogPemeriksaan(log);
            }
        }

        // 2. Validasi & Eksekusi Perlakuan (Potong Stok Otomatis)
        if (log.getDaftarPerlakuan() != null) {
            for (Perlakuan perlakuan : log.getDaftarPerlakuan()) {
                perlakuan.setLogPemeriksaan(log);

                if (perlakuan instanceof PerlakuanPemupukan pemupukan) {
                    prosesPemupukan(pemupukan);
                } else if (perlakuan instanceof PerlakuanPenyemprotan penyemprotan) {
                    prosesPenyemprotan(penyemprotan);
                }
            }
        }

        return repository.save(log);
    }

    private void prosesPemupukan(PerlakuanPemupukan pemupukan) {
        if (pemupukan.getDetailBahanList() == null || pemupukan.getDetailBahanList().isEmpty()) {
            return;
        }

        // 1. Validasi awal (fail fast)
        Set<Long> bahanIdSet = new HashSet<>();
        for (DetailBahanPemupukan detail : pemupukan.getDetailBahanList()) {
            if (detail.getBahan() == null || detail.getBahan().getId() == null) {
                throw new BusinessValidationException("ID Bahan inventaris wajib diisi pada detail pemupukan!");
            }
            Long bahanId = detail.getBahan().getId();
            if (!bahanIdSet.add(bahanId)) {
                throw new BusinessValidationException("Bahan dengan ID " + bahanId + " tidak boleh duplikat dalam satu racikan pemupukan!");
            }
        }

        // 2. Eksekusi pemotongan stok
        for (DetailBahanPemupukan detail : pemupukan.getDetailBahanList()) {
            detail.setPemupukan(pemupukan);
            InventarisBahan bahan = inventarisBahanService.kurangiStok(detail.getBahan().getId(), detail.getTotalTerpakai());
            detail.setBahan(bahan);
        }
    }

    private void prosesPenyemprotan(PerlakuanPenyemprotan penyemprotan) {
        if (penyemprotan.getDetailCampuranList() == null || penyemprotan.getDetailCampuranList().isEmpty()) {
            return;
        }

        // 1. Validasi awal (fail fast)
        Set<Long> bahanIdSet = new HashSet<>();
        for (DetailCampuranSemprot detail : penyemprotan.getDetailCampuranList()) {
            if (detail.getBahan() == null || detail.getBahan().getId() == null) {
                throw new BusinessValidationException("ID Bahan inventaris wajib diisi pada detail campuran semprot!");
            }
            Long bahanId = detail.getBahan().getId();
            if (!bahanIdSet.add(bahanId)) {
                throw new BusinessValidationException("Bahan dengan ID " + bahanId + " tidak boleh duplikat dalam satu racikan semprotan!");
            }
        }

        // 2. Eksekusi pemotongan stok
        for (DetailCampuranSemprot detail : penyemprotan.getDetailCampuranList()) {
            detail.setPenyemprotan(penyemprotan);
            InventarisBahan bahan = inventarisBahanService.kurangiStok(detail.getBahan().getId(), detail.getTotalTerpakai());
            detail.setBahan(bahan);
        }
    }

    /**
     * Solusi Perlakuan Massal:
     * Menyemprot banyak tanaman sekaligus dengan 1 tangki semprot.
     * Stok inventaris dipotong TEPAT 1 KALI untuk total bahan tangki,
     * lalu setiap tanaman mendapatkan log pemeriksaannya masing-masing dengan porsi proporsional.
     */
    @Transactional
    public List<LogPemeriksaan> simpanBatchPenyemprotan(
            List<Long> daftarTanamanId,
            Integer volumeAirSemprotMl,
            String catatan,
            List<DetailCampuranSemprot> campuranBahan) {

        if (daftarTanamanId == null || daftarTanamanId.isEmpty()) {
            throw new BusinessValidationException("Daftar tanaman untuk penyemprotan massal tidak boleh kosong!");
        }

        int jumlahTanaman = daftarTanamanId.size();
        BigDecimal pembagi = BigDecimal.valueOf(jumlahTanaman);

        // 1. Potong stok inventaris SEKALI untuk total bahan dalam tangki
        for (DetailCampuranSemprot item : campuranBahan) {
            inventarisBahanService.kurangiStok(item.getBahan().getId(), item.getTotalTerpakai());
        }

        // 2. Buat log pemeriksaan untuk masing-masing tanaman dengan nilai proporsional
        List<LogPemeriksaan> hasilLog = new ArrayList<>();
        LocalDateTime waktuSekarang = LocalDateTime.now();

        for (Long tanamanId : daftarTanamanId) {
            Tanaman t = tanamanService.ambilBerdasarkanId(tanamanId);

            LogPemeriksaan log = new LogPemeriksaan();
            log.setTanaman(t);
            log.setWaktuPemeriksaan(waktuSekarang);
            log.setCatatanUmum(catatan != null ? catatan : "Penyemprotan massal (" + jumlahTanaman + " tanaman)");

            PerlakuanPenyemprotan semprot = new PerlakuanPenyemprotan();
            semprot.setLogPemeriksaan(log);
            semprot.setVolumeAirSemprotMl(volumeAirSemprotMl / jumlahTanaman);
            semprot.setCatatan("Porsi dari total tangki " + volumeAirSemprotMl + " ml");

            for (DetailCampuranSemprot item : campuranBahan) {
                DetailCampuranSemprot detailPorsi = new DetailCampuranSemprot();
                detailPorsi.setPenyemprotan(semprot);
                detailPorsi.setBahan(item.getBahan());
                detailPorsi.setDosisPerLiter(item.getDosisPerLiter());
                detailPorsi.setTotalTerpakai(item.getTotalTerpakai().divide(pembagi, 2, RoundingMode.HALF_UP));
                semprot.addDetailCampuran(detailPorsi);
            }

            log.addPerlakuan(semprot);
            hasilLog.add(repository.save(log));
        }

        return hasilLog;
    }
}
