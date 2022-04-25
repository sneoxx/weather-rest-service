package ru.infinnity.zaraev.weatherrestservice.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.infinnity.zaraev.weatherrestservice.api.dto.TemperatureInCityDTO;

import java.util.List;
import java.util.Map;

public interface TemperatureInCityResource {

    /**
     * REST-ендпоинт для получения температуры по городу,стране и дате из сервиса OpenWeatherMap
     */
    @RequestMapping(value = "/OpenWeatherMap/{city}/{country}/{date}", method = RequestMethod.GET)
    Map<String, String> getTemperatureInCityFromOpenWeatherMap(@PathVariable String city, @PathVariable String country, @PathVariable String date);

    /**
     * REST-ендпоинт для получения температуры по городу,стране и дате из сервиса OpenWeatherMap
     */
    @RequestMapping(value = "/WeatherApi/{city}/{country}/{date}", method = RequestMethod.GET)
    Map<String, String> getTemperatureInCityFromWeatherApi(@PathVariable String city, @PathVariable String country, @PathVariable String date);

    /**
     * REST-ендпоинт для получения температуры по городу,стране и дате из сервиса WeatherStack
     */
    @RequestMapping(value = "/WeatherStack/{city}/{country}/{date}", method = RequestMethod.GET)
    Map<String, String> getTemperatureInCityFromWeatherStack(@PathVariable String city, @PathVariable String country, @PathVariable String date);

    /**
     * REST-ендпоинт для получения всех имеющиеся в БД в эту дату значений температуры для данного города;
     */
    @RequestMapping(value = "/{city}/{country}/{date}", method = RequestMethod.GET)
    List<TemperatureInCityDTO> getCityTemperatureByDate(@PathVariable String city, @PathVariable String country, @PathVariable String date);

    /**
     * REST-ендпоинт для получения последнего известного значения в БД температуры для города (ака "температура сейчас")
     */
    @RequestMapping(value = "/{city}/{country}", method = RequestMethod.GET)
    TemperatureInCityDTO getLastCityTemperature(@PathVariable String city, @PathVariable String country);

}