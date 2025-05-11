package com.example.fitai.data.model

data class EjercicioRutina(
    val ejercicio: Ejercicio,
    val series: Int= 3,
    val repeticiones: Int = 12,
    val cargaKg: Int
)
