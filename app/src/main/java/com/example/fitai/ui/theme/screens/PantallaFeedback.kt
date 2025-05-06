package com.example.fitai.ui.theme.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fitai.data.model.Feedback
import com.example.fitai.data.model.RutinaGenerada
import com.example.fitai.data.model.Usuario
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun PantallaFeedback(
    usuario: Usuario,
    rutina: RutinaGenerada,
    ejerciciosHechos: Map<String, Boolean>,
    navController: NavHostController
) {
    var fatiga by remember { mutableStateOf(3f) }
    var dificultad by remember { mutableStateOf(3f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("¿Cual es tu nivel de fatiga?", style = MaterialTheme.typography.titleMedium)
        Slider(value = fatiga, onValueChange = { fatiga = it }, valueRange = 1f..5f)

        Text("¿Te ha parecido dificil la rutina?", style = MaterialTheme.typography.titleMedium)
        Slider(value = dificultad, onValueChange = { dificultad = it }, valueRange = 1f..5f)

        Button(
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
                // y lo subimos a la bd
                Firebase.firestore.collection("feedback")
                    .add(feedback) // añadimos el objeto
                    .addOnSuccessListener {
                        Log.d("Firebase", "Feedback recogido")
                    }
                    .addOnFailureListener {
                        Log.e("Firebase", "Error al guardar usuario", it)
                    }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar feedback")
        }
    }
}
