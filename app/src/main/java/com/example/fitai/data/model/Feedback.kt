package com.example.fitai.data.model

data class Feedback (
    val userId: String,
    val fecha: Long,
    val ejerciciosHechos: Map<String, Boolean> = emptyMap(),
    val fatiga: Int,
    val dificultad: Int,
    val rutinaId: String
)
