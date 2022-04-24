package ru.infinnity.zaraev.weatherrestservice.service;

import org.springframework.stereotype.Service;
import ru.infinnity.zaraev.weatherrestservice.entity.TemperatureInCity;

import java.util.List;
import java.util.Map;

/**
 * Интерфейс для сервисного класса
 */
@Service
public interface WeatherRestService {

    String getWeatherByCityFromOpenWeatherMap(String city, String country, String date);

    String getWeatherByCityFromWeatherApi(String city, String country, String date);

    String getWeatherByCityFromWeatherStack(String city, String country, String date);

    List<TemperatureInCity> getAllAverageTemperatureValuesInACityByDate(String city, String country, String date);

    TemperatureInCity getLastTemperatureByCity(String city, String country);

    Map<String, String> getMyResponseForOpenWeatherMap(String string);

    Map<String, String> getMyResponseForWeatherApi(String string);

    Map<String, String> getMyResponseForWeatherStack(String string);

}
