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
import androidx.navigation.NavHostController
import com.example.databaseroomanddagger.data.NoteEntity
import com.example.databaseroomanddagger.viewmodel.NoteScreenViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
//@Preview
fun NoteScreen(navController: NavHostController, noteScreenViewModel: NoteScreenViewModel) {

    val allNotesState = noteScreenViewModel.allNotes.observeAsState(emptyList())
    val allNotes: List<NoteEntity> = allNotesState.value

    var searchQuery by remember { mutableStateOf("") }
    val filteredNotes = allNotes.filter { note ->
        note.title.contains(searchQuery, ignoreCase = true) || note.content.contains(searchQuery, ignoreCase = true)
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
                title = { Text("Заметки") },
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
                text = "Заметки:",
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(filteredNotes) { note ->
                    ListItem(
                        title = note.title,
                        onOpen = {
                            navController.navigate("NoteEditScreen/${note.id}/${Uri.encode(note.title)}/${Uri.encode(note.content)}")
                        },
                        onDelete = {
                            noteScreenViewModel.deleteById(note.id)
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
            IconButton(onClick = { navController.navigate("NoteEditScreen") }) {
                Icon(Icons.Filled.Add, contentDescription = "Добавить")
            }

        }
    }
}
