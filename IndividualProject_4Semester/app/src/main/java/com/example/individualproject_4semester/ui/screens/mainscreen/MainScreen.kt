package com.example.individualproject_4semester.ui.screens.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.individualproject_4semester.R
import com.example.individualproject_4semester.data.model.EpisodeItem
import com.example.individualproject_4semester.ui.extensions.ScreenHeader
import com.example.individualproject_4semester.ui.theme.IndividualProject_4SemesterTheme
import androidx.navigation.NavController


// https://developer.android.com/develop/ui/compose/lists?hl=en - работа с LazyVerticalGrid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {

    val episodeList = remember {
        mutableStateListOf(
            EpisodeItem("Чат-боты", R.drawable.chat_bots, { navController.navigate("chat_bots_screen") }),
            EpisodeItem("Генерация изображений", R.drawable.gen_images, { navController.navigate("image_generations_screen") }),
            EpisodeItem("Распознание речи", R.drawable.speech_recognition, { navController.navigate("speech_recognition_screen") }),
            EpisodeItem("Синтез речи", R.drawable.speech_synthesis, { navController.navigate("speech_synthesis_screen") })
            )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        ScreenHeader(
            R.drawable.header_background,
            "Нейросети",
            "Мы интегрируем и регулярно пополняем наше API передовыми нейронными моделями. У нас есть решения для самых разных задач."
        )


        LazyVerticalGrid(
            columns = GridCells.Adaptive(220.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            items(episodeList.size) { index ->
                EpisodeListItem(
                    episodeList[index].text,
                    episodeList[index].imageRes,
                    episodeList[index].onClick
                )
            }
        }

    }

}