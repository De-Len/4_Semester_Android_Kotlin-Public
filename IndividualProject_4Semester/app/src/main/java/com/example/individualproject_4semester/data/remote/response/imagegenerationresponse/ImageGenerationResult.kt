package com.example.individualproject_4semester.data.remote.response.imagegenerationresponse

data class ImageGenerationResult(
    val data: List<ImageData>
)

data class ImageData(
    val b64_json: String
)