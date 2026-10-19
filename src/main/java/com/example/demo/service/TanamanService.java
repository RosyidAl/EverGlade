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

    /**
     * Mengambil tanaman dengan filter aktif opsional.
     *
     * @param hanyaAktif true untuk hanya tanaman aktif; false atau null untuk semua tanaman
     * @return daftar tanaman
     */
    public List<Tanaman> ambilSemua(Boolean hanyaAktif) {
        if (Boolean.TRUE.equals(hanyaAktif)) {
            return repository.findByStatusAktif(true);
        }
        return repository.findAll();
    }

    /**
     * Mengambil tanaman berdasarkan ID.
     *
     * @param id ID tanaman
     * @return tanaman yang ditemukan
     * @throws ResourceNotFoundException jika ID tanaman tidak ditemukan
     */
    public Tanaman ambilBerdasarkanId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tanaman tidak ditemukan dengan ID: " + id));
    }

    /**
     * Menyimpan tanaman dengan tanggal hari ini dan status aktif jika belum diisi.
     *
     * @param tanaman tanaman yang akan disimpan
     * @return tanaman yang tersimpan
     */
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

    /**
     * Memperbarui informasi tanaman dengan mempertahankan status dan data penyelesaiannya.
     *
     * @param id ID tanaman
     * @param dataBaru informasi pengganti tanaman
     * @return tanaman yang diperbarui
     * @throws ResourceNotFoundException jika ID tanaman tidak ditemukan
     */
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

    /**
     * Menonaktifkan tanaman serta mencatat tanggal hari ini dan alasan selesai.
     *
     * @param id ID tanaman
     * @param keterangan alasan selesai; null menggunakan SELESAI
     * @return tanaman yang dinonaktifkan
     * @throws ResourceNotFoundException jika ID tanaman tidak ditemukan
     * @throws BusinessValidationException jika tanaman sudah tidak aktif
     */
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

    /**
     * Mengaktifkan kembali tanaman dan menghapus tanggal serta alasan selesai.
     *
     * @param id ID tanaman
     * @return tanaman yang diaktifkan
     * @throws ResourceNotFoundException jika ID tanaman tidak ditemukan
     */
    @Transactional
    public Tanaman aktifkanKembali(Long id) {
        Tanaman existing = ambilBerdasarkanId(id);
        existing.setStatusAktif(true);
        existing.setTanggalSelesai(null);
        existing.setKeteranganSelesai(null);
        return repository.save(existing);
    }
}
