package com.example.rickandmortyapi.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.rickandmortyapi.Model.Network.RickAndMortyApi
import com.example.rickandmortyapi.Model.RickAndMortyCharacter
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout

class RickAndMortyCharacterViewModel : ViewModel() {
    private val _characters = MutableStateFlow<List<RickAndMortyCharacter>>(emptyList())
    val characters: StateFlow<List<RickAndMortyCharacter>> = _characters

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchCharacters() {
        val pageNumber: Int = (0..42).random()
        viewModelScope.launch {
            try {
                Log.d("CharacterViewModel", "Загрузка персонажей...")

                val response = withTimeout(10000) {
                    RickAndMortyApi.retrofitService.getCharacters(pageNumber)
                }

                _characters.value = response.results
                Log.d("CharacterViewModel", "Персонажи загружены: ${response.results.size}")
                _errorMessage.value = null  // Если загрузка успешна, очищаем ошибку

            } catch (e: TimeoutCancellationException) {
                Log.e("CharacterViewModel", "Ошибка: запрос превысил тайм-аут (10 секунд)", e)
                _errorMessage.value = "Запрос превысил тайм-аут. Попробуйте снова."

            } catch (e: Exception) {
                Log.e("CharacterViewModel", "Ошибка при загрузке персонажей", e)
                _errorMessage.value = "Произошла ошибка при загрузке данных."

            }
        }
    }

    init {
        fetchCharacters()
    }
}