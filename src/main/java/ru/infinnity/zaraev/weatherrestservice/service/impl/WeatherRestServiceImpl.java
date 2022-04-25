package ru.infinnity.zaraev.weatherrestservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.infinnity.zaraev.weatherrestservice.api.FeignClientForOpenWeatherMap;
import ru.infinnity.zaraev.weatherrestservice.api.FeignClientForWeatherApi;
import ru.infinnity.zaraev.weatherrestservice.api.FeignClientForWeatherStack;
import ru.infinnity.zaraev.weatherrestservice.entity.TemperatureInCity;
import ru.infinnity.zaraev.weatherrestservice.repository.TemperatureInCityRepository;
import ru.infinnity.zaraev.weatherrestservice.service.WeatherRestService;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Сервисный класс для обработки запросов от контроллера
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WeatherRestServiceImpl implements WeatherRestService {

    @Value("#{${weather.city}}")
    private Map<String, String> cityWithCountry;

    private final FeignClientForOpenWeatherMap feignClientForOpenWeatherMap;

    private final FeignClientForWeatherApi feignClientForWeatherApi;

    private final FeignClientForWeatherStack feignClientForWeatherStack;

    private final TemperatureInCityRepository temperatureInCityRepository;

    /**
     * Запрос погоды на дату по городу и стране из сервиса OpenWeatherMap
     *
     * @param city    - город запроса
     * @param country - страна запроса
     * @param date    - дата запроса
     * @return - ответ сервиса
     */
    @Override
    public String getWeatherByCityFromOpenWeatherMap(String city, String country, String date) {
        String responseFromService = feignClientForOpenWeatherMap.getWeatherByCity(city + "," + country);
        log.debug("getWeatherByCityFromOpenWeatherMap() Ответ из сервиса получен {}", responseFromService);
        return responseFromService;
    }

    /**
     * Запрос погоды на дату по городу и стране из сервиса WeatherApi
     *
     * @param city    - город запроса
     * @param country - страна запроса
     * @param date    - дата запроса
     * @return - ответ сервиса
     */
    @Override
    public String getWeatherByCityFromWeatherApi(String city, String country, String date) {
        String responseFromService = feignClientForWeatherApi.getWeatherByCity(city);
        log.debug("getWeatherByCityFromWeatherApi() Ответ из сервиса получен {}", responseFromService);
        return responseFromService;
    }

    /**
     * Запрос погоды на дату по городу и стране из сервиса WeatherStack
     *
     * @param city    - город запроса
     * @param country - страна запроса
     * @param date    - дата запроса
     * @return - ответ сервиса
     */
    @Override
    public String getWeatherByCityFromWeatherStack(String city, String country, String date) {
        String responseFromService = feignClientForWeatherStack.getWeatherByCity(city);
        log.debug("getWeatherByCityFromWeatherStack() Ответ из сервиса получен {}", responseFromService);
        return responseFromService;
    }

    /**
     * Получить температуру из ответа сервиса OpenWeatherMap
     *
     * @param stringResponse ответ сервиса OpenWeatherMap
     * @return - Map, где ключ "temp", а значение полученная из сервиса температура
     */
    @Override
    public Map<String, String> getMyResponseForOpenWeatherMap(String stringResponse) {
        JSONObject JsonObj = new JSONObject(stringResponse);
        Map<String, String> myResponse;
        if (JsonObj.getJSONObject("main") != null) {
            Object object = JsonObj.getJSONObject("main").get("temp");
            myResponse = getMapWithTemp(object);
            log.info("getMyResponseForOpenWeatherMap() {} ", myResponse);
            return myResponse;
        } else
            myResponse = new HashMap<>();
        myResponse.put("Ошибка сервиса получения данных из сервиса", "WeatherMap");
        log.info("getMyResponseForOpenWeatherMap() Мар не получена, сервис вернул {} ", myResponse);
        return myResponse;
    }

    /**
     * Получить температуру из ответа сервиса WeatherApi
     *
     * @param stringResponse ответ сервиса WeatherApi
     * @return - Map, где ключ "temp", а значение полученная из сервиса температура
     */
    @Override
    public Map<String, String> getMyResponseForWeatherApi(String stringResponse) {
        JSONObject JsonObj = new JSONObject(stringResponse);
        Map<String, String> myResponse;
        if (JsonObj.getJSONObject("current") != null) {
            Object object = JsonObj.getJSONObject("current").get("temp_c");
            myResponse = getMapWithTemp(object);
            log.info("getMyResponseForWeatherApi() {} ", myResponse);
            return myResponse;
        } else
            myResponse = new HashMap<>();
        myResponse.put("Ошибка сервиса получения данных из сервиса", "WeatherApi");
        log.info("getMyResponseForWeatherApi() Мар не получена, сервис вернул {} ", myResponse);
        return myResponse;
    }

    /**
     * Получить температуру из ответа сервиса WeatherStack
     *
     * @param stringResponse ответ сервиса WeatherStack
     * @return - Map, где ключ "temp", а значение полученная из сервиса температура
     */
    @Override
    public Map<String, String> getMyResponseForWeatherStack(String stringResponse) {
        JSONObject JsonObj = new JSONObject(stringResponse);
        Map<String, String> myResponse;
        if (!JsonObj.isNull("current")) {
            Object object = JsonObj.getJSONObject("current").get("temperature");
            myResponse = getMapWithTemp(object);
            log.info("getMyResponseForOpenWeatherMap() {} ", myResponse);
            return myResponse;
        } else
            myResponse = new HashMap<>();
        myResponse.put("Ошибка сервиса получения данных из сервиса", "WeatherStack");
        log.info("getMyResponseForOpenWeatherMap() Мар не получена, сервис вернул {} ", myResponse);
        return myResponse;
    }

    /**
     * Получить Map из Object
     *
     * @param JsonObj - исходный Object
     * @return - Map, где ключ "temp", а значение полученная из сервиса температура
     */
    public Map<String, String> getMapWithTemp(Object JsonObj) {
        Map<String, String> myResponse = new HashMap<>();
        myResponse.put("temp", JsonObj.toString());
        return myResponse;
    }

    /**
     * Получение средней температуры из трех сервисов по городам на англ. языке
     * указанным в файле (с уточнением страны, где он находится)
     * на текущий момент
     * усреднение и запись в in-memory database с текущим timestamp;
     */
    @Scheduled(fixedRateString = "${weather.periodicity.in.milliseconds}")
    public void averagingTheWeatherAndRecordingInDatabase() {
        for (Map.Entry<String, String> entry : cityWithCountry.entrySet()) {
            String city = entry.getKey();
            String country = entry.getValue();
            String date = new Timestamp(System.currentTimeMillis()).toString();
            Float averageTemperature = getAveragingTemperature(city, country, date);
            writeRecordInDatabase(city, country, date, averageTemperature);
        }
    }

    /**
     * Получение средней температуры из трех сервисов
     * по городам на англ. языке с уточнением страны, где он находится
     * на текущий момент
     *
     * @param city    - искомый город
     * @param country - страна города
     * @param date    - текущий timestamp
     * @return - средняя температура в городе на текущий момент
     */
    public Float getAveragingTemperature(String city, String country, String date) {
        int numberOfWorkingServices = 0;
        float temperatureFromOpenWeatherMap = 0.0f;
        float temperatureFromWeatherApi = 0.0f;
        float temperatureWeatherStack = 0.0f;

        Map<String, String> myResponseForOpenWeatherMap = getMyResponseForOpenWeatherMap(
                getWeatherByCityFromOpenWeatherMap(city, country, date));
        if (myResponseForOpenWeatherMap.get("temp") != null) {
            String responseFromService1 = myResponseForOpenWeatherMap.get("temp");
            temperatureFromOpenWeatherMap = Float.parseFloat(responseFromService1);
            numberOfWorkingServices++;
        }

        Map<String, String> myResponseForWeatherApi = getMyResponseForWeatherApi(
                getWeatherByCityFromWeatherApi(city, country, date));
        if (myResponseForWeatherApi.get("temp") != null) {
            String responseFromService2 = myResponseForWeatherApi.get("temp");
            temperatureFromWeatherApi = Float.parseFloat(responseFromService2);
            numberOfWorkingServices++;
        }

        Map<String, String> myResponseForWeatherStack = getMyResponseForWeatherStack(
                getWeatherByCityFromWeatherStack(city, country, date));
        if (myResponseForWeatherStack.get("temp") != null) {
            String responseFromService3 = myResponseForWeatherStack.get("temp");
            temperatureWeatherStack = Float.parseFloat(responseFromService3);
            numberOfWorkingServices++;
        }

        Float averageTemperature = (temperatureFromOpenWeatherMap + temperatureFromWeatherApi + temperatureWeatherStack) / numberOfWorkingServices;
        log.info("getAveragingTemperature() В городе {} средняя температура {}", city, averageTemperature);
        return averageTemperature;
    }

    /**
     * Запись сущности в базу
     *
     * @param city               - искомый город
     * @param country            - страна города
     * @param date               - timestamp
     * @param averageTemperature - среднаяя температура
     */
    public void writeRecordInDatabase(String city, String country, String date, Float averageTemperature) {
        TemperatureInCity temperatureInCity = new TemperatureInCity();
        temperatureInCity.setCity(city);
        temperatureInCity.setCountry(country);
        temperatureInCity.setTemperature(averageTemperature);
        temperatureInCity.setTimeStampTemperature(Timestamp.valueOf(date));
        TemperatureInCity temperatureInCityCheck = temperatureInCityRepository.save(temperatureInCity);
        log.info("averagingTheWeatherAndRecordingInDatabase() Объект успешно записан в БД: {} ", temperatureInCityCheck);
    }


    /**
     * Получить все значения средней температуры в городе на дату
     *
     * @param city    - искомый город
     * @param country - страна города
     * @param date    - timestamp или дата
     * @return - все значения средней температуры в городе на дату
     */
    @Override
    public List<TemperatureInCity> getAllAverageTemperatureValuesInACityByDate(String city, String country, String date) {
        String dateWithoutTime = date.replaceAll(" .*", "");
        List<TemperatureInCity> temperatureInCityList = temperatureInCityRepository.findAllByTimeStampTemperature(city, dateWithoutTime);
        log.info("getAverageTemperatureInCityByDate() В городе {} на дату {} средняя температура {}", city, dateWithoutTime, temperatureInCityList);
        return temperatureInCityList;
    }

    /**
     * Последнее известное значение средней температуры для города (ака "температура сейчас")
     *
     * @param city    - искомый город
     * @param country - страна города
     * @return - температура сейчас
     */
    @Override
    public TemperatureInCity getLastTemperatureByCity(String city, String country) {
        TemperatureInCity temperatureInCityList = temperatureInCityRepository.findLastTemperatureByCity(city);
        log.info("getLastTemperatureByCity() В городе {} сейчас температура {}", city, temperatureInCityList);
        return temperatureInCityList;
    }
}
