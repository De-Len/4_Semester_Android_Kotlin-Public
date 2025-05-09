package com.example.lab6_images.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab6_images.data.ImageDao
import com.example.lab6_images.data.ImageEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ImageEditScreenViewModel @Inject constructor(
    private val imageDao: ImageDao
) : ViewModel() {

    fun insert(name: String, uri: String, description: String) {
        val image = ImageEntity(name = name, uri = uri, description = description)
        viewModelScope.launch {
            imageDao.insert(image)
        }
    }

    fun updateById(id: Int?, name: String, uri: String, description: String) {
        viewModelScope.launch {
            imageDao.updateById(id, name, uri, description)
        }
    }
}