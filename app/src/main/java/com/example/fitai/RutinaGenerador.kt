import android.util.Log
import com.example.fitai.data.model.Ejercicio
import com.example.fitai.data.model.EjercicioRutina
import com.example.fitai.data.model.GrupoMuscular
import com.example.fitai.data.model.RutinaGenerada
import java.util.UUID

object RutinaGenerador {

    fun generarRutinaInicial(
        ejercicios: List<Ejercicio>, //la lista de ejercicios que viene de la bd
        grupoObjetivo: GrupoMuscular, //nueva variable asociada al tipo de ejercicio
        tiempoDisponible: Int,
        nivel: String,
        pesoUsuario: Int
    ): RutinaGenerada {
        // Debug para ver ejercicios antes de filtrar
        Log.d("GENERADOR", "Ejercicios recibidos: ${ejercicios.size}")
        Log.d("GENERADOR", "Primer ejercicio: ${ejercicios.firstOrNull()?.nombre} ")

        // 1º filtro según el grupo muscular
        val filtrados = ejercicios.filter { it.grupo_muscular == grupoObjetivo}

        val tiempoPorEjercicio = 7 //contando con que son 3 series (1 por serie y 2 de descanso y habrá dos descansos)
        //tope de ejercicios que es posible meter por rutina/dia
        val ejerciciosTotales = tiempoDisponible / tiempoPorEjercicio

        //escogemos uno al azar
        val seleccionados = filtrados.shuffled().take(ejerciciosTotales)

        //y lo metemos en una lista de ejercicios

        val rutina = seleccionados.map { ejercicio ->
            EjercicioRutina(
                ejercicio = ejercicio,
                cargaKg = calcularCargaInicial(ejercicio, nivel, pesoUsuario)
            )
        }

        return RutinaGenerada(
            id = UUID.randomUUID().toString(),
            ejercicios = rutina
        )
    }

    private fun calcularCargaInicial(ejercicio: Ejercicio, nivel: String, pesoUsuario: Int): Int {
        if (ejercicio.material == "ninguno") return 0

        val porcentaje = when (nivel) {
            "principiante" -> 0.2
            "intermedio" -> 0.4
            "avanzado" -> 0.7
            else -> 0.3
        }

        return (pesoUsuario * porcentaje).toInt()
    }
}