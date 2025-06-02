package com.example.individualproject_4semester.data.helpers

import android.content.Context
import java.io.File

fun rawResourceToFile(context: Context, resId: Int, fileName: String): File {
    val inputStream = context.resources.openRawResource(resId)
    val tempFile = File(context.cacheDir, fileName)
    tempFile.outputStream().use { output ->
        inputStream.copyTo(output)
    }
    return tempFile
}