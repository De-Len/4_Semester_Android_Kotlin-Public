package com.example.individualproject_4semester.data.remote.response.speechsynthesisresponse

import okhttp3.ResponseBody
import retrofit2.Call

data class SpeechSynthesisResult(
    val audioFile: Call<ResponseBody>
)