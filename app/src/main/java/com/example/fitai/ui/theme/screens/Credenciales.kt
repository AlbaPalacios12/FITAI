package com.example.fitai.ui.theme.screens

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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun Credenciales(onRegisterSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Registro",
            color = Color(0xFFFF3C00),
            style = TextStyle(fontSize = 36.sp, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = Color.White) },
            textStyle = TextStyle(color = Color.White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedBorderColor = Color(0xFFFF3C00),
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color(0xFFFF3C00),
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña", color = Color.White) },
            textStyle = TextStyle(color = Color.White),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedBorderColor = Color(0xFFFF3C00),
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color(0xFFFF3C00),
            ),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    error = "Completa todos los campos"
                    return@Button
                }
                Firebase.auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        onRegisterSuccess()
                    }
                    .addOnFailureListener {
                        error = it.message ?: "Error al registrar usuario"
                    }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF3C00),
                contentColor = Color.White
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Registrarse", style = TextStyle(fontSize = 18.sp))
        }

        if (error != null) {
            Text(
                error!!,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}