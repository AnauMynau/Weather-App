package com.example.weatherapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.ui.viewmodel.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = viewModel(),
    onBack: () -> Unit, // Функция для возврата назад
    onCitySelected: (String) -> Unit // <--- 1. НОВЫЙ ПАРАМЕТР: Функция "Город выбран"
) {
    val favorites by viewModel.favorites.collectAsState()
    var cityName by remember { mutableStateOf("") }
    var cityNote by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Избранное") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        // Используем правильную иконку для новых версий Compose
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Поля ввода
            OutlinedTextField(
                value = cityName,
                onValueChange = { cityName = it },
                label = { Text("Город") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = cityNote,
                onValueChange = { cityNote = it },
                label = { Text("Заметка") },
                modifier = Modifier.fillMaxWidth()
            )

            // Кнопка добавления
            Button(
                onClick = {
                    if (cityName.isNotBlank()) {
                        viewModel.addCity(cityName, cityNote)
                        cityName = ""
                        cityNote = ""
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Добавить")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider()

            // СПИСОК (Вот он должен появиться!)
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(favorites) { city ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        onClick = { onCitySelected(city.name) }, // <--- 2. ДОБАВИЛИ КЛИК ПО КАРТОЧКЕ
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(city.name, style = MaterialTheme.typography.titleMedium)
                                if (city.note.isNotEmpty()) {
                                    Text(city.note, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                            IconButton(onClick = { viewModel.removeCity(city.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}