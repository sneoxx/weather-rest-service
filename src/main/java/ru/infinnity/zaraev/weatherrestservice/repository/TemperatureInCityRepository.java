package ru.infinnity.zaraev.weatherrestservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.infinnity.zaraev.weatherrestservice.entity.TemperatureInCity;

import java.util.List;
import java.util.UUID;

/**
 * Слой Repository работающий с БД
 */
@Repository
public interface TemperatureInCityRepository extends JpaRepository<TemperatureInCity, UUID> {

    @Query(value = "SELECT w FROM TemperatureInCity w WHERE w.city LIKE CONCAT ('%',:city,'%') AND w.timeStampTemperature LIKE CONCAT ('%',:date,'%')")
    List<TemperatureInCity> findAllByTimeStampTemperature(String city, String date);

    @Query(value = "SELECT * FROM temperature_in_city WHERE city = ?1 order by time_stamp_temperature desc limit 1",
            nativeQuery = true)
    TemperatureInCity findLastTemperatureByCity(String city);

}
