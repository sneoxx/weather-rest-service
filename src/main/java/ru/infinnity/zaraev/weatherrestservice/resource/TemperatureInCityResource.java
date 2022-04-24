package ru.infinnity.zaraev.weatherrestservice.resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.infinnity.zaraev.weatherrestservice.api.dto.TemperatureInCityDTO;
import ru.infinnity.zaraev.weatherrestservice.service.WeatherRestService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * RestController обрабатывающий запросы из веба
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class TemperatureInCityResource implements ru.infinnity.zaraev.weatherrestservice.api.TemperatureInCityResource {

    private final WeatherRestService weatherRestService;

    private final ModelMapper modelMapper;

    /**
     * REST-ендпоинт для получения температуры по городу,стране и дате из сервиса OpenWeatherMap
     *
     * @param city    - искомый город
     * @param country - страна города
     * @param date    - дата или timestamp
     * @return - температура по городу,стране и дате из сервиса OpenWeatherMap
     */
    @Override
    public Map<String, String> getTemperatureInCityFromOpenWeatherMap(String city, String country, String date) {
        String responseFromService = weatherRestService.getWeatherByCityFromOpenWeatherMap(city, country, date);
        Map<String, String> myResponse = weatherRestService.getMyResponseForOpenWeatherMap(responseFromService);
        log.info("getTemperatureInCityFromOpenWeatherMap() Запрос: {}, {}, {} успешно вернул ответ {}", city, country, date, myResponse);
        return myResponse;
    }

    /**
     * REST-ендпоинт для получения температуры по городу,стране и дате из сервиса WeatherApi
     *
     * @param city    - искомый город
     * @param country - страна города
     * @param date    - дата или timestamp
     * @return - температура по городу,стране и дате из сервиса WeatherApi
     */
    @Override
    public Map<String, String> getTemperatureInCityFromWeatherApi(String city, String country, String date) {
        String responseFromService = weatherRestService.getWeatherByCityFromWeatherApi(city, country, date);
        Map<String, String> myResponse = weatherRestService.getMyResponseForWeatherApi(responseFromService);
        log.info("getTemperatureInCityFromWeatherApi() Запрос: {}, {}, {} успешно вернул ответ {}", city, country, date, myResponse);
        return myResponse;
    }

    /**
     * REST-ендпоинт для получения температуры по городу,стране и дате из сервиса WeatherStack
     *
     * @param city    - искомый город
     * @param country - страна города
     * @param date    - дата или timestamp
     * @return - температура по городу,стране и дате из сервиса WeatherStack
     */
    @Override
    public Map<String, String> getTemperatureInCityFromWeatherStack(String city, String country, String date) {
        String responseFromService = weatherRestService.getWeatherByCityFromWeatherStack(city, country, date);
        Map<String, String> myResponse = weatherRestService.getMyResponseForWeatherStack(responseFromService);
        log.info("getTemperatureInCityFromWeatherStack() Запрос: {}, {}, {} успешно вернул ответ {}", city, country, date, myResponse);
        return myResponse;
    }

    /**
     * REST-ендпоинт для получения всех имеющиеся в БД в эту дату значений температуры для данного города;
     *
     * @param city    - искомый город
     * @param country - страна города
     * @param date    - дата или timestamp
     * @return - все имеющиеся в БД в эту дату значения температуры для данного города
     */
    @Override
    public List<TemperatureInCityDTO> getCityTemperatureByDate(@PathVariable String city, @PathVariable String country, @PathVariable String date) {
        List<TemperatureInCityDTO> responseFromService = weatherRestService.getAllAverageTemperatureValuesInACityByDate(city, country, date)
                .stream()
                .map(temperatureInCity -> modelMapper.map(temperatureInCity, TemperatureInCityDTO.class))
                .collect(Collectors.toList());
        return responseFromService;
    }

    /**
     * REST-ендпоинт для получения последнего известного значения в БД температуры для города (ака "температура сейчас")
     *
     * @param city    - искомый город
     * @param country - страна города
     * @return - последнее известное значение в БД температуры для города
     */
    @Override
    public TemperatureInCityDTO getLastCityTemperature(@PathVariable String city, @PathVariable String country) {
        TemperatureInCityDTO responseFromService = modelMapper.map(weatherRestService.getLastTemperatureByCity(city, country), TemperatureInCityDTO.class);
        return responseFromService;
    }

}
