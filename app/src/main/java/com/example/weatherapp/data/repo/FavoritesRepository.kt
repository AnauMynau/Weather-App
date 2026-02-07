package com.example.weatherapp.data.repo

import com.example.weatherapp.data.model.FavoriteCity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FavoritesRepository {

    // Ссылка на базу данных. Если не указать URL, берется тот, что в google-services.json
    private val database = FirebaseDatabase.getInstance("https://weatherapp-assignment8-84cd8-default-rtdb.firebaseio.com").reference
    private val auth = FirebaseAuth.getInstance()

    // 1. Функция для сохранения города (CREATE)
    fun addFavorite(cityName: String, note: String) {
        val userId = auth.currentUser?.uid

        // ДОБАВИМ ЛОГИ: Чтобы видеть в Logcat, что происходит
        if (userId == null) {
            println("FIREBASE ERROR: Пользователь не найден (uid is null). Подождите секунду.")
            return
        }

        val key = database.child("users").child(userId).child("favorites").push().key ?: return

        val city = FavoriteCity(
            id = key,
            name = cityName,
            note = note,
            userId = userId,
            timestamp = System.currentTimeMillis()
        )

        database.child("users").child(userId).child("favorites").child(key).setValue(city)
            .addOnSuccessListener { println("FIREBASE: Успешно сохранено!") }
            .addOnFailureListener { e -> println("FIREBASE ERROR: ${e.message}") }
    }

    // 2. Функция для получения списка в реальном времени (READ)
    // Мы используем Flow, чтобы UI обновлялся сам при любых изменениях в базе
    fun getFavorites(): Flow<List<FavoriteCity>> = callbackFlow {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            close() // Если нет юзера, закрываем поток
            return@callbackFlow
        }

        val ref = database.child("users").child(userId).child("favorites")

        // Слушатель событий базы данных
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<FavoriteCity>()
                for (child in snapshot.children) {
                    val item = child.getValue(FavoriteCity::class.java)
                    if (item != null) {
                        items.add(item)
                    }
                }
                trySend(items) // Отправляем новый список в UI
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        ref.addValueEventListener(listener)

        // Когда UI перестает слушать (например, экран закрыт), удаляем слушатель
        awaitClose {
            ref.removeEventListener(listener)
        }
    }

    // (DELETE)
    fun removeFavorite(cityId: String) {
        val userId = auth.currentUser?.uid ?: return
        database.child("users").child(userId).child("favorites").child(cityId).removeValue()
    }
}