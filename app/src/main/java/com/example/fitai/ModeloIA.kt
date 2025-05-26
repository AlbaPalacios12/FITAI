package com.example.fitai;
import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

public class ModeloIA(context: Context) {

    private val interpreter: Interpreter

    init {
        val assetFileDescriptor = context.assets.openFd("modelo_rutinas.tflite")
        val fileInputStream = assetFileDescriptor.createInputStream()
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        val model = fileChannel.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        interpreter = Interpreter(model)
    }

    fun predecir(input: FloatArray): Int {
        //entrada de los datos como un array de tipo float
        val inputBuffer = ByteBuffer.allocateDirect(4 * input.size).order(ByteOrder.nativeOrder())
        input.forEach { inputBuffer.putFloat(it) }
        inputBuffer.rewind()
        //y sale la prediccion que es un array de 3 elementos
        val output = Array(1) { FloatArray(3) }
        interpreter.run(inputBuffer, output)

        //clase con mayor probabilidad
        return output[0].indices.maxByOrNull { output[0][it] } ?: -1
    }
}
