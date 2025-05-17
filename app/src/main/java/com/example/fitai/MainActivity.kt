package com.example.fitai

import Registro
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        setContent {
            //para poder retroceder tenemos que incluir un navController (hay que importar esta dependencia)
            val navController = rememberNavController()
            var usuarioRegistrado by remember { mutableStateOf<Usuario?>(null) }
            var rutinaGenerada by remember { mutableStateOf<RutinaGenerada?>(null) }
            var ejercicios by remember { mutableStateOf<List<Ejercicio>>(emptyList()) }

            val rutinaViewModel: RutinaViewModel = viewModel() //SOLO INICIALIZAMOS UNA VEZ


            LaunchedEffect(Unit) {
                // cargamos el usuario guardado
                val prefs = getSharedPreferences("Sesion", MODE_PRIVATE)
                val idUsuario = prefs.getString("idUsuario", null)

                //para cargar los ejercicios desde Firestore al iniciar la app
                Firebase.firestore.collection("ejercicios")
                    .get()
                    .addOnSuccessListener { result ->
                        ejercicios = result.toObjects(Ejercicio::class.java)
                        Log.d("DEBUG", "Total ejercicios cargados: ${ejercicios.size}")
                    }

                if (idUsuario != null) {
                    // lo buscamos en la coleccion
                    Firebase.firestore.collection("usuarios")
                        .whereEqualTo("id", idUsuario)
                        .get()
                        .addOnSuccessListener { result ->
                            if (!result.isEmpty) {
                                usuarioRegistrado = result.documents[0].toObject(Usuario::class.java)
                                navController.navigate("menu")
                            }
                        }
                }

            }
            LaunchedEffect(usuarioRegistrado) {
                if (usuarioRegistrado != null && ejercicios.isNotEmpty()) {
                    navController.navigate("menu") {
                        popUpTo("bienvenida") { inclusive = true }
                    }
                }
            }

            NavHost(navController = navController, startDestination = "bienvenida") {
                composable("bienvenida") {
                    PantallaBienvenida(navController)

                }


                    composable("registro") {
                        Registro { datos: Usuario -> //este es el objeto que hemos creado en el registro

                            Firebase.firestore.collection("usuarios")
                                .add(datos) // aqui añadimos el objeto con los datos del usuario
                                .addOnSuccessListener {
                                    Log.d("Firebase", "Usuario guardado")
                                    usuarioRegistrado = datos //datos

                                    // sesion
                                    val prefs = getSharedPreferences("Sesion", MODE_PRIVATE)
                                    with(prefs.edit()) {
                                        //voy a guardar id y nombre
                                        putString("idUsuario", datos.id)
                                        putString("nombreUsuario", datos.nombre)
                                        apply()
                                    }
                                    navController.navigate("finalizado_registro") //con esto informaremos que hemos terminado el registro correctamente
                                }
                                .addOnFailureListener {
                                    Log.e("Firebase", "Error al guardar usuario", it)
                                }
                        }
                    }
                    composable("finalizado_registro") {
                        // esto ejecutará después de 2 segundos la pantalla menu automaticamente
                        LaunchedEffect(Unit) {
                            delay(2000)
                            navController.navigate("menu") {
                                popUpTo("registro") // borra las pantallas que hay entre menu y registro
                                launchSingleTop =
                                    true // para no volver a generar otra instancia de la pantalla menu
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
                                ejercicios = ejercicios,
                                usuario = usuarioRegistrado!!,
                                nombreUsuario = usuarioRegistrado!!.nombre,
                                onRutinaGenerada = { nuevaRutina ->
                                    // Limpia el estado anterior y prepara los ejercicios
                                    rutinaViewModel.estadoHecho.clear()
                                    nuevaRutina.ejercicios.forEach { ejercicio ->
                                        rutinaViewModel.estadoHecho[ejercicio.ejercicio.nombre_legible] =
                                            false
                                    }
                                    rutinaGenerada = nuevaRutina
                                    navController.navigate("rutina")
                                },
                                onCerrarSesion = {
                                    val prefs = getSharedPreferences("Sesion", MODE_PRIVATE)
                                    with(prefs.edit()) {
                                        clear()
                                        apply()
                                    }
                                    navController.navigate("bienvenida") {
                                        popUpTo("menu") { inclusive = true } //borramos el menu
                                    }
                                }
                            )
                        }
                    }

                    composable("rutina") {
                        rutinaGenerada?.let { rutina ->
                            Rutina(
                                rutina = rutina,
                                navController = navController,
                                rutinaViewModel = rutinaViewModel // EL INICIALIZADO AQUI SE PASA COMO PARAMETRO
                            )
                        }
                    }
                    composable("feedback") {
                        val usuario = usuarioRegistrado ?: return@composable
                        val rutina = rutinaGenerada ?: return@composable

                        PantallaFeedback(
                            usuario = usuario,
                            rutina = rutina,
                            navController = navController,
                            rutinaViewModel = rutinaViewModel // AQUI TAMBIEN SE PASA COMO PARAMETRO

                        )
                    }
                    composable("finalizado_feedback") {
                        // esto ejecutará después de 2 segundos la pantalla menu automaticamente
                        LaunchedEffect(Unit) {
                            delay(2000)
                            navController.navigate("menu") {
                                // popUpTo("registro") // borra las pantallas que hay entre menu y registro
                                launchSingleTop =
                                    true // para no volver a generar otra instancia de la pantalla menu
                            }

                        }
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Sensaciones registradas",
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    }
                }
            }

        }
    }






