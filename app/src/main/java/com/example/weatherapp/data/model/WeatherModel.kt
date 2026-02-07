package com.example.weatherapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// --- 1. A model for SEARCHING FOR A CITY (Geocoding API) ---
@Serializable
data class GeocodingResponse(
    @SerialName("results") val results: List<CityDto>? = null
)

@Serializable
data class CityDto(
    @SerialName("name") val name: String,
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double,
    @SerialName("country") val country: String? = null,
    @SerialName("timezone") val timezone: String? = null
)

// --- 2. A model for the WEATHER (Weather API) ---
@Serializable
data class WeatherResponse(
    @SerialName("current_weather") val currentWeather: CurrentWeatherDto,
    @SerialName("daily") val daily: DailyForecastDto? = null
)

@Serializable
data class CurrentWeatherDto(
    @SerialName("temperature") val temperature: Double,
    @SerialName("windspeed") val windSpeed: Double,
    @SerialName("weathercode") val weatherCode: Int,
    @SerialName("is_day") val isDay: Int
)

@Serializable
data class DailyForecastDto(
    @SerialName("time") val time: List<String>,
    @SerialName("temperature_2m_max") val maxTemp: List<Double>,
    @SerialName("temperature_2m_min") val minTemp: List<Double>
)