package ru.infinnity.zaraev.weatherrestservice.api.dto;

import lombok.Data;

// DTO класс для возврата ответов в Api слой
@Data
public class TemperatureInCityDTO {

    private String id;

    private String city;

    private String temperature;

    private String timeStampTemperature;

}