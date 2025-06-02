package com.example.individualproject_4semester.ui.screens.chatbotsscreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.individualproject_4semester.data.remote.response.chatbotsresponse.ChatBotsRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.individualproject_4semester.data.remote.ProxyApiRepository
import kotlinx.coroutines.launch

@HiltViewModel
class ChatBotsScreenViewModel @Inject constructor(
    private val repository: ProxyApiRepository
) : ViewModel() {
    var responseText by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun sendMessage(model: String, input: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val request = ChatBotsRequest(model, input)
                val result = repository.getResponse(request)

                responseText = result.output.firstOrNull()
                    ?.content?.firstOrNull()
                    ?.text ?: "Нет ответа"

                Log.d("Ответ", responseText)
            }
            catch (e: Exception) {
                responseText = "Ошибка: ${e.message}"
                Log.e("Ошибка", e.message.toString())
            }
            finally {
                isLoading = false
            }

        }
    }
}