package com.example.individualproject_4semester.data.helpers

import android.util.Log
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream

fun saveAudio(body: ResponseBody): File {
    val fileName = "outputVoice${System.currentTimeMillis()}.wav"
    val tempDir = System.getProperty("java.io.tmpdir")
    val outputFile = File(tempDir, fileName)

    try {
        body.byteStream().use { input ->
            FileOutputStream(outputFile).use { output ->
                input.copyTo(output)
            }
        }
        Log.d("Сохранение аудио","Аудиофайл сохранён: ${outputFile.absolutePath}")
    } catch (e: Exception) {
        Log.e("Ошибка", "Ошибка при сохранении аудио: ${e.message}")
    }

    return outputFile
}