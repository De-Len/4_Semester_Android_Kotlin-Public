package com.example.rickandmortyapi.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.rickandmortyapi.Model.Network.RickAndMortyApi
import com.example.rickandmortyapi.Model.RickAndMortyCharacter

class RickAndMortyCharacterViewModel : ViewModel() {
    private val _characters = MutableStateFlow<List<RickAndMortyCharacter>>(emptyList())
    val characters: StateFlow<List<RickAndMortyCharacter>> = _characters

    fun fetchCharacters() {
        val pageNumber: Int = (0..42).random()
        viewModelScope.launch {
            try {
                Log.d("CharacterViewModel", "Загрузка персонажей...")
                val response = RickAndMortyApi.retrofitService.getCharacters(pageNumber)
                _characters.value = response.results
                Log.d("CharacterViewModel", "Персонажи загружены: ${response.results.size}")
            } catch (e: Exception) {
                Log.e("CharacterViewModel", "Ошибка при загрузке персонажей", e)
            }
        }
    }

    init {
        fetchCharacters()
    }
}