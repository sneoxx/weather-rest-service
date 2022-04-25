package ru.infinnity.zaraev.weatherrestservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.infinnity.zaraev.weatherrestservice.entity.TemperatureInCity;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
class TemperatureInCityRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TemperatureInCityRepository repository;

    TemperatureInCity temperatureInCity;
    TemperatureInCity temperatureInCity2;
    TemperatureInCity temperatureInCity3;

    @BeforeEach
    public void setUp() {
        temperatureInCity = new TemperatureInCity();
        temperatureInCity.setCity("London");
        temperatureInCity.setCountry("UK");
        temperatureInCity.setTemperature(16.67f);
        temperatureInCity.setTimeStampTemperature(Timestamp.valueOf("2022-04-25 08:16:35.816"));

        temperatureInCity2 = new TemperatureInCity();
        temperatureInCity2.setCity("London");
        temperatureInCity2.setCountry("UK2");
        temperatureInCity2.setTemperature(17.67f);
        temperatureInCity2.setTimeStampTemperature(Timestamp.valueOf("2022-04-25 09:18:35.84"));

        temperatureInCity3 = new TemperatureInCity();
        temperatureInCity3.setCity("London");
        temperatureInCity3.setCountry("UK2");
        temperatureInCity3.setTemperature(18.00f);
        temperatureInCity3.setTimeStampTemperature(Timestamp.valueOf("2022-04-23 06:98:35.74"));
    }

    @Test
    void testFindAllByTimeStampTemperature() {
        this.entityManager.persist(temperatureInCity);
        this.entityManager.persist(temperatureInCity2);

        List<TemperatureInCity> temperatureInCityList = this.repository.findAllByTimeStampTemperature("London", "2022-04-25");

        assertEquals(temperatureInCityList.get(0).getTemperature(), (16.67f));
        assertTrue(temperatureInCityList.size() == 2);
    }

    @Test
    void testFindLastTemperatureByCity() {
        this.entityManager.persist(temperatureInCity);
        this.entityManager.persist(temperatureInCity2);
        this.entityManager.persist(temperatureInCity3);

        TemperatureInCity temperatureInCityResult = this.repository.findLastTemperatureByCity("London");

        assertEquals(temperatureInCityResult.getTimeStampTemperature(), Timestamp.valueOf("2022-04-25 09:18:35.84"));
        assertTrue(temperatureInCityResult.getTimeStampTemperature().compareTo(temperatureInCity.getTimeStampTemperature()) == 1);
    }
}