package com.example.fitai

import Historial
import Registro
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.example.fitai.ui.theme.screens.Credenciales
import com.example.fitai.ui.theme.screens.PantallaBienvenida
import com.example.fitai.ui.theme.screens.PantallaFeedback
import com.example.fitai.ui.theme.screens.PantallaMenu
import com.example.fitai.ui.theme.screens.Rutina
import com.example.fitai.ui.theme.screens.RutinaViewModel
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
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
            var isLoading by remember { mutableStateOf(true) }

            val rutinaViewModel: RutinaViewModel = viewModel()

            // Cargar datos iniciales
            LaunchedEffect(Unit) {
                val prefs = getSharedPreferences("Sesion", MODE_PRIVATE)
                val idUsuario = prefs.getString("idUsuario", null)

                Firebase.firestore.collection("ejercicios").get()
                    .addOnSuccessListener { ejercicios = it.toObjects(Ejercicio::class.java) }

                if (idUsuario != null) {
                    Firebase.firestore.collection("usuarios").document(idUsuario).get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                usuarioRegistrado = document.toObject(Usuario::class.java)
                            }
                            isLoading = false
                        }
                        .addOnFailureListener {
                            Log.e("MainActivity", "Error al obtener los datos del usuario", it)
                        }
                } else {
                    isLoading = false
                }
            }

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                NavHost(navController = navController, startDestination = "bienvenida") {
                    composable("bienvenida") {
                        PantallaBienvenida(
                            navController = navController,
                            onLoginSuccess = { userId ->
                                Firebase.firestore.collection("usuarios").document(userId).get()
                                    .addOnSuccessListener { doc ->
                                        val datosUsuario = doc.toObject(Usuario::class.java)
                                        usuarioRegistrado = datosUsuario

                                        getSharedPreferences("Sesion", MODE_PRIVATE).edit().apply {
                                            putString("idUsuario", userId)
                                            putString("nombreUsuario", datosUsuario?.nombre)
                                            apply()
                                        }
                                        navController.navigate("menu")
                                    }
                            },
                            onGoToRegister = { navController.navigate("credenciales") }
                        )
                    }

                    composable("credenciales") {
                        Credenciales(
                            onRegisterSuccess = {
                                navController.navigate("registro")
                            }
                        )
                    }

                    composable("registro") {
                        Registro { datosUsuario ->
                            val userId = Firebase.auth.currentUser?.uid ?: return@Registro

                            val usuarioConId = datosUsuario.copy(id = userId)
                            Firebase.firestore.collection("usuarios").document(userId).set(usuarioConId)
                                .addOnSuccessListener {
                                    usuarioRegistrado = usuarioConId
                                    getSharedPreferences("Sesion", MODE_PRIVATE).edit().apply {
                                        putString("idUsuario", userId)
                                        putString("nombreUsuario", usuarioConId.nombre)
                                        apply()
                                    }
                                    navController.navigate("menu")
                                }
                        }
                    }

                    composable("menu") {
                        if (usuarioRegistrado == null) {
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
                                    //limpiamos el viewModel
                                    rutinaViewModel.estadoHecho.clear()
                                    nuevaRutina.ejercicios.forEach {
                                        rutinaViewModel.estadoHecho[it.ejercicio.nombre_legible] = false
                                    }
                                    //asignamos rutina
                                    rutinaGenerada = nuevaRutina
                                    navController.navigate("rutina")
                                },
                                onCerrarSesion = {
                                    usuarioRegistrado = null
                                    rutinaGenerada = null
                                    getSharedPreferences("Sesion", MODE_PRIVATE).edit().clear().apply()
                                    navController.navigate("bienvenida") { popUpTo("menu") { inclusive = true } }
                                },
                                navController = navController
                            )
                        }
                    }

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

                    composable("historial") {
                        Historial(userId = usuarioRegistrado?.id ?: "")
                    }
                }
            }
        }
    }
}