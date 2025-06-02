package com.example.individualproject_4semester

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.individualproject_4semester.ui.screens.chatbotsscreen.ChatBotsScreen
import com.example.individualproject_4semester.ui.screens.imagegenerationscreen.ImageGenerationScreen
import com.example.individualproject_4semester.ui.screens.mainscreen.MainScreen
import com.example.individualproject_4semester.ui.screens.speechrecognitionscreen.SpeechRecognitionScreen
import com.example.individualproject_4semester.ui.screens.speechsynthesisscreen.SpeechSynthesisScreen
import com.example.individualproject_4semester.ui.theme.IndividualProject_4SemesterTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IndividualProject_4SemesterTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "main_screen"
                ) {
                    composable("main_screen") {
                        MainScreen(navController = navController) // ← ВАЖНО
                    }

                    composable("chat_bots_screen") {
                        ChatBotsScreen(navController = navController)
                    }
                    composable("image_generations_screen") {
                        ImageGenerationScreen(navController = navController)
                    }
                    composable("speech_recognition_screen") {
                        SpeechRecognitionScreen(navController = navController)
                    }
                    composable("speech_synthesis_screen") {
                        SpeechSynthesisScreen(navController = navController)
                    }

                }
            }
        }
    }

}

