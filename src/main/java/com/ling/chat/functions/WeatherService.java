package com.ling.chat.functions;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WeatherService {

  private final String URL = "https://api.vvhan.com/api";
  private final RestClient restClient;

  public WeatherService() {
    this.restClient = RestClient.create(URL);
  }

  public record City(String city) {}

  public record WeatherInfo(String city, Data data) {}

  public record Data(String date, Condition night, String high, String low) {}

  public record Condition(String type, String fengli) {}

  public WeatherInfo getCityWeather(City cityName) {
    System.out.println("request is :" + cityName);
    WeatherInfo response =
        restClient
            .get()
            .uri("/weather?city={city}", cityName.city())
            .retrieve()
            .body(WeatherInfo.class);
    System.out.println("response is :" + response);
    return response;
  }
}
