package com.example.weatherapp.data.api

import com.example.weatherapp.data.model.GeocodingResponse
import com.example.weatherapp.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    // 1. Поиск города
    // Используем полный URL, так как у Open-Meteo поиск на другом поддомене
    @GET("https://geocoding-api.open-meteo.com/v1/search")
    suspend fun searchCity(
        @Query("name") query: String,
        @Query("count") count: Int = 10,
        @Query("language") language: String = "en",
        @Query("format") format: String = "json"
    ): GeocodingResponse

    // 2. Получение погоды
    // Base URL будет https://api.open-meteo.com/v1/
    @GET("forecast")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double,
        @Query("current_weather") current: Boolean = true,
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min", // Запрашиваем макс/мин температуру
        @Query("timezone") timezone: String = "auto"
    ): WeatherResponse
}