package com.example.fitai

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.fitai.ui.theme.screens.PantallaBienvenida
import com.example.fitai.ui.theme.screens.Registro
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            var pantallaActual by remember { mutableStateOf("bienvenida") }

            when (pantallaActual) {
                "bienvenida" -> PantallaBienvenida {
                    pantallaActual = "registro"
                }

                "registro" -> Registro { datos ->
                    Firebase.firestore.collection("usuarios")
                        .add(datos)
                        .addOnSuccessListener {
                            Log.d("Firebase", "Usuario guardado")
                            pantallaActual = "finalizado"
                        }
                        .addOnFailureListener {
                            Log.e("Firebase", "Error", it)
                        }
                }

                "finalizado" -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Registro completado correctamente", style = MaterialTheme.typography.headlineSmall)
                }
            }
        }
    }
}

