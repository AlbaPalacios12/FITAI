package com.example.fitai.data.model

data class Usuario(
    val id: String = "",
    val nombre: String = "",
    val edad: Int = 0,
    val sexo: String = "",
    val peso: Double = 0.0,
    val altura: Double = 0.0,
    val tiempo: Int = 0,
    val enfoque: String = "" // superior, inferior, ambos
)
