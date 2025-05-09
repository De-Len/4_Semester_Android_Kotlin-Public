package com.example.lab6_images.ui.editscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.lab6_images.viewmodel.ImageEditScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageEditScreen(
    navController: NavHostController,
    imageEditScreenViewModel: ImageEditScreenViewModel,
    id: Int? = -1,
    nameDefault: String = "",
    uriStringDefault: String = "",
    descriptionDefault: String = ""
) {
    val nameState = remember { mutableStateOf(nameDefault) }
    val uriStringState = remember { mutableStateOf(uriStringDefault) }
    val descriptionState = remember { mutableStateOf(descriptionDefault) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text(nameState.value) },
            actions = {
                imageEditScreenViewModel.updateById(id, nameState.value, uriStringState.value, descriptionState.value)
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(Icons.Filled.Check, contentDescription = "Сохранить")
                }
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.Close, contentDescription = "Закрыть")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        AsyncImage(
            model = uriStringDefault.toUri(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
        )
//

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            shape = RoundedCornerShape(15.dp),
            value = if (descriptionState.value == "Описание") "" else descriptionState.value,
            onValueChange = { descriptionState.value = it },
            label = { Text("Текст описания") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            maxLines = Int.MAX_VALUE,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )
    }
}
