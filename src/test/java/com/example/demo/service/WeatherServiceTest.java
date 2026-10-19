package com.example.demo.service;

import com.example.demo.entity.Cuaca;
import com.example.demo.repository.CuacaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private CuacaRepository cuacaRepository;

    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        // Inisialisasi WeatherService dengan koordinat Mojosongo
        weatherService = new WeatherService(
                cuacaRepository,
                "https://api.open-meteo.com/v1",
                -7.5428,
                110.8439,
                "Asia/Jakarta"
        );
    }

    @Test
    @DisplayName("Berhasil menyimpan data cuaca secara manual")
    void testSimpanManual() {
        Cuaca cuaca = new Cuaca();
        cuaca.setKondisi("Cerah");
        cuaca.setSuhuCelsius(new BigDecimal("32.5"));
        cuaca.setKelembapanPersen(60);

        when(cuacaRepository.save(any(Cuaca.class))).thenAnswer(i -> i.getArgument(0));

        Cuaca result = weatherService.simpanManual(cuaca);

        assertNotNull(result);
        assertNotNull(result.getWaktuCatat());
        assertEquals("Cerah", result.getKondisi());
        verify(cuacaRepository).save(cuaca);
    }

    @Test
    @DisplayName("Mengambil semua riwayat cuaca")
    void testAmbilSemua() {
        Cuaca c1 = new Cuaca();
        c1.setId(1L);
        when(cuacaRepository.findAll()).thenReturn(List.of(c1));

        List<Cuaca> list = weatherService.ambilSemua();

        assertEquals(1, list.size());
        verify(cuacaRepository).findAll();
    }

    @Test
    @DisplayName("Fallback aman saat endpoint API cuaca tidak valid/offline mengembalikan null")
    void testFetchCurrentWeather_OfflineFallback() {
        // Service dengan base URL yang tidak valid untuk simulasi offline/timeout
        WeatherService offlineService = new WeatherService(
                cuacaRepository,
                "http://invalid-host-for-testing-123456.local",
                -7.5428,
                110.8439,
                "Asia/Jakarta"
        );

        Cuaca result = offlineService.fetchCurrentWeather(null, null);

        assertNull(result, "Harus mengembalikan null saat offline tanpa melempar exception");
        verify(cuacaRepository, never()).save(any());
    }
}
