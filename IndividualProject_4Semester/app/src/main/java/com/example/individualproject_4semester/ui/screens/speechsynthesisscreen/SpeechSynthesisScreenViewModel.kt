package com.example.individualproject_4semester.ui.screens.speechsynthesisscreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.individualproject_4semester.data.helpers.saveAudio
import com.example.individualproject_4semester.data.remote.ProxyApiRepository
import com.example.individualproject_4semester.data.remote.response.speechsynthesisresponse.SpeechSynthesisRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SpeechSynthesisScreenViewModel @Inject constructor(
    val repository: ProxyApiRepository
): ViewModel() {
    var isLoading by mutableStateOf<Boolean>(false)
        private set

    var outputFile: File? = null

    fun generateVoice(
        model: String,
        input: String,
        voice: String,
        response_format: String = "wav"
    ) {
        viewModelScope.launch {
            isLoading = true
            try {
                val request = SpeechSynthesisRequest(model = model,
                    input = input,
                    voice = voice,
                    response_format = response_format)

//                val response = repository.generateVoice(request).audioFile.execute()
                val response = repository.generateVoice(request)

                outputFile = saveAudio(response)


            }
            catch (e: Exception) {
                Log.e("Ошибка", e.toString())
            }
            finally {
                isLoading = false
            }

        }
    }
}