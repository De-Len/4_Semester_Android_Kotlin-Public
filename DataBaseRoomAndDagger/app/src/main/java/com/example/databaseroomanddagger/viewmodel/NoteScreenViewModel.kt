package com.example.databaseroomanddagger.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.databaseroomanddagger.data.NoteDao
import com.example.databaseroomanddagger.data.NoteEntity
import kotlinx.coroutines.launch

@HiltViewModel
class NoteScreenViewModel @Inject constructor(
    private val noteDao: NoteDao
) : ViewModel() {

    private val _allNotes: LiveData<List<NoteEntity>> = noteDao.getAllNotesEntity()
    val allNotes: LiveData<List<NoteEntity>> get() = _allNotes

    fun deleteById(id: Int) {
        viewModelScope.launch {
            noteDao.deleteById(id)
        }
    }

}