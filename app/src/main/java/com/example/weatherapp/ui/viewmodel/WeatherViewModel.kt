package com.example.weatherapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.local.WeatherCache
import com.example.weatherapp.data.model.CityDto
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.repo.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val cache = WeatherCache(application)
    private val repository = WeatherRepository(cache)

    var cityQuery by mutableStateOf("")
    var searchResults by mutableStateOf<List<CityDto>>(emptyList())
    var weatherData by mutableStateOf<WeatherResponse?>(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isOffline by mutableStateOf(false)

    var currentCityName by mutableStateOf("")

    init {
        viewModelScope.launch {
            val cachedWeather = cache.getLastWeather()
            val cachedName = cache.getLastCityName()

            if (cachedWeather != null) {
                weatherData = cachedWeather
                currentCityName = cachedName ?: "Saved City"
                isOffline = true
            }
        }
    }

    fun searchCity() {
        if (cityQuery.isBlank()) return
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                searchResults = repository.searchCity(cityQuery)
            } catch (e: Exception) {
                errorMessage = "Search error"
            } finally {
                isLoading = false
            }
        }
    }

    fun loadWeather(city: CityDto) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            searchResults = emptyList()

            //remember the city name
            currentCityName = city.name
            cache.saveCityName(city.name)

            try {
                val (data, offlineStatus) = repository.getWeatherData(city.latitude, city.longitude)
                weatherData = data
                isOffline = offlineStatus
            } catch (e: Exception) {
                errorMessage = "No internet connection and no saved data"
            } finally {
                isLoading = false
            }
        }
    }
}