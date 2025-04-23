package com.example.fitai.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Registro (onFinalizar: (Map<String, Any>) -> Unit) {
    var edad by remember { mutableStateOf(25f) }
    var peso by remember { mutableStateOf(70f) }
    var altura by remember { mutableStateOf(170f) }
    var tiempo by remember { mutableStateOf(45f) }

    val opcionesSexo = listOf("Masculino", "Femenino")
    var sexoSeleccionado by remember { mutableStateOf("Masculino") }


    var enfoqueSeleccionado by remember { mutableStateOf("Ambos") }
    val opcionesEnfoque = listOf("Ambos", "Tren Superior", "Tren Inferior")

    Column(
    modifier = Modifier
    .fillMaxSize()
    .padding(24.dp),
    verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Completa tus datos", style = MaterialTheme.typography.titleLarge)

        // Edad
        Text("Edad: ${edad.toInt()} años")
        Slider(value = edad, onValueChange = { edad = it }, valueRange = 18f..99f)

        //Sexo
        Text("Sexo", style = MaterialTheme.typography.titleMedium)
        Column {
            opcionesSexo.forEach { sexo ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = sexoSeleccionado == sexo,
                        onClick = { sexoSeleccionado = sexo }
                    )
                    Text(sexo)
                }
            }
        }

        // Peso
        Text("Peso: ${peso.toInt()} kg")
        Slider(value = peso, onValueChange = { peso = it }, valueRange = 30f..150f)

        // Altura
        Text("Altura: ${altura.toInt()} cm")
        Slider(value = altura, onValueChange = { altura = it }, valueRange = 130f..220f)

        // Tiempo disponible
        Text("Tiempo disponible: ${tiempo.toInt()} min")
        Slider(value = tiempo, onValueChange = { tiempo = it }, valueRange = 10f..120f)

        //Enfoque
        Text("Enfoque", style = MaterialTheme.typography.titleMedium)
        Column {
            opcionesEnfoque.forEach { enfoque ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = enfoqueSeleccionado == enfoque,
                        onClick = { enfoqueSeleccionado = enfoque }
                    )
                    Text(enfoque)
                }
            }
        }


        Button(onClick = {
            val datos = mapOf(
                "edad" to edad.toInt(),
                "sexo" to sexoSeleccionado,
                "peso" to peso.toInt(),
                "altura" to altura.toInt(),
                "tiempo" to tiempo.toInt(),
                "enfoque" to enfoqueSeleccionado
            )
            onFinalizar(datos)
        }) {
            Text("Finalizar")
        }
    }
}