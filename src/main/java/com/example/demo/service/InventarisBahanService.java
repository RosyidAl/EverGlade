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

    /**
     * Mengambil bahan inventaris dengan filter aktif opsional.
     *
     * @param hanyaAktif true untuk hanya bahan aktif; false atau null untuk semua bahan
     * @return daftar bahan inventaris
     */
    public List<InventarisBahan> ambilSemua(Boolean hanyaAktif) {
        if (Boolean.TRUE.equals(hanyaAktif)) {
            return repository.findByIsActive(true);
        }
        return repository.findAll();
    }

    /**
     * Mengambil bahan inventaris berdasarkan ID.
     *
     * @param id ID bahan inventaris
     * @return bahan yang ditemukan
     * @throws ResourceNotFoundException jika ID bahan tidak ditemukan
     */
    public InventarisBahan ambilBerdasarkanId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventaris bahan tidak ditemukan dengan ID: " + id));
    }

    /**
     * Menyimpan bahan dengan stok awal nol dan status aktif jika nilainya belum diisi.
     *
     * @param bahan bahan yang akan disimpan; stok awal harus nonnegatif
     * @return bahan yang tersimpan
     * @throws BusinessValidationException jika stok awal negatif
     */
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

    /**
     * Memperbarui informasi bahan dengan mempertahankan stok dan status aktifnya.
     *
     * @param id ID bahan inventaris
     * @param dataBaru informasi pengganti bahan
     * @return bahan yang diperbarui
     * @throws ResourceNotFoundException jika ID bahan tidak ditemukan
     */
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

    /**
     * Mengarsipkan bahan dengan mengubah status aktif menjadi false.
     *
     * @param id ID bahan yang akan diarsipkan
     * @throws ResourceNotFoundException jika ID bahan tidak ditemukan
     */
    @Transactional
    public void softDelete(Long id) {
        InventarisBahan existing = ambilBerdasarkanId(id);
        existing.setIsActive(false);
        repository.save(existing);
    }

    /**
     * Mengurangi stok bahan aktif setelah memvalidasi jumlah dan ketersediaannya.
     *
     * @param bahanId ID bahan inventaris
     * @param jumlah jumlah positif dalam satuan stok bahan
     * @return bahan dengan stok yang telah dikurangi
     * @throws BusinessValidationException jika jumlah tidak positif atau bahan tidak aktif
     * @throws ResourceNotFoundException jika ID bahan tidak ditemukan
     * @throws InsufficientStockException jika stok tersedia kurang dari jumlah yang diminta
     */
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

    /**
     * Menambahkan jumlah positif ke stok bahan.
     *
     * @param bahanId ID bahan inventaris
     * @param jumlah jumlah tambahan dalam satuan stok bahan
     * @return bahan dengan stok yang telah ditambah
     * @throws BusinessValidationException jika jumlah null atau tidak positif
     * @throws ResourceNotFoundException jika ID bahan tidak ditemukan
     */
    @Transactional
    public InventarisBahan tambahStok(Long bahanId, BigDecimal jumlah) {
        if (jumlah == null || jumlah.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessValidationException("Jumlah penambahan stok harus lebih besar dari 0!");
        }

        InventarisBahan bahan = ambilBerdasarkanId(bahanId);
        bahan.setStokSaatIni(bahan.getStokSaatIni().add(jumlah));
        return repository.save(bahan);
    }

    /**
     * Mengambil bahan aktif dengan stok kurang dari atau sama dengan batas.
     *
     * @param batasMinimal batas atas stok yang dianggap menipis
     * @return daftar bahan aktif yang stoknya menipis
     */
    public List<InventarisBahan> ambilStokMenipis(BigDecimal batasMinimal) {
        return repository.findByIsActiveAndStokSaatIniLessThanEqual(true, batasMinimal);
    }
}
