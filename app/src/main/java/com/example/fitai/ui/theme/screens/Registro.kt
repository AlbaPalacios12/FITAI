
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitai.data.model.Usuario
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun Registro(onFinalizar: (Usuario) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf(25f) }
    var peso by remember { mutableStateOf(70f) }
    var tiempo by remember { mutableStateOf(45f) }

    val opcionesSexo = listOf("Masculino", "Femenino")
    var sexoSeleccionado by remember { mutableStateOf("Masculino") }

    val opcionesNivel = listOf("Principiante", "Intermedio", "Avanzado")
    var nivelSeleccionado by remember { mutableStateOf("Principiante") }

    val scrollState = rememberScrollState()
    var error by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Completa tus datos",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFFFF3C00)
        )

        // Datos personales
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Datos personales",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFFFF3C00)
                )

                Text("Nombre: ", color = Color.Black, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        if (error) error = false
                    },
                    label = { Text("Nombre", color = Color.Black) },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (error) {
                    Text(
                        "El nombre no puede estar vacio",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Text("Edad: ${edad.toInt()} años", color = Color.Black, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                Slider(
                    value = edad,
                    onValueChange = { edad = it },
                    valueRange = 18f..99f,
                    colors = SliderDefaults.colors(
                        thumbColor =  Color(0xFFFF3C00),
                        activeTrackColor =  Color(0xFFFF3C00)
                    )
                )

                Text("Sexo", color = Color.Black, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                opcionesSexo.forEach { sexo ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = sexoSeleccionado == sexo,
                            onClick = { sexoSeleccionado = sexo },
                            colors = RadioButtonDefaults.colors(
                                selectedColor =  Color(0xFFFF3C00)
                            )
                        )
                        Text(sexo, color = Color.Black)
                    }
                }
            }
        }

        // Fisico
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Condición física",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFFFF3C00)
                )

                Text("Peso: ${peso.toInt()} kg", color = Color.Black, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                Slider(
                    value = peso,
                    onValueChange = { peso = it },
                    valueRange = 30f..150f,
                    colors = SliderDefaults.colors(
                        thumbColor =  Color(0xFFFF3C00),
                        activeTrackColor =  Color(0xFFFF3C00)
                    )
                )

                Text("Tiempo disponible: ${tiempo.toInt()} min", color = Color.Black, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                Slider(
                    value = tiempo,
                    onValueChange = { tiempo = it },
                    valueRange = 10f..120f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFFFF3C00),
                        activeTrackColor = Color(0xFFFF3C00)
                    )
                )
            }
        }

        // Objetivo
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Entrenamiento",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFFFF3C00)
                )


                Text("Nivel", color = Color.Black, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                opcionesNivel.forEach { nivel ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = nivelSeleccionado == nivel,
                            onClick = { nivelSeleccionado = nivel },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFFFF3C00)
                            )
                        )
                        Text(nivel, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }

        // Botón
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF3C00),
                contentColor = Color.White,

            ),
            onClick = {
                if (nombre.isBlank()) {
                    error = true
                } else {
                    val userId = Firebase.auth.currentUser?.uid ?: return@Button
                    val usuario = Usuario(
                        id = userId, //esto luego me permite que los id coincidan cuando lo busco con firebase
                        nombre = nombre,
                        edad = edad.toInt(),
                        peso = peso.toInt(),
                        tiempo = tiempo.toInt(),
                        sexo = sexoSeleccionado,
                        nivel = nivelSeleccionado
                    )
                    onFinalizar(usuario)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Finalizar",
                style = TextStyle(fontSize = 18.sp))
        }
    }
}
