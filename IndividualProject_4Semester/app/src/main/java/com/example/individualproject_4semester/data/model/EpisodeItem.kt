package com.example.individualproject_4semester.data.model

import androidx.annotation.DrawableRes

data class EpisodeItem(
    val text: String,
    @DrawableRes val imageRes: Int,
    val onClick: () -> Unit
)