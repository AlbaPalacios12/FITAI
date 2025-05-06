package com.example.fitai.ui.theme.screens

import RutinaGenerador
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fitai.data.model.DiaRutina
import com.example.fitai.data.model.Ejercicio
import com.example.fitai.data.model.RutinaGenerada
import com.example.fitai.data.model.Usuario
import com.example.fitai.ui.theme.FitAITheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PantallaMenu(navController: NavHostController, ejercicios: List<Ejercicio>, usuario: Usuario, nombreUsuario: String, onRutinaGenerada: (RutinaGenerada) -> Unit ,  rutinaSemanal: List<DiaRutina>) {
    FitAITheme {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Hola, $nombreUsuario 👋",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Hoy es ${obtenerFechaActual()}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(24.dp))


                Button(

                    onClick = {
                        val rutina = RutinaGenerador.generarRutinaInicial(
                            ejercicios = ejercicios, //lista pasada como parametro
                            enfoque = usuario.enfoque,
                            tiempoDisponible = usuario.tiempo,
                            nivel = usuario.nivel,
                            pesoUsuario = usuario.peso
                        )
                        onRutinaGenerada(rutina)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4C00))
                ) {
                    Text("¡Comenzar entrenamiento!", color = Color.White, style = TextStyle(fontSize = 18.sp))

                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Tu entrenamiento semanal",
                   // style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFFFF3C00),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyColumn {
                    items(rutinaSemanal) { dia ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Text(text = dia.dia, color = Color.White, style = TextStyle(fontWeight = FontWeight.Bold))

                                Text(
                                    text = dia.enfoque?.replaceFirstChar { it.uppercaseChar() } ?: "Descanso",
                                    style = TextStyle(fontStyle = FontStyle.Italic)
                                )

                            }
                        }
                    }
                }
            }
        }
    }
}

// fecha actual
fun obtenerFechaActual(): String {
    val formatter = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "ES"))
    return formatter.format(Date())
}
