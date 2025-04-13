package com.example.databaseroomanddagger.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.databaseroomanddagger.data.NoteDao
import com.example.databaseroomanddagger.data.NoteEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class NoteEditScreenViewModel @Inject constructor(
    private val noteDao: NoteDao
) : ViewModel() {

    fun insert(title: String, content: String) {
        val note = NoteEntity(title = title, content = content)
        viewModelScope.launch {
            noteDao.insert(note)
        }
    }

    fun updateById(id: Int?, title: String, content: String) {
        viewModelScope.launch {
            noteDao.updateById(id, title, content)
        }
    }
}