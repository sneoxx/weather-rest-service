package ru.infinnity.zaraev.weatherrestservice.api;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Интерфейс получающий данные с сервиса WeatherStack
 */
@FeignClient(name = "WeatherStack", url = "http://api.weatherstack.com/current?access_key=3b0d465e4baa775575417e3de3c8e341&units=m")
public interface FeignClientForWeatherStack {

    @GetMapping
    String getWeatherByCity(@RequestParam String query);

}