package ru.infinnity.zaraev.weatherrestservice.api;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Интерфейс получающий данные с сервиса WeatherApi
 */
@FeignClient(name = "WeatherApi", url = "http://api.weatherapi.com/v1/current.json?key=fe1a824c93d3437687a94559222104")
public interface FeignClientForWeatherApi {

    @GetMapping
    String getWeatherByCity(@RequestParam String query);

}