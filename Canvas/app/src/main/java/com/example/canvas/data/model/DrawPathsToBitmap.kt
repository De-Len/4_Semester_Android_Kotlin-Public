package com.example.canvas.data.model

import android.graphics.Bitmap
import android.graphics.Paint
import androidx.compose.ui.graphics.toArgb
import androidx.room.TypeConverter
import com.example.canvas.viewmodel.PathData

fun drawPathsToBitmap(
    width: Int,
    height: Int,
    paths: List<PathData>
): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)

    paths.forEach { pathData ->
        val paint = Paint().apply {
            color = pathData.color.toArgb()
            strokeWidth = pathData.thickness
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }

        val androidPath = android.graphics.Path().apply {
            if (pathData.paths.isNotEmpty()) {
                moveTo(pathData.paths.first().x, pathData.paths.first().y)
                for (point in pathData.paths.drop(1)) {
                    lineTo(point.x, point.y)
                }
            }
        }

        canvas.drawPath(androidPath, paint)
    }

    return bitmap
}