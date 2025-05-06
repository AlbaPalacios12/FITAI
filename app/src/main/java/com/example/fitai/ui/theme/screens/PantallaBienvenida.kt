package com.example.fitai.ui.theme.screens

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fitai.R

@Composable
    fun PantallaBienvenida(navController: NavHostController) { //para decidir desde el main que va a pasar cuando se pulse el boton
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(36.dp))
            Text("Bienvenido a FitAI", color = Color(0xFFFF3C00),
                style = TextStyle(fontSize = 36.sp, fontWeight = FontWeight.Bold))

            Text("Confíanos tu proceso",
                color = Color.White,
                style = TextStyle(fontSize = 20.sp)
                )
            Image(
                painter = painterResource(id = R.drawable.logo), //
                contentDescription = "Logo",
            )

            Button(onClick = {navController.navigate("registro")},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3C00)),

            ) {
                Text("Comenzar", color = Color.White, style = TextStyle(fontSize = 18.sp))
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }



