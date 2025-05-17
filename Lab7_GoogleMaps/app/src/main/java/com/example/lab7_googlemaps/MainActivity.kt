package com.example.lab7_googlemaps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.lab7_googlemaps.ui.MapScreen
import com.example.lab7_googlemaps.ui.theme.Lab7_GoogleMapsTheme
import com.example.lab7_googlemaps.viewmodel.MapViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab7_GoogleMapsTheme {
                val mapViewModel = MapViewModel()
                MapScreen(mapViewModel)
            }
        }
    }
}

