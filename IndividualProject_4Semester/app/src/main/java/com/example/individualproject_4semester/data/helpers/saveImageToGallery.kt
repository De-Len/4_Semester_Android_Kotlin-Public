package com.example.individualproject_4semester.data.helpers

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import java.io.OutputStream


fun saveImageToGallery(context: Context, bitmap: Bitmap, name: String = "generated_image"): Boolean {
    val filename = "$name-${System.currentTimeMillis()}.png"
    val mimeType = "image/png"
    val compressFormat = Bitmap.CompressFormat.PNG

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/GeneratedImages")
        }
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    uri?.let {
        val stream: OutputStream? = resolver.openOutputStream(it)
        stream?.use {
            bitmap.compress(compressFormat, 100, it)
        }
        return true
    }

    return false
}