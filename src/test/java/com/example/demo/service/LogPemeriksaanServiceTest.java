package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.BusinessValidationException;
import com.example.demo.repository.LogPemeriksaanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogPemeriksaanServiceTest {

    @Mock
    private LogPemeriksaanRepository repository;

    @Mock
    private TanamanService tanamanService;

    @Mock
    private InventarisBahanService inventarisBahanService;

    @InjectMocks
    private LogPemeriksaanService logService;

    private Tanaman tanaman;
    private InventarisBahan pupuk;

    @BeforeEach
    void setUp() {
        tanaman = new Tanaman();
        tanaman.setId(10L);
        tanaman.setNamaTanaman("Cabai Rawit Merah");
        tanaman.setTanggalTanam(LocalDate.now());

        pupuk = new InventarisBahan();
        pupuk.setId(1L);
        pupuk.setNamaProduk("Pupuk Organik Cair");
        pupuk.setStokSaatIni(new BigDecimal("1000.00"));
    }

    @Test
    @DisplayName("Gagal menyimpan log jika kondisi vegetatif lebih dari 1")
    void testSimpanLog_GagalKondisiVegetatifGanda() {
        when(tanamanService.ambilBerdasarkanId(10L)).thenReturn(tanaman);

        LogPemeriksaan log = new LogPemeriksaan();
        log.setTanaman(tanaman);

        KondisiVegetatif v1 = new KondisiVegetatif();
        v1.setTinggiCm(new BigDecimal("25.00"));
        v1.setFaseTumbuh(FaseTumbuh.VEGETATIF);

        KondisiVegetatif v2 = new KondisiVegetatif();
        v2.setTinggiCm(new BigDecimal("26.00"));
        v2.setFaseTumbuh(FaseTumbuh.VEGETATIF);

        log.setDaftarKondisi(new ArrayList<>(List.of(v1, v2)));

        assertThrows(BusinessValidationException.class, () -> logService.simpanLog(log));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Berhasil simpan log pemupukan dan memicu pemotongan stok")
    void testSimpanLog_PemupukanMemotongStok() {
        when(tanamanService.ambilBerdasarkanId(10L)).thenReturn(tanaman);
        when(repository.save(any(LogPemeriksaan.class))).thenAnswer(i -> i.getArgument(0));

        LogPemeriksaan log = new LogPemeriksaan();
        log.setTanaman(tanaman);

        PerlakuanPemupukan pemupukan = new PerlakuanPemupukan();
        pemupukan.setMetodePupuk(MetodePupuk.KOCOR);
        pemupukan.setVolumeAirKocorMl(1000);

        DetailBahanPemupukan detail = new DetailBahanPemupukan();
        detail.setBahan(pupuk);
        detail.setDosisPerLiterAtauPot(new BigDecimal("5.00"));
        detail.setTotalTerpakai(new BigDecimal("5.00"));

        pemupukan.addDetailBahan(detail);
        log.addPerlakuan(pemupukan);

        LogPemeriksaan saved = logService.simpanLog(log);

        assertNotNull(saved);
        verify(inventarisBahanService).kurangiStok(1L, new BigDecimal("5.00"));
        verify(repository).save(log);
    }

    @Test
    @DisplayName("Gagal jika ada bahan duplikat dalam satu racikan pemupukan")
    void testSimpanLog_BahanDuplikatGagal() {
        when(tanamanService.ambilBerdasarkanId(10L)).thenReturn(tanaman);

        LogPemeriksaan log = new LogPemeriksaan();
        log.setTanaman(tanaman);

        PerlakuanPemupukan pemupukan = new PerlakuanPemupukan();
        pemupukan.setMetodePupuk(MetodePupuk.KOCOR);

        DetailBahanPemupukan d1 = new DetailBahanPemupukan();
        d1.setBahan(pupuk);
        d1.setTotalTerpakai(new BigDecimal("5.00"));

        DetailBahanPemupukan d2 = new DetailBahanPemupukan();
        d2.setBahan(pupuk);
        d2.setTotalTerpakai(new BigDecimal("10.00"));

        pemupukan.addDetailBahan(d1);
        pemupukan.addDetailBahan(d2);
        log.addPerlakuan(pemupukan);

        assertThrows(BusinessValidationException.class, () -> logService.simpanLog(log));
        verify(inventarisBahanService, never()).kurangiStok(any(), any());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Batch penyemprotan memotong stok sekali dan membuat log per tanaman")
    void testBatchPenyemprotan_Berhasil() {
        Tanaman t1 = new Tanaman();
        t1.setId(10L);
        Tanaman t2 = new Tanaman();
        t2.setId(20L);

        when(tanamanService.ambilBerdasarkanId(10L)).thenReturn(t1);
        when(tanamanService.ambilBerdasarkanId(20L)).thenReturn(t2);
        when(repository.save(any(LogPemeriksaan.class))).thenAnswer(i -> i.getArgument(0));

        DetailCampuranSemprot campuran = new DetailCampuranSemprot();
        campuran.setBahan(pupuk);
        campuran.setDosisPerLiter(new BigDecimal("2.00"));
        campuran.setTotalTerpakai(new BigDecimal("10.00"));

        List<LogPemeriksaan> hasil = logService.simpanBatchPenyemprotan(
                List.of(10L, 20L),
                2000,
                "Semprot pencegahan hama",
                List.of(campuran)
        );

        assertEquals(2, hasil.size());
        // Memastikan stok hanya dipotong SEKALI sejumlah total tangki (10.00)
        verify(inventarisBahanService, times(1)).kurangiStok(1L, new BigDecimal("10.00"));
        verify(repository, times(2)).save(any(LogPemeriksaan.class));
    }
}
