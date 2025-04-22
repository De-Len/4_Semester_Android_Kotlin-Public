package com.example.canvas.ui.maincanvasscreen

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
import androidx.navigation.NavHostController
import com.example.canvas.data.database.CanvasEntity
import com.example.canvas.viewmodel.MainCanvasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//@Preview
fun MainCanvasScreen(navController: NavHostController, mainCanvasViewModel: MainCanvasViewModel) {

    val allCanvasesState = mainCanvasViewModel.allCanvases.observeAsState(emptyList())
    val allCanvases: List<CanvasEntity> = allCanvasesState.value

    var searchQuery by remember { mutableStateOf("") }
    val filteredCanvases = allCanvases.filter { canvas ->
        canvas.title.contains(searchQuery, ignoreCase = true)
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
                title = { Text("Холсты") },
                actions = {
                    IconButton(onClick = { /* Действие при нажатии */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Поиск")
                    }
                },
            )
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text(text = "Поиск") },
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


            Spacer(modifier =  Modifier.height(5.dp))
            Text(
                text = "Холсты:",
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(filteredCanvases) { canvas ->
                    ListItem(
                        title = canvas.title,
                        onOpen = {
                            navController.navigate("CanvasEditScreen/${canvas.id}/${Uri.encode(canvas.title)}/${Uri.encode(canvas.content)}")
                        },
                        onDelete = {
                            mainCanvasViewModel.deleteById(canvas.id)
                        }
                    )
                }
            }

            Spacer(modifier =  Modifier.height(5.dp))
        }
        BottomAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            containerColor = Color.White,

            ) {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { navController.navigate("CanvasEditScreen") }) {
                Icon(Icons.Filled.Add, contentDescription = "Добавить")
            }

        }
    }
}
