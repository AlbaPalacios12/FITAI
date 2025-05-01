package com.example.fitai.ui.theme.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fitai.data.model.RutinaGenerada
import com.example.fitai.ui.theme.FitAITheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Rutina(rutina: RutinaGenerada) {
    FitAITheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Tu rutina generada") }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Descanso entre ejercicios: ${rutina.descansoEntreEjercicios} segundos",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    items(rutina.ejercicios) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = item.ejercicio.nombre,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text("Series: ${item.series}")
                                Text("Repeticiones: ${item.repeticiones}")
                                Text("Carga: ${item.cargaKg} kg")
                            }
                        }
                    }
                }
            }
        }
    }
}
