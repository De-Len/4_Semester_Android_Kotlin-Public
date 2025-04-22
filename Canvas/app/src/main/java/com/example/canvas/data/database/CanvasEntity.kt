package com.example.canvas.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "canvases")
data class CanvasEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String
)
