package com.example.rickandmortyapi.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.rickandmortyapi.Model.RickAndMortyCharacter
import com.example.rickandmortyapi.ViewModel.RickAndMortyCharacterViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rickandmortyapi.R

@Composable
@Preview
@OptIn(ExperimentalMaterial3Api::class)
fun CharacterScreen(viewModel: RickAndMortyCharacterViewModel = viewModel()) {
    val characters by viewModel.characters.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(

                    title = {
                        Image(
                        painter = painterResource(R.drawable.rick_and_morty_logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(150.dp)
                        )
                            },
                    actions = {
                        Button(
                            onClick = { viewModel.fetchCharacters() } ,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A7C1)),
                            modifier = Modifier.testTag("refreshButton")
                        ) {
                            Text("Обновить")
                            Icon(Icons.Default.Refresh, contentDescription = "Обновить")
                        }
                    }
                )
            },
        ) { innerPadding ->
            if (errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .align(Alignment.Center),
                        textAlign = TextAlign.Center,
                    )
                }
            }

            else if (characters.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

            } else {
                CharacterList(characters, modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun CharacterList(
    characters: List<RickAndMortyCharacter>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(characters) {
            CharacterCard(
                character = it,
                modifier = when (it.species) {
                    "Human" -> Modifier.size(64.dp).clip(CircleShape)
                    "Alien" -> Modifier.size(64.dp).padding(8.dp)
                    else ->  Modifier.size(64.dp).fillMaxWidth()
                },
                color = when (it.species) {
                    "Human" -> Color.Gray.copy(0.25f)
                    "Alien" -> Color.Green.copy(0.25f)
                    else ->  Color.Blue.copy(0.25f)
                }
            )
        }
    }
}

@Composable
fun CharacterCard(character: RickAndMortyCharacter, modifier: Modifier, color: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .shadow(6.dp, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
//                .background(backgroundColor.copy(alpha = 0.6f))
                .background(color)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = modifier
                    .size(80.dp)
//                    .clip(imageShape)
//                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.3f))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = character.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    text = "Species: ${character.species}",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}