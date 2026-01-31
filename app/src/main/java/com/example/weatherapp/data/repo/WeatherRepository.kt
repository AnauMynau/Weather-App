package com.example.weatherapp.data.repository

import com.example.weatherapp.data.api.RetrofitClient
import com.example.weatherapp.data.local.WeatherCache
import com.example.weatherapp.data.model.CityDto
import com.example.weatherapp.data.model.WeatherResponse

// Теперь репозиторий требует Cache
class WeatherRepository(private val cache: WeatherCache) {
    private val api = RetrofitClient.api

    suspend fun searchCity(query: String): List<CityDto> {
        return try {
            api.searchCity(query).results ?: emptyList()
        } catch (e: Exception) {
            emptyList() // Если ошибка поиска, просто вернем пустой список
        }
    }

    // Возвращаем Пару: (Данные, ЯвляетсяЛиЭтоКэшем)
    suspend fun getWeatherData(lat: Double, long: Double): Pair<WeatherResponse, Boolean> {
        return try {
            // 1. Пробуем Интернет
            val response = api.getWeather(lat, long)
            // 2. Если успешно — сохраняем в кэш
            cache.saveWeather(response)
            // Возвращаем данные + false (это НЕ офлайн данные, это свежак)
            Pair(response, false)
        } catch (e: Exception) {
            // 3. Если ошибка (нет инета) — читаем Кэш
            val cached = cache.getLastWeather()
            if (cached != null) {
                // Возвращаем кэш + true (это ОФЛАЙН данные)
                Pair(cached, true)
            } else {
                throw e // Если и в кэше пусто, тогда уже ошибка
            }
        }
    }
}