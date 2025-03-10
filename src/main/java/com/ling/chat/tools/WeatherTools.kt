package com.ling.chat.tools

import org.springframework.ai.tool.annotation.Tool
import org.springframework.web.reactive.function.client.WebClient

class WeatherTools {

    private val URL = "https://api.vvhan.com/api"
    private val restClient: WebClient = WebClient.create(URL)

    // 定义数据类
    data class City(val city: String)
    data class WeatherInfo(val city: String, val data: Data)
    data class Data(val date: String, val night: Condition, val high: String, val low: String)
    data class Condition(val type: String, val fengli: String)

    // 获取城市天气的方法
    @Tool(description = "根据输入的城市获取对应的天气")
    fun getCityWeather(cityName: City): WeatherInfo {
        println("request is: $cityName")
        val response = restClient
            .get()
            .uri("/weather?city={city}", cityName.city)
            .retrieve()
            .bodyToMono(WeatherInfo::class.java)
            .block() // 阻塞直到获取到响应
        println("response is: $response")
        return response ?: throw RuntimeException("Failed to retrieve weather data")
    }
}

