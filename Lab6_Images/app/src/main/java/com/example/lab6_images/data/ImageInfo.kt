package com.example.lab6_images.data

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import android.util.Log

data class ImageInfo(
    val name: String,
    val uri: String
)


class ImageInfoList() {
    companion object {
        lateinit var ImageList: List<ImageInfo>

        fun init(contentResolver: ContentResolver) {
            ImageList = readImages(contentResolver)
        }

        private fun readImages(contentResolver: ContentResolver): List<ImageInfo> {
            val imageList = mutableListOf<ImageInfo>()

            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME
            )

            val cursor = contentResolver.query(
                uri,
                projection,
                null,
                null,
                "${MediaStore.Images.Media.DATE_ADDED} DESC"
            )

            cursor?.use {
                val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

                while (it.moveToNext()) {
                    val id = it.getLong(idColumn)
                    val name = it.getString(nameColumn)
                    val contentUri = ContentUris.withAppendedId(uri, id)

                    imageList.add(ImageInfo(name, contentUri.toString()))

                    Log.d("Image", "Name: $name, Uri: $contentUri")
                }
            }
            return imageList
        }
    }
}

