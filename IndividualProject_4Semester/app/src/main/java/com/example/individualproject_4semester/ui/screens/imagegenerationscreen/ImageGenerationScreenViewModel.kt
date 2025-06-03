package com.example.individualproject_4semester.ui.screens.imagegenerationscreen

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.individualproject_4semester.data.remote.ProxyApiRepository
import com.example.individualproject_4semester.data.remote.response.imagegenerationresponse.ImageGenerationRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@HiltViewModel
class ImageGenerationScreenViewModel @Inject constructor(
    private val repository: ProxyApiRepository
): ViewModel() {
    var imageBitmap by mutableStateOf<ImageBitmap?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set


    fun generateImage(
        model: String,
        input: String,
        size: String? = null,
        quality: String? = null,
        background: String? = null
    ) {
        viewModelScope.launch {
            isLoading = true
            try {
                if (model == "gpt-image-1") {
                    val request = ImageGenerationRequest(model = model, prompt = input, size = size, quality = quality, background = background, format = null, response_format = null)
                    val result = repository.generateImage(request)
                    imageBitmap = base64ToImageBitmap(result.data.firstOrNull()?.b64_json)
                }
                else {
                    val request = ImageGenerationRequest(model = model, prompt = input, size = size, response_format = "b64_json", quality = quality, format = null, background = null)
                    val result = repository.generateImage(request)
                    imageBitmap = base64ToImageBitmap(result.data.firstOrNull()?.b64_json)
                }
                Log.d("Ответ", "Картинка сгеренена")
            }
            catch (e: Exception) {
                Log.e("Ошибка", e.message.toString())
            }
            finally {
                isLoading = false
            }

        }

    }

    @OptIn(ExperimentalEncodingApi::class)
    fun base64ToImageBitmap(base64Str: String?): ImageBitmap? {
        if (base64Str == null) return null
        return try {
            val bytes = Base64.decode(base64Str, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            bitmap?.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }
}