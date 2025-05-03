package com.example.fitai.ui.theme.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fitai.YouTubePlayer
import com.example.fitai.data.model.RutinaGenerada
import com.example.fitai.extractYouTubeId

@Composable
fun Rutina(rutina: RutinaGenerada) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text("Tu rutina", style = MaterialTheme.typography.headlineSmall)
            Text("Descanso: ${rutina.descansoEntreEjercicios} seg")
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(rutina.ejercicios) { ejercicio ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Nombre y detalles
                    Text(ejercicio.ejercicio.nombre_legible, style = MaterialTheme.typography.titleMedium)
                    Text("${ejercicio.series} x ${ejercicio.repeticiones} reps")

                    if (ejercicio.cargaKg > 0) {
                        Text("Peso: ${ejercicio.cargaKg} kg")
                    }

                        Spacer(modifier = Modifier.height(8.dp))
                        YouTubePlayer(
                            videoId = extractYouTubeId(ejercicio.ejercicio.video) ?: "", // Usa videoUrl y extrae el ID
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                        )
                    }
                }
            }
        }
    }
