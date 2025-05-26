package com.example.fitai

import Registro
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitai.data.model.Ejercicio
import com.example.fitai.data.model.RutinaGenerada
import com.example.fitai.data.model.Usuario
import com.example.fitai.ui.theme.screens.PantallaBienvenida
import com.example.fitai.ui.theme.screens.PantallaFeedback
import com.example.fitai.ui.theme.screens.PantallaMenu
import com.example.fitai.ui.theme.screens.Rutina
import com.example.fitai.ui.theme.screens.RutinaViewModel
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            val navController = rememberNavController()
            var usuarioRegistrado by remember { mutableStateOf<Usuario?>(null) }
            var rutinaGenerada by remember { mutableStateOf<RutinaGenerada?>(null) }
            var ejercicios by remember { mutableStateOf<List<Ejercicio>>(emptyList()) }
            val rutinaViewModel: RutinaViewModel = viewModel()

            // Cargar datos iniciales
            LaunchedEffect(Unit) {
                val prefs = getSharedPreferences("Sesion", MODE_PRIVATE)
                val idUsuario = prefs.getString("idUsuario", null)

                Firebase.firestore.collection("ejercicios").get()
                    .addOnSuccessListener { ejercicios = it.toObjects(Ejercicio::class.java) }

                if (idUsuario != null) {
                    Firebase.firestore.collection("usuarios")
                        .whereEqualTo("id", idUsuario)
                        .get()
                        .addOnSuccessListener { result ->
                            usuarioRegistrado = result.documents[0].toObject(Usuario::class.java)
                        }
                }
            }

            NavHost(navController = navController, startDestination = "bienvenida") {
                // Pantalla Bienvenida
                composable("bienvenida") {
                    if (usuarioRegistrado != null) {
                        LaunchedEffect(Unit) {
                            navController.navigate("menu") { popUpTo("bienvenida") { inclusive = true } }
                        }
                    }
                    PantallaBienvenida(navController)
                }

                // Pantalla Registro
                composable("registro") {
                    Registro { datosUsuario ->
                        Firebase.firestore.collection("usuarios").add(datosUsuario)
                            .addOnSuccessListener {
                                usuarioRegistrado = datosUsuario
                                getSharedPreferences("Sesion", MODE_PRIVATE).edit().apply {
                                    putString("idUsuario", datosUsuario.id)
                                    putString("nombreUsuario", datosUsuario.nombre)
                                    apply()
                                }
                                navController.navigate("menu") { popUpTo("bienvenida") }
                            }
                    }
                }

                // Pantalla Menú Principal
                composable("menu") {
                    if (usuarioRegistrado == null) {
                        // Redirigir si no hay usuario
                        LaunchedEffect(Unit) {
                            navController.navigate("bienvenida") { popUpTo("menu") { inclusive = true } }
                        }
                        CircularProgressIndicator()
                    } else {
                        PantallaMenu(
                            ejercicios = ejercicios,
                            usuario = usuarioRegistrado!!,
                            nombreUsuario = usuarioRegistrado!!.nombre,
                            onRutinaGenerada = { nuevaRutina ->
                                rutinaGenerada = nuevaRutina
                                navController.navigate("rutina")
                            },
                            onCerrarSesion = {
                                // Limpiar datos y redirigir
                                usuarioRegistrado = null
                                rutinaGenerada = null
                                getSharedPreferences("Sesion", MODE_PRIVATE).edit().clear().apply()
                                navController.navigate("bienvenida") { popUpTo("menu") { inclusive = true } }
                            },
                            navController = navController
                        )
                    }
                }

                // Pantalla Rutina
                composable("rutina") {
                    if (rutinaGenerada == null) {
                        LaunchedEffect(Unit) {
                            navController.navigate("menu") { popUpTo("rutina") { inclusive = true } }
                        }
                        CircularProgressIndicator()
                    } else {
                        Rutina(rutinaGenerada!!, navController, rutinaViewModel)
                    }
                }
                composable("feedback") {
                    val usuario = usuarioRegistrado ?: return@composable
                    val rutina = rutinaGenerada ?: return@composable

                    PantallaFeedback(
                        usuario = usuario,
                        rutina = rutina,
                        navController = navController,
                        rutinaViewModel = rutinaViewModel
                    )
                }
            }
        }
    }
}





