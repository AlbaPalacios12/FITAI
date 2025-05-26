package com.example.fitai.ui.theme.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fitai.YouTubePlayer
import com.example.fitai.data.model.RutinaGenerada
import com.example.fitai.extractYouTubeId

@Composable
fun Rutina(rutina: RutinaGenerada, navController: NavHostController ,  rutinaViewModel: RutinaViewModel = viewModel()) {
    //como tenemos que guardar cada uno de los checkbox, lo hacemos con un mapa
    //metemos el nombre del ejercicio y si esta hecho o no

    if (rutinaViewModel.estadoHecho.isEmpty()) {
        rutina.ejercicios.forEach {
            rutinaViewModel.estadoHecho[it.ejercicio.nombre_legible] = false
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .padding(16.dp)

    ) {
        item {
            Text("Tu rutina",  style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFFFF3C00)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        items(rutina.ejercicios) { ejercicio ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)

            ) {
                Column(
                    modifier = Modifier.padding(16.dp)


                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //nombre
                       Text(
                           text = ejercicio.ejercicio.nombre_legible,
                           color = Color.Black,
                           style = TextStyle(fontWeight = FontWeight.Bold),
                           modifier = Modifier.weight(1f)
                       )
                        Checkbox(
                            checked = rutinaViewModel.estadoHecho[ejercicio.ejercicio.nombre_legible] ?: false,
                            onCheckedChange = {
                                rutinaViewModel.estadoHecho[ejercicio.ejercicio.nombre_legible] = it
                            },
                            colors = CheckboxDefaults.colors(Color(0xFFFF3C00))
                        )
                        Text(
                            text = if (rutinaViewModel.estadoHecho[ejercicio.ejercicio.nombre_legible] == true) "Done" else "Do",
                            color = if (rutinaViewModel.estadoHecho[ejercicio.ejercicio.nombre_legible] == true) Color.Green else Color.Red,
                            style = TextStyle(fontWeight = FontWeight.Bold)
                        )

                    }

                    Text("${ejercicio.series} x ${ejercicio.repeticiones} reps")

                    if (ejercicio.cargaKg > 0) {
                        Text("Peso: ${ejercicio.cargaKg} kg")
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    YouTubePlayer(
                        videoId = extractYouTubeId(ejercicio.ejercicio.video) ?: "", // Usa videoUrl y extrae el ID
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp)) //redondea el video
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))


                    }
                }
            }
        item{
            Button( colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF3C00),
                contentColor = Color.White),

                onClick = {
                    navController.navigate("feedback")
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Finalizar rutina", color = Color.White)

            }
        }
    }
}
