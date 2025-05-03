package com.example.fitai

import Registro
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitai.data.model.DiaRutina
import com.example.fitai.data.model.Ejercicio
import com.example.fitai.data.model.RutinaGenerada
import com.example.fitai.data.model.Usuario
import com.example.fitai.ui.theme.screens.PantallaBienvenida
import com.example.fitai.ui.theme.screens.PantallaMenu
import com.example.fitai.ui.theme.screens.Rutina
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        setContent {
            //para poder retroceder tenemos que incluir un navController (hay que importar esta dependencia)
            val navController = rememberNavController()
            var usuarioRegistrado by remember { mutableStateOf<Usuario?>(null) }
            var rutinaGenerada by remember { mutableStateOf<RutinaGenerada?>(null) }
            var ejercicios by remember { mutableStateOf<List<Ejercicio>>(emptyList()) }
            var semana by remember { mutableStateOf<List<DiaRutina>>(emptyList()) }


            //para cargar los ejercicios desde Firestore al iniciar la app
            LaunchedEffect(Unit) {
                Firebase.firestore.collection("ejercicios")
                    .get()
                    .addOnSuccessListener { result ->
                        ejercicios = result.toObjects(Ejercicio::class.java)
                        Log.d("DEBUG", "Total ejercicios cargados: ${ejercicios.size}")
                    }
            }

            // Generar semana cuando se registre el usuario
            LaunchedEffect(usuarioRegistrado) {
                usuarioRegistrado?.let { usuario ->
                    semana = generarSemanaCompleta(ejercicios, usuario)
                }
            }

            NavHost(navController = navController, startDestination = "bienvenida") {
                composable("bienvenida") {
                    PantallaBienvenida(navController)
                }

                composable("registro") {
                    Registro { datos: Usuario -> //este es el objeto que hemos creado en el registro
                        // guardamos en Firestore y cambiamos el estado
                        Firebase.firestore.collection("usuarios")
                            .add(datos) // aqui añadimos el objeto con los datos del usuario
                            .addOnSuccessListener {
                                Log.d("Firebase", "Usuario guardado")
                                usuarioRegistrado = datos
                                navController.navigate("finalizado") //con esto informaremos que hemos terminado el registro correctamente
                            }
                            .addOnFailureListener {
                                Log.e("Firebase", "Error al guardar usuario", it)
                            }
                    }
                }
                composable("finalizado") {
                    // esto ejecutará después de 2 segundos la pantalla menu automaticamente
                    LaunchedEffect(Unit) {
                        delay(2000)
                        navController.navigate("menu") {
                            popUpTo("registro") // borra las pantallas que hay entre menu y registro
                            launchSingleTop = true // para no volver a generar otra instancia de la pantalla menu
                        }

                    }
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Registro completado correctamente",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
                composable("menu") {
                    if (ejercicios.isEmpty()) {
                        CircularProgressIndicator() // Muestra carga mientras espera
                    } else {
                        PantallaMenu(
                            navController = navController,
                            ejercicios = ejercicios,
                            usuario = usuarioRegistrado!!,
                            nombreUsuario = usuarioRegistrado!!.nombre,
                            onRutinaGenerada = { nuevaRutina ->
                                Log.d("DEBUG", "Ejercicios en rutina: ${nuevaRutina.ejercicios.size}")
                                rutinaGenerada = nuevaRutina
                                navController.navigate("rutina")
                            },
                            rutinaSemanal = semana
                        )
                    }
                }

                composable("rutina") {
                    rutinaGenerada?.let { rutina ->
                        Rutina(rutina)
                    }
                }
            }

        }
        }
    }




