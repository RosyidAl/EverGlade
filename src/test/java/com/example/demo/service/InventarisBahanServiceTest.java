package com.example.demo.service;

import com.example.demo.entity.BentukFisik;
import com.example.demo.entity.InventarisBahan;
import com.example.demo.entity.SatuanStok;
import com.example.demo.entity.TipeBahan;
import com.example.demo.exception.BusinessValidationException;
import com.example.demo.exception.InsufficientStockException;
import com.example.demo.repository.InventarisBahanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarisBahanServiceTest {

    @Mock
    private InventarisBahanRepository repository;

    @InjectMocks
    private InventarisBahanService service;

    private InventarisBahan bahan;

    /** Menyiapkan bahan aktif dengan stok awal 500 untuk setiap pengujian. */
    @BeforeEach
    void setUp() {
        bahan = new InventarisBahan();
        bahan.setId(1L);
        bahan.setNamaProduk("NPK Cair Mutiara");
        bahan.setTipeBahan(TipeBahan.PUPUK);
        bahan.setBentukFisik(BentukFisik.CAIR);
        bahan.setSatuanStok(SatuanStok.ML);
        bahan.setStokSaatIni(new BigDecimal("500.00"));
        bahan.setIsActive(true);
    }

    /** Memastikan pengurangan stok yang mencukupi menyimpan saldo baru. */
    @Test
    @DisplayName("Berhasil mengurangi stok saat stok mencukupi")
    void testKurangiStok_Berhasil() {
        when(repository.findById(1L)).thenReturn(Optional.of(bahan));
        when(repository.save(any(InventarisBahan.class))).thenAnswer(i -> i.getArgument(0));

        InventarisBahan result = service.kurangiStok(1L, new BigDecimal("150.00"));

        assertNotNull(result);
        assertEquals(new BigDecimal("350.00"), result.getStokSaatIni());
        verify(repository).save(bahan);
    }

    /** Memastikan kekurangan stok melempar exception tanpa menyimpan bahan. */
    @Test
    @DisplayName("Gagal dan melempar InsufficientStockException saat stok kurang")
    void testKurangiStok_StokKurang() {
        when(repository.findById(1L)).thenReturn(Optional.of(bahan));

        assertThrows(InsufficientStockException.class, () -> {
            service.kurangiStok(1L, new BigDecimal("600.00"));
        });

        verify(repository, never()).save(any());
    }

    /** Memastikan bahan yang diarsipkan tidak dapat dikurangi stoknya. */
    @Test
    @DisplayName("Gagal mengurangi stok jika bahan tidak aktif")
    void testKurangiStok_BahanTidakAktif() {
        bahan.setIsActive(false);
        when(repository.findById(1L)).thenReturn(Optional.of(bahan));

        assertThrows(BusinessValidationException.class, () -> {
            service.kurangiStok(1L, new BigDecimal("50.00"));
        });

        verify(repository, never()).save(any());
    }

    /** Memastikan penambahan stok menyimpan jumlah persediaan terbaru. */
    @Test
    @DisplayName("Berhasil menambah stok")
    void testTambahStok_Berhasil() {
        when(repository.findById(1L)).thenReturn(Optional.of(bahan));
        when(repository.save(any(InventarisBahan.class))).thenAnswer(i -> i.getArgument(0));

        InventarisBahan result = service.tambahStok(1L, new BigDecimal("250.00"));

        assertEquals(new BigDecimal("750.00"), result.getStokSaatIni());
        verify(repository).save(bahan);
    }
}
