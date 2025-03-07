package com.ling.chat.config;

import com.ling.chat.functions.BookService;
import com.ling.chat.functions.WeatherService;
import java.util.function.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Configuration(proxyBeanMethods = false)
public class FunctionsConfiguration {

  @Bean
  @Description("Get the list of books written by the given author available in the library")
  public Function<BookService.Author, BookService.BookList> booksByAuthor(BookService bookService) {
    return bookService::getBooksByAuthor;
  }

  @Bean
  @Description("根据输入的城市获取对应的天气")
  public Function<WeatherService.City, WeatherService.WeatherInfo> weatherByCity(
      WeatherService weatherService) {
    return weatherService::getCityWeather;
  }
}
