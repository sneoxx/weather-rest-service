/*REST сервис хранения и предоставления информации о температуре в городах.

Технологии: SPRING,HSQLDB,Junit,Mockito

1 Температура для городов из заданного в application.properties списка с уточнением страны
запрашивается из трех публично доступных REST сервисов погоды:
- openweathermap.org
- weatherapi.com
- weatherstack.com
  с периодичностью указанной в application.properties

2 Значения, полученные от сервисов для одного и того же города в текущий момент, усредняются
и складываются в HSQLDB базу данных в in-process режиме с текущим timestamp

3 Сервис предоставляет REST-ендпоинт, через который, указав город/страну и дату,
можно получить все имеющиеся в БД в эту дату значения температуры для данного города
http://localhost:8080/London/uk/2022-04-25

4 Сервис  предоставляет REST-ендпоинт, через который, указав город/страну
возвращается последнее известное значение для города (ака "температура сейчас")
http://localhost:8080/London/uk/

5  Сервис предоставляет REST-ендпоинт, через который, указав город/страну
возвращается текущая температура по городу,стране и дате из сервиса OpenWeatherMap
http://localhost:8080/OpenWeatherMap/Paris/France/2022-04-25%2008:16:35.816

6  Сервис  предоставляет REST-ендпоинт, через который, указав город/страну возвращается последнее известное значение для города (ака "температура сейчас")
возвращается текущая температура по городу,стране и дате из сервиса WeatherApi
http://localhost:8080/WeatherApi/London/uk/2022-04-25%2008:16:35.816

7  Сервис  предоставляет REST-ендпоинт, через который, указав город/страну возвращается последнее известное значение для города (ака "температура сейчас")
возвращается текущая температура по городу,стране и дате из сервиса WeatherStack
http://localhost:8080/WeatherStack/London/uk/2022-04-25%2008:16:35.816

На логику сервиса написаны, юнит-тесты (JUnit, Mockito). И интеграционные тесты