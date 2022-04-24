package ru.infinnity.zaraev.weatherrestservice;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/*REST сервис хранения и предоставления информации о температуре в городах.

1 Температура для городов из заданного в application.properties списка с уточнением страны
 запрашивается  из трех публично доступных REST сервисов погоды:
  - openweathermap.org
  - weatherapi.com
  - weatherstack.com
  с периодичностью указанной в application.properties

2  Значения, полученные от сервисов для одного и того же города в текущий момент, усредняются
   и складываются в HSQLDB в in-process режиме с текущим timestamp;

3  Сервис предоставляет REST-ендпоинт, через который, указав город/страну и дату,
   можно получить все имеющиеся в БД в эту дату значения температуры для данного города

4  Сервис  предоставляет REST-ендпоинт, через который, указав город/страну
   возвращается последнее известное значение для города (ака "температура сейчас")

5  Сервис предоставляет REST-ендпоинт, через который, указав город/страну
   возвращается текущая температура по городу,стране и дате из сервиса OpenWeatherMap

6  Сервис  предоставляет REST-ендпоинт, через который, указав город/страну возвращается последнее известное значение для города (ака "температура сейчас")
   возвращается текущая температура по городу,стране и дате из сервиса WeatherApi

7  Сервис  предоставляет REST-ендпоинт, через который, указав город/страну возвращается последнее известное значение для города (ака "температура сейчас")
   возвращается текущая температура по городу,стране и дате из сервиса WeatherStack

 На логику сервиса должны быть написаны, как минимум, юнит-тесты (JUnit, Mockito). В юнит-тестах учесть краевые сценарии.

 Плюсом идет наличие интеграционных тестов.*/


/**
 * Класс регистрирующий Spring Dispatcher Servlet и запускающий приложение
 */
@SpringBootApplication
@EnableScheduling
@EnableFeignClients
@EnableDiscoveryClient
public class FeignClientApplication {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(FeignClientApplication.class);

    }
}