
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fitai.data.model.Feedback
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun Historial(userId: String) {
    var historial by remember { mutableStateOf<List<Feedback>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }

    // carga los datos al entrar en la pantalla
    LaunchedEffect(userId) {
        FirebaseFirestore.getInstance()
            .collection("feedback")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { resultado ->
                val lista = resultado.documents.mapNotNull { it.toObject(Feedback::class.java) }
                historial = lista.sortedByDescending { it.fecha }
                cargando = false
            }
            .addOnFailureListener {
                cargando = false
            }
    }

    if (cargando) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize().background(Color.DarkGray).padding(16.dp)) {
            items(historial) { feedback ->
                RutinaCard(feedback)
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun RutinaCard(feedback: Feedback) {
    val fechaFormateada = remember(feedback.fecha) {
        SimpleDateFormat("dd/MM/yyyy").format(Date(feedback.fecha))
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("📅 Fecha: $fechaFormateada", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            //Text("🆔 Rutina ID: ${feedback.rutinaId}", style = MaterialTheme.typography.bodyMedium)
            Text(
                "✅ Ejercicios hechos: ${feedback.ejerciciosHechos.count { it.value }} de ${feedback.ejerciciosHechos.size}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text("💢 Fatiga: ${feedback.fatiga}", style = MaterialTheme.typography.bodyMedium)
            Text("🔥 Dificultad: ${feedback.dificultad}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
