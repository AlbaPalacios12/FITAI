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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
    fun PantallaBienvenida(navController: NavHostController) { //para decidir desde el main que va a pasar cuando se pulse el boton
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Bienvenido a FitAI", color = Color(0xFFFF3C00), style = MaterialTheme.typography.titleLarge)
            Text("Confíanos tu proceso", color = Color.White)
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = {navController.navigate("registro")}, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3C00))) {
                Text("Comenzar", color = Color.White)
            }
        }
    }

