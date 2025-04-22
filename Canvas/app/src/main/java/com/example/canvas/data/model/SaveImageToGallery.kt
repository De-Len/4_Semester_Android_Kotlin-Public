package com.example.canvas.data.model

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import java.io.IOException
import java.io.OutputStream

fun saveImageToGallery(context: Context, bitmap: Bitmap, title: String) {
    val contentResolver: ContentResolver = context.contentResolver
    val imageCollectionUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "$title.png")
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CanvasApp")  // For Android 10+ (Scoped Storage)
    }

    try {
        val uri = contentResolver.insert(imageCollectionUri, contentValues)
        uri?.let {
            val outputStream: OutputStream? = contentResolver.openOutputStream(it)
            outputStream?.let { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.close()
            } ?: run {
                Log.e("CanvasEditScreen", "Failed to get OutputStream.")
            }
        }
    } catch (e: IOException) {
        Log.e("CanvasEditScreen", "Error saving image", e)
    }
}