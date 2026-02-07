package com.example.weatherapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherapp.data.model.WeatherResponse
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// Это расширение создает единое хранилище "weather_cache" для всего приложения
private val Context.dataStore by preferencesDataStore("weather_cache")

class WeatherCache(private val context: Context) {
    private val KEY_WEATHER = stringPreferencesKey("last_weather_json")

    // 1. Сохранить погоду (Превращаем объект в JSON-строку)
    suspend fun saveWeather(weather: WeatherResponse) {
        val jsonString = Json.encodeToString(weather)
        context.dataStore.edit { prefs ->
            prefs[KEY_WEATHER] = jsonString
        }
    }

    // 2. Достать погоду (Превращаем JSON-строку обратно в объект)
    suspend fun getLastWeather(): WeatherResponse? {
        val prefs = context.dataStore.data.first()
        val jsonString = prefs[KEY_WEATHER]
        return if (jsonString != null) {
            try {
                Json.decodeFromString(jsonString)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }
}