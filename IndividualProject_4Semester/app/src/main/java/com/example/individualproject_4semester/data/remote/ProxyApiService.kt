package com.example.individualproject_4semester.data.remote

import com.example.individualproject_4semester.data.remote.response.chatbotsresponse.ChatBotsResult
import com.example.individualproject_4semester.data.remote.response.chatbotsresponse.ChatBotsRequest
import com.example.individualproject_4semester.data.remote.response.imagegenerationresponse.ImageGenerationRequest
import com.example.individualproject_4semester.data.remote.response.imagegenerationresponse.ImageGenerationResult
import com.example.individualproject_4semester.data.remote.response.speechrecognitionresponse.SpeechRecognitionRequest
import com.example.individualproject_4semester.data.remote.response.speechrecognitionresponse.SpeechRecognitionResult
import com.example.individualproject_4semester.data.remote.response.speechsynthesisresponse.SpeechSynthesisRequest
import com.example.individualproject_4semester.data.remote.response.speechsynthesisresponse.SpeechSynthesisResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface ProxyApiService {
    @POST("openai/v1/responses")
    suspend fun getChatBotsResponse(
        @Body request: ChatBotsRequest
    ): ChatBotsResult

    @POST("openai/v1/images/generations")
    suspend fun generateImage(
        @Body request: ImageGenerationRequest
    ): ImageGenerationResult

    @Multipart
    @POST("openai/v1/audio/transcriptions")
    suspend fun transcribeAudio(
        @Part("model") model: RequestBody,
        @Part file: MultipartBody.Part
    ): SpeechRecognitionResult

    @POST("openai/v1/audio/speech")
    suspend fun generateVoice(
        @Body request: SpeechSynthesisRequest
    ): ResponseBody
}