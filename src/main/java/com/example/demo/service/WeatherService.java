package com.example.demo.service;

import com.example.demo.dto.weather.OpenMeteoResponse;
import com.example.demo.entity.Cuaca;
import com.example.demo.repository.CuacaRepository;
import com.example.demo.util.WeatherCodeTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

    private final CuacaRepository cuacaRepository;
    private final RestClient restClient;
    private final Double defaultLatitude;
    private final Double defaultLongitude;
    private final String defaultTimezone;

    public WeatherService(
            CuacaRepository cuacaRepository,
            @Value("${weather.api.base-url:https://api.open-meteo.com/v1}") String baseUrl,
            @Value("${weather.default.latitude:-7.5428}") Double defaultLatitude,
            @Value("${weather.default.longitude:110.8439}") Double defaultLongitude,
            @Value("${weather.default.timezone:Asia/Jakarta}") String defaultTimezone) {
        this.cuacaRepository = cuacaRepository;
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.defaultLatitude = defaultLatitude;
        this.defaultLongitude = defaultLongitude;
        this.defaultTimezone = defaultTimezone;
    }

    /**
     * Mengambil cuaca terkini dari Open-Meteo API dan menyimpannya ke tabel cuaca.
     * Jika terjadi kegagalan jaringan atau timeout, method ini mengembalikan null tanpa melempar exception
     * sehingga alur bisnis utama tidak terganggu.
     *
     * @param latitude koordinat latitude (opsional, default lokasi kebun Mojosongo)
     * @param longitude koordinat longitude (opsional, default lokasi kebun Mojosongo)
     * @return objek Cuaca yang tersimpan di DB, atau null jika gagal/offline
     */
    @Transactional
    public Cuaca fetchCurrentWeather(Double latitude, Double longitude) {
        Double lat = (latitude != null) ? latitude : defaultLatitude;
        Double lon = (longitude != null) ? longitude : defaultLongitude;

        try {
            OpenMeteoResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/forecast")
                            .queryParam("latitude", lat)
                            .queryParam("longitude", lon)
                            .queryParam("current", "temperature_2m,relative_humidity_2m,precipitation,weather_code,wind_speed_10m")
                            .queryParam("timezone", defaultTimezone)
                            .build())
                    .retrieve()
                    .body(OpenMeteoResponse.class);

            if (response != null && response.getCurrent() != null) {
                OpenMeteoResponse.CurrentWeather cur = response.getCurrent();
                Cuaca cuaca = new Cuaca();
                cuaca.setWaktuCatat(LocalDateTime.now());
                cuaca.setKondisi(WeatherCodeTranslator.translate(cur.getWeatherCode()));
                cuaca.setSuhuCelsius(cur.getTemperature2m());
                cuaca.setKelembapanPersen(cur.getRelativeHumidity2m());
                cuaca.setCurahHujanMm(cur.getPrecipitation());
                cuaca.setKecepatanAnginKmh(cur.getWindSpeed10m());

                return cuacaRepository.save(cuaca);
            }
        } catch (Exception e) {
            log.warn("Gagal mengambil data cuaca dari Open-Meteo API ({}, {}): {}", lat, lon, e.getMessage());
        }

        return null;
    }

    @Transactional
    public Cuaca simpanManual(Cuaca cuaca) {
        if (cuaca.getWaktuCatat() == null) {
            cuaca.setWaktuCatat(LocalDateTime.now());
        }
        return cuacaRepository.save(cuaca);
    }

    public List<Cuaca> ambilSemua() {
        return cuacaRepository.findAll();
    }
}
