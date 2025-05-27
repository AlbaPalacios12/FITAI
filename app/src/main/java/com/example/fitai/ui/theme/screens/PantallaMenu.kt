package com.example.fitai.ui.theme.screens

import RutinaGenerador
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fitai.data.model.Ejercicio
import com.example.fitai.data.model.Feedback
import com.example.fitai.data.model.RutinaGenerada
import com.example.fitai.data.model.Usuario
import com.example.fitai.grupoMuscularDelDia
import com.example.fitai.rutinaPlanSemanal
import com.example.fitai.ui.theme.FitAITheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PantallaMenu(
    ejercicios: List<Ejercicio>,
    usuario: Usuario,
    nombreUsuario: String,
    onRutinaGenerada: (RutinaGenerada) -> Unit,
    onCerrarSesion: () -> Unit,
    navController: NavController
) {
    var showDialog by remember { mutableStateOf(false) }

    FitAITheme {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            )

            {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                )

                {
                    Text(
                        text = "Hola, $nombreUsuario 👋",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    IconButton (onClick =  {showDialog = true }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false }, //para que se cierre
                            title = { Text("Cerrar sesion") },
                            text = { Text("¿Estas seguro de que quieres cerrar sesion?") },
                            confirmButton = {
                                Button(onClick = {
                                    showDialog = false
                                    onCerrarSesion() //función que se pasa para cerrar sesion
                                }) {
                                    Text("Si")
                                }
                            },
                            dismissButton = {
                                Button(onClick = { showDialog = false }) {
                                    Text("Cancelar")
                                }
                            }
                        )
                    }
                }

                Text(
                    text = "Hoy es ${obtenerFechaActual()}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(24.dp))

                val grupoHoy = grupoMuscularDelDia()
                Log.d("DEBUG", "Grupo muscular hoy: $grupoHoy") // Verifica que no sea null

                Button(
                    onClick = {
                        if (grupoHoy != null) {
                            val feedbackRef = Firebase.firestore.collection("feedback")
                                .whereEqualTo("userId", usuario.id)
                                .limit(1)

                            feedbackRef.get().addOnSuccessListener { snapshot ->
                                val feedback =
                                    snapshot.documents.firstOrNull()?.toObject(Feedback::class.java)

                                val fatiga = feedback?.fatiga ?: 5 // Valor medio por defecto
                                val dificultad = feedback?.dificultad ?: 5

                                val rutina = RutinaGenerador.generarRutinaInicial(
                                    context = navController.context,
                                    ejercicios = ejercicios,
                                    grupoObjetivo = grupoHoy,
                                    tiempoDisponible = usuario.tiempo,
                                    nivel = usuario.nivel,
                                    pesoUsuario = usuario.peso,
                                    edad = usuario.edad,
                                    sexo = if (usuario.sexo == "Masculino") 1 else 0,
                                    fatiga = fatiga,
                                    dificultad = dificultad
                                )
                                onRutinaGenerada(rutina)
                                Firebase.firestore.collection("rutina")
                                    .add(
                                        mapOf(
                                            "usuarioId" to usuario.id,
                                            "rutina_diaria" to rutina
                                        )
                                    )
                                    .addOnSuccessListener {
                                        Log.d("Firebase", "Rutina guardada correctamente")
                                    }
                                    .addOnFailureListener {
                                        Log.e("Firebase", "Error al guardar la rutina", it)
                                    }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF4C00),
                        disabledContainerColor = Color.Gray.copy(alpha = 0.5f)
                    ),
                    enabled = grupoHoy != null

                ) {
                    Text(
                        text = if (grupoHoy != null) {
                            "Comenzar entrenamiento de ${grupoHoy.name.lowercase() } 💪"
                        } else {
                            "Hoy toca descansar 😴"
                        },
                        color = Color.White,
                        style = TextStyle(fontStyle = FontStyle.Italic)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Tu entrenamiento semanal",
                    color = Color(0xFFFF3C00),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                val diasSemana = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
                LazyColumn {
                    items(diasSemana.indices.toList()) { index ->
                        val nombreDia = diasSemana[index]
                        val grupo = rutinaPlanSemanal[index + 1]

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Text(
                                    text = nombreDia,
                                    color = Color.White,
                                    style = TextStyle(fontWeight = FontWeight.Bold)
                                )

                                Text(
                                    text = grupo?.name?.lowercase()
                                        ?.replaceFirstChar { it.uppercaseChar() }
                                        ?.let { "Día de $it " } ?: "Descanso",
                                    style = TextStyle(fontStyle = FontStyle.Italic)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            navController.navigate("historial")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF4C00),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Historial de Rutinas")
                    }
                }
            }
            }
        }
    }

fun obtenerFechaActual(): String {
    val formatter = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "ES"))
    return formatter.format(Date())
}
