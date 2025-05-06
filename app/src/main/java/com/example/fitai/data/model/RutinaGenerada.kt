package com.example.fitai.data.model

data class RutinaGenerada(
    val id: String,
    val ejercicios: List<EjercicioRutina>,
    val descansoEntreEjercicios: Int
)
