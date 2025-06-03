package com.example.individualproject_4semester.data.remote.response.imagegenerationresponse

data class ImageGenerationRequest(
    val model: String,
    val prompt: String,
    val size: String?,
    val quality: String?,
    val format: String?,
    val background: String?,
    val response_format: String?,
)