package ru.infinnity.zaraev.weatherrestservice.service.impl;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.infinnity.zaraev.weatherrestservice.api.FeignClientForOpenWeatherMap;
import ru.infinnity.zaraev.weatherrestservice.api.FeignClientForWeatherApi;
import ru.infinnity.zaraev.weatherrestservice.api.FeignClientForWeatherStack;
import ru.infinnity.zaraev.weatherrestservice.repository.TemperatureInCityRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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


    @Test
    void getWeatherByCityFromOpenWeatherMap() {
        String expectedResponseFromService = "{\"coord\":{\"lon\":-0.1257,\"lat\":51.5085},\"weather\":[{\"id\":804,\"main\"" +
                ":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"base\":\"stations\",\"main\"" +
                ":{\"temp\":17.34,\"feels_like\":16.33,\"temp_min\":16.45,\"temp_max\":18.96,\"pressure\":1009,\"" +
                "humidity\":46},\"visibility\":10000,\"wind\":{\"speed\":5.66,\"deg\":50},\"clouds\":{\"all\":90},\"" +
                "dt\":1650808862,\"sys\":{\"type\":2,\"id\":2019646,\"country\":\"GB\",\"sunrise\":1650775566,\"sunset\"" +
                ":1650827453},\"timezone\":3600,\"id\":2643743,\"name\":\"London\",\"cod\":200}";

        when(weatherRestService.getWeatherByCityFromOpenWeatherMap("London", "UK", "2022-04-24"))
                .thenReturn(expectedResponseFromService);

        String actualResponseFromService = weatherRestService.getWeatherByCityFromOpenWeatherMap("London", "UK", "2022-04-24");

        verify(feignClientForOpenWeatherMap, times(1)).getWeatherByCity("London,UK");

        assertTrue(actualResponseFromService.contains("London"));
    }

    @Test
    void getWeatherByCityFromWeatherApi() {
        String expectedResponseFromService = "{\"location\":{\"name\":\"London\",\"region\":\"City of London," +
                " Greater London\",\"country\":\"United Kingdom\",\"lat\":51.52,\"lon\":-0.11,\"tz_id\":\"Europe/London\"" +
                ",\"localtime_epoch\":1650816110,\"localtime\":\"2022-04-24 17:01\"},\"current\":{\"last_updated_epoch\"" +
                ":1650815100,\"last_updated\":\"2022-04-24 16:45\",\"temp_c\":16.0,\"temp_f\":60.8,\"is_day\":1," +
                "\"condition\":{\"text\":\"Sunny\",\"icon:\"//cdn.weatherapi.com/weather/64x64/day/113.png\"," +
                "\"code\":1000},\"wind_mph\":9.4,\"wind_kph\":15.1,\"wind_degree\":30,\"wind_dir\":\"NNE\"," +
                "\"pressure_mb\":1009.0,\"pressure_in\":29.8,\"precip_mm\":0.0,\"precip_in\":0.0,\"humidity\":48," +
                "\"cloud\":0,\"feelslike_c\":16.0,\"feelslike_f\":60.8,\"vis_km\":10.0,\"vis_miles\":6.0,\"uv\":5.0," +
                "\"gust_mph\":14.1,\"gust_kph\":22.7}}";

        when(weatherRestService.getWeatherByCityFromWeatherApi("London", "UK", "2022-04-24"))
                .thenReturn(expectedResponseFromService);

        String actualResponseFromService = weatherRestService.getWeatherByCityFromWeatherApi("London", "UK", "2022-04-24");

        verify(feignClientForWeatherApi, times(1)).getWeatherByCity("London");

        assertTrue(actualResponseFromService.contains("United Kingdom"));
    }

    @Test
    void getWeatherByCityFromWeatherStack() {
        String expectedResponseFromService = "{\"success\":false,\"error\":{\"code\":104,\"type\":\"usage_limit_reached\"," +
                "\"info\":\"Your monthly usage limit has been reached. Please upgrade your Subscription Plan.\"}}";

        when(weatherRestService.getWeatherByCityFromWeatherStack("London", "UK", "2022-04-24"))
                .thenReturn(expectedResponseFromService);

        String actualResponseFromService = weatherRestService.getWeatherByCityFromWeatherStack("London", "UK", "2022-04-24");

        verify(feignClientForWeatherStack, times(1)).getWeatherByCity("London");

        assertTrue(actualResponseFromService.contains("Please upgrade your Subscription Plan"));
    }

    @Test
    void getMyResponseForOpenWeatherMap() {
        String stringResponse = "{\"coord\":{\"lon\":-0.1257,\"lat\":51.5085},\"weather\":[{\"id\":804,\"main\"" +
                ":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"base\":\"stations\",\"main\"" +
                ":{\"temp\":17.34,\"feels_like\":16.33,\"temp_min\":16.45,\"temp_max\":18.96,\"pressure\":1009,\"" +
                "humidity\":46},\"visibility\":10000,\"wind\":{\"speed\":5.66,\"deg\":50},\"clouds\":{\"all\":90},\"" +
                "dt\":1650808862,\"sys\":{\"type\":2,\"id\":2019646,\"country\":\"GB\",\"sunrise\":1650775566,\"sunset\"" +
                ":1650827453},\"timezone\":3600,\"id\":2643743,\"name\":\"London\",\"cod\":200}";

        JSONObject jsonObj = new JSONObject(stringResponse);

        Map<String, String> myResponse = new HashMap<>();
        String str = jsonObj.getJSONObject("main").get("temp").toString();
        myResponse.put("temp", str);

        Mockito.lenient().when(weatherRestService.getMyResponseForWeatherStack(stringResponse))
                .thenReturn(myResponse);

        Map<String, String> actualResult = weatherRestService.getMyResponseForOpenWeatherMap(stringResponse);

        verify(weatherRestService, times(1)).getMapWithTemp(jsonObj.getJSONObject("main").get("temp"));

        assertEquals("17.34", actualResult.get("temp"));

    }

}