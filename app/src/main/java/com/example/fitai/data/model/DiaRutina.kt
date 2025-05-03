package com.example.fitai.data.model

data class DiaRutina(
        val dia: String,
        val enfoque: String?, // superior, inferior, mixto o descanso
        val rutina: RutinaGenerada? // solo si hay rutina, es decir, puede ser null
)

