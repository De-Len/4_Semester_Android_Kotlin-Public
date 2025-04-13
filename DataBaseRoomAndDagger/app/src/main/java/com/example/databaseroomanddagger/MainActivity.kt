package com.example.databaseroomanddagger

import NoteEditScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.databaseroomanddagger.ui.mainscreen.NoteScreen
import com.example.databaseroomanddagger.ui.theme.DataBaseRoomAndDaggerTheme
import com.example.databaseroomanddagger.viewmodel.NoteEditScreenViewModel
import com.example.databaseroomanddagger.viewmodel.NoteScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
//    private val noteScreenViewModel: NoteScreenViewModel by viewModels()
//    private val noteEditScreenViewModel: NoteEditScreenViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DataBaseRoomAndDaggerTheme {
                MainScreen()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "NoteScreen") {
        composable("NoteScreen") {
            NoteScreen(navController, noteScreenViewModel = hiltViewModel())
        }
        composable(
            route = "NoteEditScreen/{id}/{title}/{content}",
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

            NoteEditScreen(
                navController = navController,
                noteEditScreenViewModel = hiltViewModel(),
                id = id,
                titleDefault = title,
                contentDefault = content
            )
        }
        composable("NoteEditScreen") {
            NoteEditScreen(
                navController = navController,
                noteEditScreenViewModel = hiltViewModel(),
                id = -1,
                titleDefault = "",
                contentDefault = ""
            )
        }
    }
}