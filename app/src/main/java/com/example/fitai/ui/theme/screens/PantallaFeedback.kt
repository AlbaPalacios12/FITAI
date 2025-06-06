package com.example.fitai.ui.theme.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fitai.R
import com.example.fitai.data.model.Feedback
import com.example.fitai.data.model.RutinaGenerada
import com.example.fitai.data.model.Usuario
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun PantallaFeedback(
    usuario: Usuario,
    rutina: RutinaGenerada,
    navController: NavHostController,
    rutinaViewModel: RutinaViewModel = viewModel()
) {

    val ejerciciosHechos = rutinaViewModel.estadoHecho.toMap()
    Log.d("DEBUG", "EjerciciosHechos contiene: $ejerciciosHechos")

    var fatiga by remember { mutableStateOf(3f) }
    var dificultad by remember { mutableStateOf(3f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray) //antes que el padding porque sino sale un margen blanco
            .padding(30.dp),

        verticalArrangement = Arrangement.spacedBy(50.dp)
    ) {
        Text("Sensaciones",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFFFF3C00))

        //NO SE HA PUESTO EN EL MEDIO
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.sensaciones),
                contentDescription = "Sensaciones",
                modifier = Modifier.fillMaxWidth(0.5f)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            )
            {
                Text(
                    "¿Cual es tu nivel de fatiga?",
                    style = MaterialTheme.typography.titleMedium, color = Color.White
                )
                Slider(
                    value = fatiga,
                    onValueChange = { fatiga = it },
                    valueRange = 1f..5f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFFFF3C00),
                        activeTrackColor = Color(0xFFFF3C00)
                    )
                )
                Text("Nivel de fatiga: ${fatiga.toInt()}", color = Color.White)
            }
        }


        Box {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "¿Te ha parecido dificil la rutina?",
                    style = MaterialTheme.typography.titleMedium, color = Color.White
                )
                Slider(
                    value = dificultad,
                    onValueChange = { dificultad = it },
                    valueRange = 1f..5f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFFFF3C00),
                        activeTrackColor = Color(0xFFFF3C00)
                    )
                )
                Text("Nivel de dificultad: ${dificultad.toInt()}", color = Color.White)

            }
        }

        Spacer(modifier = Modifier.height(35.dp))
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF3C00),
                contentColor = Color.White,
                ),
            onClick = {
                //creamos el objeto feedback
                val feedback = Feedback(
                    userId = usuario.id,
                    fecha = System.currentTimeMillis(),
                    ejerciciosHechos = ejerciciosHechos,
                    fatiga = fatiga.toInt(),
                    dificultad = dificultad.toInt(),
                    rutinaId = rutina.id
                )
                Log.d("DEBUG", "Feedback completo: $feedback")
                // y lo subimos a la bd
                Firebase.firestore.collection("feedback")
                    .add(feedback) // añadimos el objeto
                    .addOnSuccessListener {
                        Log.d("Firebase", "Feedback recogido")
                    }
                    .addOnFailureListener {
                        Log.e("Firebase", "Error al guardar usuario", it)
                    }

                navController.navigate("menu")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar feedback")
        }
    }
}
