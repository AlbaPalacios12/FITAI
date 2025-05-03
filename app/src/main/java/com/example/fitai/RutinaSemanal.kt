package com.example.fitai

import RutinaGenerador
import com.example.fitai.data.model.DiaRutina
import com.example.fitai.data.model.Ejercicio
import com.example.fitai.data.model.Usuario

fun generarSemanaCompleta(
    ejercicios: List<Ejercicio>,
    usuario: Usuario
): List<DiaRutina> {
    val dias = listOf("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo")

    val enfoqueSemana = when (usuario.enfoque.lowercase()) {
        "superior" -> listOf("superior", null, "mixto", null, "superior", "inferior", null)
        "inferior" -> listOf("inferior", null, "mixto", null, "inferior", "superior", null)
        else ->       listOf("mixto", null, "superior", null, "mixto", "inferior", null)
    }

    return dias.mapIndexed { index, dia ->
        val enfoqueDia = enfoqueSemana[index]
        val rutina = enfoqueDia?.let {
            RutinaGenerador.generarRutinaInicial(
                ejercicios = ejercicios,
                enfoque = it,
                tiempoDisponible = usuario.tiempo,
                nivel = usuario.nivel,
                pesoUsuario = usuario.peso
            )
        }

        DiaRutina(dia = dia, enfoque = enfoqueDia, rutina = rutina)
    }
}
