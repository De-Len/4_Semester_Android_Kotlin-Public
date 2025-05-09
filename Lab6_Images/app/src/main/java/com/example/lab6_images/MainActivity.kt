package com.example.lab6_images

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.databaseroomanddagger.ui.mainscreen.ImageScreen
import com.example.lab6_images.data.ImageInfoList
import com.example.lab6_images.ui.editscreen.ImageEditScreen
import com.example.lab6_images.ui.theme.Lab6_ImagesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ImageInfoList.init(contentResolver)

        setContent {
            Lab6_ImagesTheme {
                MainScreen()
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_MEDIA_IMAGES
                )
                != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                    1001
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "ImageScreen") {
        composable("ImageScreen") {
            ImageScreen(navController, imageScreenViewModel = hiltViewModel())
        }
        composable(
            route = "ImageEditScreen/{id}/{name}/{uri}/{description}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument("name") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("uri") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("description") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val uri = backStackEntry.arguments?.getString("uri") ?: ""
            val description = backStackEntry.arguments?.getString("description") ?: ""

            ImageEditScreen(
                navController = navController,
                imageEditScreenViewModel = hiltViewModel(),
                id = id,
                nameDefault = name,
                uriStringDefault = uri,
                descriptionDefault = description
            )
        }
        composable("ImageEditScreen") {
            ImageEditScreen(
                navController = navController,
                imageEditScreenViewModel = hiltViewModel(),
                id = -1,
                nameDefault = "",
                uriStringDefault = "",
                descriptionDefault = ""
            )
        }
    }
}


