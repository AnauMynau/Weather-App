package com.example.weatherapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.FavoriteCity
import com.example.weatherapp.data.repo.FavoritesRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {

    private val repository = FavoritesRepository()
    private val auth = FirebaseAuth.getInstance()

    // Список городов, который будет наблюдать UI
    private val _favorites = MutableStateFlow<List<FavoriteCity>>(emptyList())
    val favorites: StateFlow<List<FavoriteCity>> = _favorites.asStateFlow()

    init {
        signInAndLoad()
    }

    private fun signInAndLoad() {
        // Проверяем, вошли ли мы уже в систему
        if (auth.currentUser == null) {
            // Если нет - делаем анонимный вход (требование задания)
            auth.signInAnonymously().addOnSuccessListener {
                // Успех! Теперь у нас есть UID, можем слушать базу
                observeFavorites()
            }.addOnFailureListener { e ->
                // В реальном приложении тут нужно показать ошибку
                println("Error signing in: ${e.message}")
            }
        } else {
            // Если уже вошли - сразу начинаем слушать базу
            observeFavorites()
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            // Подписываемся на Flow из репозитория
            repository.getFavorites().collect { list ->
                _favorites.value = list
            }
        }
    }

    // Метод для добавления (вызывается из UI)
    fun addCity(name: String, note: String) {
        repository.addFavorite(name, note)
    }

    // Метод для удаления (вызывается из UI)
    fun removeCity(cityId: String) {
        repository.removeFavorite(cityId)
    }
}