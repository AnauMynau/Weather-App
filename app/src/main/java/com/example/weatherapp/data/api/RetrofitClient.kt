package com.example.weatherapp.data.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitClient {
    private const val BASE_URL = "https://api.open-meteo.com/v1/"

    // Настройка JSON парсера (чтобы он игнорировал неизвестные поля и не падал)
    private val json = Json {
        ignoreUnknownKeys = true
    }

    // Создаем сам Retrofit
    val api: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(WeatherApi::class.java)
    }
}