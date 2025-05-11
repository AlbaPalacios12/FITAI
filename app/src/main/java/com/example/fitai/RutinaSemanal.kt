package com.example.fitai

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.fitai.data.model.GrupoMuscular
import java.time.LocalDate

val rutinaPlanSemanal = mapOf(
    1 to GrupoMuscular.PECHO,
    2 to GrupoMuscular.PIERNA,
    3 to GrupoMuscular.ESPALDA,
    4 to null,
    5 to GrupoMuscular.GLUTEO,
    6 to GrupoMuscular.HOMBRO,
    7 to null
)
//rutina establecida para la semana

@RequiresApi(Build.VERSION_CODES.O)
fun grupoMuscularDelDia(): GrupoMuscular? {
    val hoy = LocalDate.now().dayOfWeek.value // sacamos el numero del dia de la semana que corresponde a hoy
    return rutinaPlanSemanal[hoy] //devolvemos la rutina del grupo muscular correspondiente
}

