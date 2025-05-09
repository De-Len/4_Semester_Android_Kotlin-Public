package com.example.lab6_images.viewmodel

import android.content.ClipDescription
import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.lab6_images.data.ImageDao
import com.example.lab6_images.data.ImageEntity
import com.example.lab6_images.data.ImageInfo
import com.example.lab6_images.data.ImageInfoList
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ImageScreenViewModel @Inject constructor(
    private val imageDao: ImageDao
) : ViewModel() {
    init {
        loadImagesToDatabase()
    }

    private val _allImages: LiveData<List<ImageEntity>> = imageDao.getAllImagesEntity()
    val allImages: LiveData<List<ImageEntity>> get() = _allImages

    fun deleteById(id: Int) {
        viewModelScope.launch {
            imageDao.deleteById(id)
        }
    }

    fun insert(name: String, uri: String, description: String) {
        val image = ImageEntity(name = name, uri = uri, description = description)
        viewModelScope.launch {
            imageDao.insert(image)
        }
    }

    fun updateById(id: Int?, name: String, uri: String, description: String) {
        viewModelScope.launch {
            imageDao.updateById(id, name, uri, "")
        }
    }

    fun loadImagesToDatabase() {
        viewModelScope.launch {
            val currentImages = imageDao.getAllImagesEntityList()
            val allImagesNames = currentImages.map { it.name }

            ImageInfoList.ImageList.forEach { image ->
                if (image.name !in allImagesNames) {
                    insert(image.name, image.uri, "")
                }
            }
        }
    }

}