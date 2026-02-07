package com.example.weatherapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherapp.data.model.WeatherResponse
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore by preferencesDataStore("weather_cache")

class WeatherCache(private val context: Context) {
    private val KEY_WEATHER = stringPreferencesKey("last_weather_json")

    // 1. Save the weather
    private val KEY_CITY_NAME = stringPreferencesKey("last_city_name")
    suspend fun saveWeather(weather: WeatherResponse) {
        val jsonString = Json.encodeToString(weather)
        context.dataStore.edit { prefs ->
            prefs[KEY_WEATHER] = jsonString
        }
    }

    // 2. Get the weather
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

    suspend fun saveCityName(name: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_CITY_NAME] = name
        }
    }

    suspend fun getLastCityName(): String? {
        val prefs = context.dataStore.data.first()
        return prefs[KEY_CITY_NAME]
    }
}