package com.example.weatherapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.ui.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {

    // ViewModel
    val searchText = viewModel.cityQuery
    val searchResults = viewModel.searchResults
    val weatherData = viewModel.weatherData
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Weather App") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // --- 1. Requirement: Search ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { viewModel.cityQuery = it },
                    label = { Text("Enter the city") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { viewModel.searchCity() }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
            }

            // --- 2. LOADING INDICATOR ---
            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            // --- 3. ERROR ---
            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // --- 4. Search Results ---
            if (searchResults.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    items(searchResults) { city ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { viewModel.loadWeather(city) }, // When you click, we upload the weather
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = city.name, style = MaterialTheme.typography.titleMedium)
                                Text(text = "${city.country ?: ""} ", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }

            // --- 5. Requirement: Weather screen ---
            // We only show it if we have selected a city and the data has loaded
            weatherData?.let { weather ->
                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            text = viewModel.currentCityName,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        if (viewModel.isOffline) {
                            Text(
                                text = "OFFLINE MODE (Data from cache)",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        Text(
                            text = "Current weather",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${weather.currentWeather.temperature}°C",
                            style = MaterialTheme.typography.displayMedium
                        )

                        Text(text = "wind: ${weather.currentWeather.windSpeed} km/h")

                        // If there is a forecast (Daily)
                        if (weather.daily != null) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "7-day forecast",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            androidx.compose.foundation.lazy.LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val daily = weather.daily
                                items(daily.time.size) { index ->
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                                        ),
                                        modifier = Modifier.width(100.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(8.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            // data
                                            Text(
                                                text = daily.time[index].takeLast(5), // "2023-10-15" -> "10-15"
                                                style = MaterialTheme.typography.labelMedium
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            // Max temp
                                            Text(
                                                text = "${daily.maxTemp.getOrElse(index) { 0.0 }}°",
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                            // min temp
                                            Text(
                                                text = "${daily.minTemp.getOrElse(index) { 0.0 }}°",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}