package com.example.individualproject_4semester.data.remote.response.speechsynthesisresponse

data class SpeechSynthesisRequest(
    val model: String,
    val input: String,
    val voice: String,
    val response_format: String = "wav"
)