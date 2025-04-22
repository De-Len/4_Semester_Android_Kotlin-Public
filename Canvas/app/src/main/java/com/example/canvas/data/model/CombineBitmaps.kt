package com.example.canvas.data.model
import android.graphics.Bitmap
import android.graphics.Canvas


fun combineBitmaps(base64Img1: String, base64Img2: String): Bitmap {
    // Преобразуем обе строки base64 в Bitmap
    val bitmap1 = base64ToBitmap(base64Img1)
    val bitmap2 = base64ToBitmap(base64Img2)

    // Создаем новый Bitmap, чтобы вместить оба изображения
    val width = maxOf(bitmap1.width, bitmap2.width)
    val height = maxOf(bitmap1.height, bitmap2.height) // Делаем высоту минимальной из двух изображений

    // Новый Bitmap для комбинированного изображения
    val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(resultBitmap)

    // Рисуем первое изображение на холсте
    canvas.drawBitmap(bitmap1, 0f, 0f, null)

    // Рисуем второе изображение поверх первого (координаты 0f, 0f — наложение на верхний левый угол)
    canvas.drawBitmap(bitmap2, 0f, 0f, null)

    return resultBitmap
}