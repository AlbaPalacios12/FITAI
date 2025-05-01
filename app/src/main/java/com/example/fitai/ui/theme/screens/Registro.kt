
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
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fitai.data.model.Usuario

@Composable
fun Registro (onFinalizar: (Usuario) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf(25f) }
    var peso by remember { mutableStateOf(70f) }
    var altura by remember { mutableStateOf(170f) }
    var tiempo by remember { mutableStateOf(45f) }

    val opcionesSexo = listOf("Masculino", "Femenino")
    var sexoSeleccionado by remember { mutableStateOf("Masculino") }

    val opcionesEnfoque = listOf("Ambos", "Tren Superior", "Tren Inferior")
    var enfoqueSeleccionado by remember { mutableStateOf("Ambos") }

    val opcionesNivel = listOf("Principiante", "Intermedio", "Avanzado")
    var nivelSeleccionado by remember { mutableStateOf("Principiante") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Completa tus datos", style = MaterialTheme.typography.headlineSmall)

        // Datos personales
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Datos personales", style = MaterialTheme.typography.titleMedium)

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Edad: ${edad.toInt()} años")
                Slider(value = edad, onValueChange = { edad = it }, valueRange = 18f..99f)

                Text("Sexo")
                opcionesSexo.forEach { sexo ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = sexoSeleccionado == sexo, onClick = { sexoSeleccionado = sexo })
                        Text(sexo)
                    }
                }
            }
        }

        // Físico
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Condición física", style = MaterialTheme.typography.titleMedium)

                Text("Peso: ${peso.toInt()} kg")
                Slider(value = peso, onValueChange = { peso = it }, valueRange = 30f..150f)

                Text("Altura: ${altura.toInt()} cm")
                Slider(value = altura, onValueChange = { altura = it }, valueRange = 130f..220f)

                Text("Tiempo disponible: ${tiempo.toInt()} min")
                Slider(value = tiempo, onValueChange = { tiempo = it }, valueRange = 10f..120f)
            }
        }

        // Objetivo
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Objetivo de entrenamiento", style = MaterialTheme.typography.titleMedium)

                Text("Enfoque")
                opcionesEnfoque.forEach { enfoque ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = enfoqueSeleccionado == enfoque, onClick = { enfoqueSeleccionado = enfoque })
                        Text(enfoque)
                    }
                }

                Text("Nivel")
                opcionesNivel.forEach { nivel ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = nivelSeleccionado == nivel, onClick = { nivelSeleccionado = nivel })
                        Text(nivel)
                    }
                }
            }
        }

        // Botón
        Button(
            onClick = {
                val usuario = Usuario(
                    nombre = nombre,
                    edad = edad.toInt(),
                    sexo = sexoSeleccionado,
                    peso = peso.toInt(),
                    altura = altura.toInt(),
                    tiempo = tiempo.toInt(),
                    enfoque = enfoqueSeleccionado,
                    nivel = nivelSeleccionado
                )
                onFinalizar(usuario)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
        ) {
            Text("Finalizar")
        }
    }
}
