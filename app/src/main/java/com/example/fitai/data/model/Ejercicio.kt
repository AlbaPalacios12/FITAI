package com.example.fitai.data.model

enum class GrupoMuscular {
        PECHO, ESPALDA, PIERNA, CORE, GLUTEO, HOMBRO
}

data class Ejercicio (
        val id: String = "",
        val nombre: String = "",
        val grupo_muscular: GrupoMuscular = GrupoMuscular.PECHO,
        val material: String = "",
        val video: String = "",
        val nombre_legible: String = ""

)

