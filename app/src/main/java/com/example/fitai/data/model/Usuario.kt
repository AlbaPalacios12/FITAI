package com.example.fitai.data.model

data class Usuario(
    val id: String = "",
    val nombre: String = "",
    val edad: Int = 0,
    val sexo: String = "",
    val peso: Int = 0,
    val tiempo: Int = 0,
    val enfoque: String = "", // superior, inferior, ambos
    val nivel: String = "" // principiante, intermedio, avanzado
)


//he quitado la altura porque creo que no es relevante para ningun calculo