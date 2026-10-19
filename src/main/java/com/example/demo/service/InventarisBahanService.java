package com.example.demo.service;

import com.example.demo.entity.InventarisBahan;
import com.example.demo.exception.BusinessValidationException;
import com.example.demo.exception.InsufficientStockException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.InventarisBahanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InventarisBahanService {

    @Autowired
    private InventarisBahanRepository repository;

    public List<InventarisBahan> ambilSemua(Boolean hanyaAktif) {
        if (Boolean.TRUE.equals(hanyaAktif)) {
            return repository.findByIsActive(true);
        }
        return repository.findAll();
    }

    public InventarisBahan ambilBerdasarkanId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventaris bahan tidak ditemukan dengan ID: " + id));
    }

    @Transactional
    public InventarisBahan simpan(InventarisBahan bahan) {
        if (bahan.getStokSaatIni() == null) {
            bahan.setStokSaatIni(BigDecimal.ZERO);
        }
        if (bahan.getStokSaatIni().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessValidationException("Stok awal tidak boleh bernilai negatif!");
        }
        if (bahan.getIsActive() == null) {
            bahan.setIsActive(true);
        }
        return repository.save(bahan);
    }

    @Transactional
    public InventarisBahan perbarui(Long id, InventarisBahan dataBaru) {
        InventarisBahan existing = ambilBerdasarkanId(id);
        existing.setNamaProduk(dataBaru.getNamaProduk());
        existing.setTipeBahan(dataBaru.getTipeBahan());
        existing.setKategoriDetail(dataBaru.getKategoriDetail());
        existing.setBentukFisik(dataBaru.getBentukFisik());
        existing.setSatuanStok(dataBaru.getSatuanStok());
        existing.setTanggalKadaluarsa(dataBaru.getTanggalKadaluarsa());
        existing.setCatatan(dataBaru.getCatatan());
        return repository.save(existing);
    }

    @Transactional
    public void softDelete(Long id) {
        InventarisBahan existing = ambilBerdasarkanId(id);
        existing.setIsActive(false);
        repository.save(existing);
    }

    @Transactional
    public InventarisBahan kurangiStok(Long bahanId, BigDecimal jumlah) {
        if (jumlah == null || jumlah.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessValidationException("Jumlah pengurangan stok harus lebih besar dari 0!");
        }

        InventarisBahan bahan = ambilBerdasarkanId(bahanId);

        if (!Boolean.TRUE.equals(bahan.getIsActive())) {
            throw new BusinessValidationException("Bahan '" + bahan.getNamaProduk() + "' sudah tidak aktif / diarsipkan!");
        }

        if (bahan.getStokSaatIni().compareTo(jumlah) < 0) {
            throw new InsufficientStockException(bahanId, bahan.getNamaProduk(), bahan.getStokSaatIni(), jumlah);
        }

        bahan.setStokSaatIni(bahan.getStokSaatIni().subtract(jumlah));
        return repository.save(bahan);
    }

    @Transactional
    public InventarisBahan tambahStok(Long bahanId, BigDecimal jumlah) {
        if (jumlah == null || jumlah.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessValidationException("Jumlah penambahan stok harus lebih besar dari 0!");
        }

        InventarisBahan bahan = ambilBerdasarkanId(bahanId);
        bahan.setStokSaatIni(bahan.getStokSaatIni().add(jumlah));
        return repository.save(bahan);
    }

    public List<InventarisBahan> ambilStokMenipis(BigDecimal batasMinimal) {
        return repository.findByIsActiveAndStokSaatIniLessThanEqual(true, batasMinimal);
    }
}
