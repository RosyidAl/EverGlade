package com.example.demo.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class OpenMeteoResponse {

    private Double latitude;
    private Double longitude;
    private String timezone;
    private CurrentWeather current;

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }

    public CurrentWeather getCurrent() { return current; }
    public void setCurrent(CurrentWeather current) { this.current = current; }

    public static class CurrentWeather {
        private String time;

        @JsonProperty("temperature_2m")
        private BigDecimal temperature2m;

        @JsonProperty("relative_humidity_2m")
        private Integer relativeHumidity2m;

        private BigDecimal precipitation;

        @JsonProperty("weather_code")
        private Integer weatherCode;

        @JsonProperty("wind_speed_10m")
        private BigDecimal windSpeed10m;

        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }

        public BigDecimal getTemperature2m() { return temperature2m; }
        public void setTemperature2m(BigDecimal temperature2m) { this.temperature2m = temperature2m; }

        public Integer getRelativeHumidity2m() { return relativeHumidity2m; }
        public void setRelativeHumidity2m(Integer relativeHumidity2m) { this.relativeHumidity2m = relativeHumidity2m; }

        public BigDecimal getPrecipitation() { return precipitation; }
        public void setPrecipitation(BigDecimal precipitation) { this.precipitation = precipitation; }

        public Integer getWeatherCode() { return weatherCode; }
        public void setWeatherCode(Integer weatherCode) { this.weatherCode = weatherCode; }

        public BigDecimal getWindSpeed10m() { return windSpeed10m; }
        public void setWindSpeed10m(BigDecimal windSpeed10m) { this.windSpeed10m = windSpeed10m; }
    }
}
