
import android.content.Context
import android.util.Log
import com.example.fitai.ModeloIA
import com.example.fitai.data.model.Ejercicio
import com.example.fitai.data.model.EjercicioRutina
import com.example.fitai.data.model.GrupoMuscular
import com.example.fitai.data.model.RutinaGenerada
import java.util.UUID

object RutinaGenerador {

    //le paso un contexto para poder acceder a la carpeta de assets
    fun generarRutinaInicial(
        context: Context,
        ejercicios: List<Ejercicio>, //la lista de ejercicios que viene de la bd
        grupoObjetivo: GrupoMuscular, //nueva variable asociada al tipo de ejercicio
        tiempoDisponible: Int,
        nivel: String,
        pesoUsuario: Int,
        edad:Int,
        sexo: Int, //0 si es mujer, 1 si es hombre
        fatiga: Int,
        dificultad: Int
    ): RutinaGenerada {
        // instancia para el modelo
        val modeloIA = ModeloIA(context)

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
            // codificamos one-hot del grupo muscular (0-5)
            //esto es como yo tengo una variable que es un enum
            val oneHotGrupo = FloatArray(6) { 0f }
            oneHotGrupo[grupoObjetivo.ordinal] = 1f

            val entrada = floatArrayOf(
                edad.toFloat(),
                sexo.toFloat(),
                when (nivel){
                    "principiante" -> 0f
                    "intermedio" -> 1f
                    "avanzado" -> 2f
                    else -> 0f
                },
                fatiga.toFloat(),
                dificultad.toFloat(),
            ) + oneHotGrupo
            Log.d("IA", "Entrada del modelo: ${entrada.contentToString()}")
            val accion = modeloIA.predecir(entrada)

            Log.d("IA", "Resultado del modelo: $accion")
            val carga = calcularCargaInicial(ejercicio, nivel, pesoUsuario, accion)

            EjercicioRutina(ejercicio = ejercicio, cargaKg = carga)
        }

        return RutinaGenerada(
            id = UUID.randomUUID().toString(),
            ejercicios = rutina
        )
    }

    //calcula la carga inicial según el nivel y la acción del modelo
    private fun calcularCargaInicial(ejercicio: Ejercicio, nivel: String, pesoUsuario: Int, accion: Int): Int {
        if (ejercicio.material == "ninguno") return 0

        //predefinido
        val porcentaje = when (nivel) {
            "principiante" -> 0.2
            "intermedio" -> 0.4
            "avanzado" -> 0.7
            else -> 0.3
        }

        val ajuste = when (accion) {
            0 -> 1.0 //mantener
            1 -> 0.8 //adaptar
            2 -> 0.0 //cambiar ejercicio --> no carga
            else -> 1.0
        }

        return (pesoUsuario * porcentaje * ajuste).toInt()
    }
}