package com.example.databaseroomanddagger.ui.mainscreen

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.example.lab6_images.data.ImageEntity
import com.example.lab6_images.ui.mainscreen.ListItem
import com.example.lab6_images.viewmodel.ImageScreenViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
//@Preview
fun ImageScreen(navController: NavHostController, imageScreenViewModel: ImageScreenViewModel) {

    val allImagesState = imageScreenViewModel.allImages.observeAsState(emptyList())
    val allImages: List<ImageEntity> = allImagesState.value

    var searchQuery by remember { mutableStateOf("") }
    val filteredImages = allImages.filter { Image ->
        Image.name.contains(searchQuery, ignoreCase = true) || Image.uri.contains(searchQuery, ignoreCase = true)
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            TopAppBar(
                title = { Text("Галерея") },
//                actions = {
//                    IconButton(onClick = { /* Действие при нажатии */ }) {
//                        Icon(Icons.Default.MoreVert, contentDescription = "Поиск")
//                    }
//                },
            )
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text(text = "Поиск по названию") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Поиск"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent, // Убираем нижнее подчеркивание при потере фокуса
                    focusedIndicatorColor = Color.Transparent // Убираем нижнее подчеркивание при потере фокуса
                ),
                shape = RoundedCornerShape(15.dp),
            )


            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(filteredImages) { Image ->
                    ListItem(
                        uri = Image.uri.toUri(),
                        name = Image.name,
                        onOpen = {
                            val safeDescription = if (Image.description.isEmpty()) "Описание" else Uri.encode(Image.description)
                            navController.navigate("ImageEditScreen/${Image.id}/${Uri.encode(Image.name)}/${Uri.encode(Image.uri)}/${Uri.encode(safeDescription)}")
                        },
                        onDelete = {
                            imageScreenViewModel.deleteById(Image.id)
                        }
                    )
                }
            }

            Spacer(modifier =  Modifier.height(5.dp))
        }
    }
}
