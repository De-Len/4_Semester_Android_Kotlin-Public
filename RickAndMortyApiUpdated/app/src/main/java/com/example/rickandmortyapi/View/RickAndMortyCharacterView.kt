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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A7C1))
                        ) {
                            Text("Обновить")
                            Icon(Icons.Default.Refresh, contentDescription = "Обновить")
                        }
                    }
                )
            },
        ) { innerPadding ->
            if (characters.isEmpty()) {
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
            CharacterItem(it)
        }
    }
}

@Composable
fun CharacterItem(character: RickAndMortyCharacter) {
    when (character.species) {
        "Human" -> HumanCharacter(character)
        "Alien" -> AlienCharacter(character)
        else -> OtherCharacter(character)
    }
}

@Composable
fun HumanCharacter(character: RickAndMortyCharacter) {
    CharacterCard(
        character = character,
        backgroundColor = Color(0xfff1cf),
        imageShape = CircleShape
    )
}

@Composable
fun AlienCharacter(character: RickAndMortyCharacter) {
    CharacterCard(
        character = character,
        backgroundColor = Color(0xff6bf0),
        imageShape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun OtherCharacter(character: RickAndMortyCharacter) {
    CharacterCard(
        character = character,
        backgroundColor = Color(Color.Cyan.value),
        imageShape = RoundedCornerShape(8.dp)
    )
}

@Composable
fun CharacterCard(character: RickAndMortyCharacter, backgroundColor: Color, imageShape: Shape) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .shadow(6.dp, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
//        elevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor.copy(alpha = 0.6f))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(imageShape)
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