package com.example.fitai.ui.theme.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fitai.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun PantallaBienvenida(
    navController: NavHostController,
    onLoginSuccess: (String) -> Unit,
    onGoToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var showLoginForm by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(36.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Bienvenido a FitAI",
                color = Color(0xFFFF3C00),
                style = TextStyle(fontSize = 36.sp, fontWeight = FontWeight.Bold),

            )

            Text(
                "Confíanos tu proceso",
                color = Color.White,
                style = TextStyle(fontSize = 20.sp)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
        )

        if (showLoginForm) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it.trim()
                        error = null
                    },
                    label = { Text("Email", color = Color.White) },
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it.trim()
                        error = null
                    },
                    label = { Text("Contraseña", color = Color.White) },
                    visualTransformation = PasswordVisualTransformation(),
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (error != null) {
                    Text(error!!, color = Color.Red)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            error = "Completa todos los campos"
                            return@Button
                        }
                        Log.d("LOGIN", "Email: $email - Password: $password")
                        Firebase.auth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener {
                                val userId = it.user?.uid ?: return@addOnSuccessListener
                                onLoginSuccess(userId)
                            }
                            .addOnFailureListener {
                                error = it.message ?: "Error al iniciar sesión"
                            }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3C00))
                ) {
                    Text("Entrar", color = Color.White, style = TextStyle(fontSize = 18.sp))
                }

                TextButton(onClick = onGoToRegister) {
                    Text(
                        "¿No tienes cuenta? Regístrate",
                        color = Color.White
                    )
                }
            }
        } else {
            Button(
                onClick = { showLoginForm = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3C00))
            ) {
                Text("Comenzar", color = Color.White, style = TextStyle(fontSize = 18.sp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}