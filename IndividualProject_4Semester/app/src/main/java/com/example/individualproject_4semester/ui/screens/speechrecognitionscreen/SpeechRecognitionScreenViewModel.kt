package com.example.individualproject_4semester.ui.screens.speechrecognitionscreen

import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.individualproject_4semester.data.remote.ProxyApiRepository
import com.example.individualproject_4semester.data.remote.response.speechrecognitionresponse.SpeechRecognitionRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SpeechRecognitionScreenViewModel @Inject constructor(
    private val repository: ProxyApiRepository
): ViewModel() {
    var audioRecognizedText by mutableStateOf<String>("")
    var isLoading by mutableStateOf(false)
        private set

    lateinit var audioFile: File

    fun transcribeAudio(model: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val request = SpeechRecognitionRequest(model = model, file = audioFile)
                val result = repository.transcribeAudio(request)
                audioRecognizedText = result.text

                Log.d("Ответ", "Звук распознан")
            }
            catch (e: Exception) {
                Log.e("Ошибка", e.message.toString())
            }
            finally {
                isLoading = false
            }
        }
    }
}