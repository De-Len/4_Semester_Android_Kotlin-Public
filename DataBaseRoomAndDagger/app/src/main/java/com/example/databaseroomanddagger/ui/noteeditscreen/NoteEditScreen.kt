import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TextField
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.databaseroomanddagger.viewmodel.NoteEditScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditScreen(
    navController: NavHostController,
    noteEditScreenViewModel: NoteEditScreenViewModel,
    id: Int? = -1,
    titleDefault: String = "",
    contentDefault: String = "",
) {
    val titleState = remember { mutableStateOf(titleDefault) }
    val contentState = remember { mutableStateOf(contentDefault) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text("Редактирование") },
            actions = {
                IconButton(onClick = {
                    if (id == -1) {
                        noteEditScreenViewModel.insert(titleState.value, contentState.value)
                    }
                    else {
                        noteEditScreenViewModel.updateById(id, titleState.value, contentState.value)
                    }
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

        TextField(
            shape = RoundedCornerShape(15.dp),
            value = titleState.value,
            onValueChange = { titleState.value = it },
            label = { Text("Название") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )

        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            shape = RoundedCornerShape(15.dp),
            value = contentState.value,
            onValueChange = { contentState.value = it },
            label = { Text("Текст заметки") },
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
