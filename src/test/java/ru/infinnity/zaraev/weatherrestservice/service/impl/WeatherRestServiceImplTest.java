package ru.infinnity.zaraev.weatherrestservice.service.impl;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.infinnity.zaraev.weatherrestservice.api.FeignClientForOpenWeatherMap;
import ru.infinnity.zaraev.weatherrestservice.api.FeignClientForWeatherApi;
import ru.infinnity.zaraev.weatherrestservice.api.FeignClientForWeatherStack;
import ru.infinnity.zaraev.weatherrestservice.entity.TemperatureInCity;
import ru.infinnity.zaraev.weatherrestservice.repository.TemperatureInCityRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherRestServiceImplTest {

    @Mock
    private FeignClientForOpenWeatherMap feignClientForOpenWeatherMap;

    @Mock
    private FeignClientForWeatherApi feignClientForWeatherApi;

    @Mock
    private FeignClientForWeatherStack feignClientForWeatherStack;

    @Mock
    private TemperatureInCityRepository temperatureInCityRepository;

    @Spy
    @InjectMocks
    private WeatherRestServiceImpl weatherRestService;

    String expectedResponseFromOpenWeatherMapService;
    String expectedResponseFromWeatherApiService;
    String expectedResponseFromWeatherStackService;

    @BeforeEach
    public void setUp() {
         expectedResponseFromOpenWeatherMapService = "{\"coord\":{\"lon\":-0.1257,\"lat\":51.5085},\"weather\":[{\"id\":804,\"main\"" +
                ":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"base\":\"stations\",\"main\"" +
                ":{\"temp\":17.34,\"feels_like\":16.33,\"temp_min\":16.45,\"temp_max\":18.96,\"pressure\":1009,\"" +
                "humidity\":46},\"visibility\":10000,\"wind\":{\"speed\":5.66,\"deg\":50},\"clouds\":{\"all\":90},\"" +
                "dt\":1650808862,\"sys\":{\"type\":2,\"id\":2019646,\"country\":\"GB\",\"sunrise\":1650775566,\"sunset\"" +
                ":1650827453},\"timezone\":3600,\"id\":2643743,\"name\":\"London\",\"cod\":200}";

        expectedResponseFromWeatherApiService = "{\"location\":{\"name\":\"London\",\"region\":\"City of London," +
                " Greater London\",\"country\":\"United Kingdom\",\"lat\":51.52,\"lon\":-0.11,\"tz_id\":\"Europe/London\"" +
                ",\"localtime_epoch\":1650816110,\"localtime\":\"2022-04-24 17:01\"},\"current\":{\"last_updated_epoch\"" +
                ":1650815100,\"last_updated\":\"2022-04-24 16:45\",\"temp_c\":16.0,\"temp_f\":60.8,\"is_day\":1," +
                "\"condition\":{\"text\":\"Sunny\",\"icon:\":\"cdn.weatherapi.com/weather/64x64/day/113.png\"," +
                "\"code\":1000},\"wind_mph\":9.4,\"wind_kph\":15.1,\"wind_degree\":30,\"wind_dir\":\"NNE\"," +
                "\"pressure_mb\":1009.0,\"pressure_in\":29.8,\"precip_mm\":0.0,\"precip_in\":0.0,\"humidity\":48," +
                "\"cloud\":0,\"feelslike_c\":16.0,\"feelslike_f\":60.8,\"vis_km\":10.0,\"vis_miles\":6.0,\"uv\":5.0," +
                "\"gust_mph\":14.1,\"gust_kph\":22.7}}";

        expectedResponseFromWeatherStackService = "{\"success\":false,\"error\":{\"code\":104,\"type\":\"usage_limit_reached\"," +
                "\"info\":\"Your monthly usage limit has been reached. Please upgrade your Subscription Plan.\"}}";
    }


    @Test
    void getWeatherByCityFromOpenWeatherMap() {
        when(weatherRestService.getWeatherByCityFromOpenWeatherMap("London", "UK", "2022-04-24"))
                .thenReturn(expectedResponseFromOpenWeatherMapService);

        String actualResponseFromService = weatherRestService.getWeatherByCityFromOpenWeatherMap("London", "UK", "2022-04-24");

        verify(feignClientForOpenWeatherMap, times(1)).getWeatherByCity("London,UK");
        assertTrue(actualResponseFromService.contains("London"));
    }

    @Test
    void getWeatherByCityFromWeatherApi() {


        when(weatherRestService.getWeatherByCityFromWeatherApi("London", "UK", "2022-04-24"))
                .thenReturn(expectedResponseFromWeatherApiService);

        String actualResponseFromService = weatherRestService.getWeatherByCityFromWeatherApi("London", "UK", "2022-04-24");

        verify(feignClientForWeatherApi, times(1)).getWeatherByCity("London");
        assertTrue(actualResponseFromService.contains("United Kingdom"));
    }

    @Test
    void getWeatherByCityFromWeatherStack() {
        when(weatherRestService.getWeatherByCityFromWeatherStack("London", "UK", "2022-04-24"))
                .thenReturn(expectedResponseFromWeatherStackService);

        String actualResponseFromService = weatherRestService.getWeatherByCityFromWeatherStack("London", "UK", "2022-04-24");

        verify(feignClientForWeatherStack, times(1)).getWeatherByCity("London");
        assertTrue(actualResponseFromService.contains("Please upgrade your Subscription Plan"));
    }

    @Test
    void getMyResponseForOpenWeatherMap() {
        JSONObject jsonObj = new JSONObject(expectedResponseFromOpenWeatherMapService);

        Map<String, String> actualResult = weatherRestService.getMyResponseForOpenWeatherMap(expectedResponseFromOpenWeatherMapService);

        verify(weatherRestService, times(1)).getMapWithTemp(jsonObj.getJSONObject("main").get("temp"));
        assertEquals("17.34", actualResult.get("temp"));

    }

    @Test
    void getAveragingTemperature() {
        when(weatherRestService.getWeatherByCityFromOpenWeatherMap("London", "UK", "2022-04-24"))
                .thenReturn(expectedResponseFromOpenWeatherMapService);
        when(weatherRestService.getWeatherByCityFromWeatherApi("London", "UK", "2022-04-24"))
                .thenReturn(expectedResponseFromWeatherApiService);
        when(weatherRestService.getWeatherByCityFromWeatherStack("London", "UK", "2022-04-24"))
                .thenReturn(expectedResponseFromWeatherStackService);

        Float actualResult = weatherRestService.getAveragingTemperature("London", "UK", "2022-04-24");

        verify(weatherRestService, times(1)).getMyResponseForOpenWeatherMap(any());
        verify(weatherRestService, times(1)).getMyResponseForWeatherStack(any());
        verify(weatherRestService, times(1)).getMyResponseForWeatherApi(any());
        assertEquals(16.67f, actualResult);
    }

    @Test
    void writeRecordInDatabase() {
        TemperatureInCity temperatureInCity = new TemperatureInCity();
        temperatureInCity.setCity("London");
        temperatureInCity.setCountry("UK");
        temperatureInCity.setTemperature(16.67f);
        temperatureInCity.setTimeStampTemperature(Timestamp.valueOf("2022-04-25 08:16:35.816"));

        when(temperatureInCityRepository.save(temperatureInCity))
                .thenReturn(temperatureInCity);

        weatherRestService.writeRecordInDatabase("London", "UK", "2022-04-25 08:16:35.816", 16.67f);

        verify(temperatureInCityRepository, times(1)).save(temperatureInCity);
    }

    @Test
    void getAllAverageTemperatureValuesInACityByDate() {
        List<TemperatureInCity> temperatureInCityList = new ArrayList<>();

        when(temperatureInCityRepository.findAllByTimeStampTemperature("London", "2022-04-25"))
                .thenReturn(temperatureInCityList);

        weatherRestService.getAllAverageTemperatureValuesInACityByDate("London", "UK", "2022-04-25 08:16:35.816");

        verify(temperatureInCityRepository, times(1)).findAllByTimeStampTemperature("London", "2022-04-25");
    }

    @Test
    void getLastTemperatureByCity() {
        weatherRestService.getLastTemperatureByCity("London", "UK");

        verify(temperatureInCityRepository, times(1)).findLastTemperatureByCity("London");
    }
}