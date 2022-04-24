package ru.infinnity.zaraev.weatherrestservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Сущность для маппинга в БД
 */
@Data
@NoArgsConstructor
@Entity
public class TemperatureInCity {

    @Id
    @GeneratedValue
    private UUID id;

    private String city;

    private String country;

    private Float temperature;

    private Timestamp timeStampTemperature;

}
