package com.example.weatherapp.data.model

data class FavoriteCity(
    val id: String = "",          // Уникальный ID записи в базе
    val name: String = "",
    val note: String = "",
    val userId: String = "",
    val timestamp: Long = 0       // for sorting
)
// All fields have default values (""), so that Firebase can automatically convert JSON into this class.
