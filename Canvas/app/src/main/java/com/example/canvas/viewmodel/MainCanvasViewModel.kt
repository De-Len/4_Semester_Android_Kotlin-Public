package com.example.canvas.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.canvas.data.database.CanvasDao
import com.example.canvas.data.database.CanvasEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch


@HiltViewModel
class MainCanvasViewModel @Inject constructor(
    private val canvasDao: CanvasDao
) : ViewModel() {

    private val _allCanvases: LiveData<List<CanvasEntity>> = canvasDao.getAllCanvasesEntity()
    val allCanvases: LiveData<List<CanvasEntity>> get() = _allCanvases

    fun deleteById(id: Int) {
        viewModelScope.launch {
            canvasDao.deleteById(id)
        }
    }

}