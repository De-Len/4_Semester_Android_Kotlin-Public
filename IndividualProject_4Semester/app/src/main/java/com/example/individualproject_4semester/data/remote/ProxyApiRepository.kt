package com.example.individualproject_4semester.data.remote

import com.example.individualproject_4semester.data.remote.response.chatbotsresponse.ChatBotsRequest
import com.example.individualproject_4semester.data.remote.response.chatbotsresponse.ChatBotsResult
import com.example.individualproject_4semester.data.remote.response.imagegenerationresponse.ImageGenerationRequest
import com.example.individualproject_4semester.data.remote.response.imagegenerationresponse.ImageGenerationResult
import com.example.individualproject_4semester.data.remote.response.speechrecognitionresponse.SpeechRecognitionRequest
import com.example.individualproject_4semester.data.remote.response.speechrecognitionresponse.SpeechRecognitionResult
import com.example.individualproject_4semester.data.remote.response.speechsynthesisresponse.SpeechSynthesisRequest
import com.example.individualproject_4semester.data.remote.response.speechsynthesisresponse.SpeechSynthesisResult
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject


class ProxyApiRepository @Inject constructor(
    private val api: ProxyApiService
) {
    suspend fun getResponse(request: ChatBotsRequest): ChatBotsResult {
        return api.getChatBotsResponse(request)
    }

    suspend fun generateImage(request: ImageGenerationRequest): ImageGenerationResult {
        return api.generateImage(request)
    }

    suspend fun transcribeAudio(request: SpeechRecognitionRequest): SpeechRecognitionResult {
        val audioFile = request.file
        val mediaType = "audio/wav".toMediaTypeOrNull() // wav/mp3/m4a/webm
        val fileRequestBody = request.file.asRequestBody(mediaType)

        val filePart = MultipartBody.Part.createFormData("file", audioFile.name, fileRequestBody)

        val modelPart = request.model.toRequestBody("text/plain".toMediaType())

        return api.transcribeAudio(modelPart, filePart)
    }

    suspend fun generateVoice(request: SpeechSynthesisRequest): ResponseBody {
        return api.generateVoice(request)
    }
}