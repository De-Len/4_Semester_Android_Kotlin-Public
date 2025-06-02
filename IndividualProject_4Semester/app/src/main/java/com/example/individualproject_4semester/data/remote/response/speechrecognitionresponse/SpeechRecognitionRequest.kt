package com.example.individualproject_4semester.data.remote.response.speechrecognitionresponse

import okhttp3.RequestBody
import java.io.File

data class SpeechRecognitionRequest(
    val model: String,
    val file: File
)
