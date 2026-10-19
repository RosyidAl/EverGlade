package com.example.demo.service;

import com.example.demo.entity.Tanaman;
import com.example.demo.exception.BusinessValidationException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.TanamanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TanamanServiceTest {

    @Mock
    private TanamanRepository repository;

    @InjectMocks
    private TanamanService service;

    private Tanaman tanaman;

    /** Menyiapkan tanaman aktif dengan tanggal tanam hari ini untuk setiap pengujian. */
    @BeforeEach
    void setUp() {
        tanaman = new Tanaman();
        tanaman.setId(1L);
        tanaman.setNamaTanaman("Tomat Beef");
        tanaman.setStatusAktif(true);
        tanaman.setTanggalTanam(LocalDate.now());
    }

    /** Memastikan penonaktifan menyimpan status, tanggal, dan alasan selesai tanaman. */
    @Test
    @DisplayName("Berhasil menonaktifkan tanaman (Soft Delete) beserta tanggal dan alasan")
    void testNonaktifkanTanaman_Berhasil() {
        when(repository.findById(1L)).thenReturn(Optional.of(tanaman));
        when(repository.save(any(Tanaman.class))).thenAnswer(i -> i.getArgument(0));

        Tanaman hasil = service.nonaktifkan(1L, "PANEN_SELESAI");

        assertFalse(hasil.getStatusAktif());
        assertNotNull(hasil.getTanggalSelesai());
        assertEquals("PANEN_SELESAI", hasil.getKeteranganSelesai());
        verify(repository).save(tanaman);
    }

    /** Memastikan penonaktifan tanaman yang sudah tidak aktif ditolak tanpa penyimpanan. */
    @Test
    @DisplayName("Gagal menonaktifkan tanaman jika tanaman sudah non-aktif")
    void testNonaktifkanTanaman_SudahNonaktif() {
        tanaman.setStatusAktif(false);
        when(repository.findById(1L)).thenReturn(Optional.of(tanaman));

        assertThrows(BusinessValidationException.class, () -> service.nonaktifkan(1L, "PANEN_SELESAI"));
        verify(repository, never()).save(any());
    }

    /** Memastikan pencarian ID tanaman yang tidak ada melempar ResourceNotFoundException. */
    @Test
    @DisplayName("Throw ResourceNotFoundException saat tanaman tidak ada")
    void testAmbilBerdasarkanId_NotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.ambilBerdasarkanId(99L));
    }
}
