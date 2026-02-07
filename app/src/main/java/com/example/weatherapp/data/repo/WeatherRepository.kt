package com.example.weatherapp.data.repo

import com.example.weatherapp.data.api.RetrofitClient
import com.example.weatherapp.data.local.WeatherCache
import com.example.weatherapp.data.model.CityDto
import com.example.weatherapp.data.model.WeatherResponse

class WeatherRepository(private val cache: WeatherCache) {
    private val api = RetrofitClient.api

    suspend fun searchCity(query: String): List<CityDto> {
        return try {
            api.searchCity(query).results ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getWeatherData(lat: Double, long: Double): Pair<WeatherResponse, Boolean> {
        return try {
            // 1. Trying the Internet
            val response = api.getWeather(lat, long)
            // 2. If successful, we save it to the cache.
            cache.saveWeather(response)
            Pair(response, false)
        } catch (e: Exception) {
            // 3. No internet read the cache
            val cached = cache.getLastWeather()
            if (cached != null) {
                Pair(cached, true)
            } else {
                throw e
            }
        }
    }
}