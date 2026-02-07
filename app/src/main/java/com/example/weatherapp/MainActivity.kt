package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.ui.screens.FavoritesScreen
import com.example.weatherapp.ui.theme.WeatherAppTheme

// ВАЖНО: Убедитесь, что эта строка добавлена в импорты!
import com.example.weatherapp.ui.screens.WeatherScreen
import com.example.weatherapp.ui.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showFavorites by remember { mutableStateOf(false) }

                    // ПОЛУЧАЕМ ГЛАВНУЮ VIEWMODEL ЗДЕСЬ, чтобы она жила дольше экранов
                    val weatherViewModel: WeatherViewModel = viewModel()

                    if (showFavorites) {
                        FavoritesScreen(
                            onBack = { showFavorites = false },
                            onCitySelected = { cityName ->

                                // ИСПОЛЬЗУЕМ НОВУЮ ФУНКЦИЮ ЗДЕСЬ
                                weatherViewModel.onFavoriteCitySelected(cityName)

                                // Закрываем экран избранного
                                showFavorites = false
                            }
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize()) {
                            // Передаем ту же viewModel внутрь, чтобы она показала результат
                            WeatherScreen(viewModel = weatherViewModel)

                            Button(
                                onClick = { showFavorites = true },
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 32.dp)
                            ) {
                                Text("Открыть Избранное")
                            }
                        }
                    }
                }
            }
        }
    }
}