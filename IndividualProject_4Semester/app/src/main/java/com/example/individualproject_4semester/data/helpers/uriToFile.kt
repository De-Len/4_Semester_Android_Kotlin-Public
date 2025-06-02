package com.example.individualproject_4semester.data.helpers

import android.content.Context
import android.net.Uri
import java.io.File

fun uriToFile(uri: Uri, context: Context): File {
    val fileName = "selected_audio_${System.currentTimeMillis()}.wav"
    val tempFile = File(context.cacheDir, fileName)

    context.contentResolver.openInputStream(uri)?.use { inputStream ->
        tempFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    } ?: throw IllegalArgumentException("Невозможно открыть InputStream для URI: $uri")

    return tempFile
}