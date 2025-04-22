package com.example.canvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.canvas.ui.canvasevitscreen.CanvasEditScreen
import com.example.canvas.ui.maincanvasscreen.MainCanvasScreen
import com.example.canvas.ui.theme.CanvasTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.ByteArray

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
//    private val noteScreenViewModel: NoteScreenViewModel by viewModels()
//    private val noteEditScreenViewModel: NoteEditScreenViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CanvasTheme {
                MainScreen()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "MainCanvasScreen") {
        composable("MainCanvasScreen") {
            MainCanvasScreen(navController, mainCanvasViewModel = hiltViewModel())
        }
        composable(
            route = "CanvasEditScreen/{id}/{title}/{content}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument("title") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("content") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val content = backStackEntry.arguments?.getString("content") ?: ""

            CanvasEditScreen(
                navController = navController,
                canvasEditViewModel = hiltViewModel(),
                id = id,
                titleDefault = title,
                contentDefault = content
            )
        }
        composable("CanvasEditScreen") {
            CanvasEditScreen(
                navController = navController,
                canvasEditViewModel = hiltViewModel(),
                id = -1,
                titleDefault = "",
                contentDefault = ""
            )
        }
    }
}