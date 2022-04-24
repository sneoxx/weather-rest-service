package ru.infinnity.zaraev.weatherrestservice.api;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Интерфейс получающий данные с сервиса OpenWeatherMap
 */
@FeignClient(name = "OpenWeatherMap", url = "https://api.openweathermap.org/data/2.5/weather?appid=224da7ebeb2190f46539cc5e4039a545&units=metric")
public interface FeignClientForOpenWeatherMap {

    @GetMapping(value = "/")
    String getWeatherByCity(@RequestParam String q);

}