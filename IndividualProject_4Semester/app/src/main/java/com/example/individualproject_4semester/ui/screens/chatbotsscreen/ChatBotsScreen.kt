package com.example.individualproject_4semester.ui.screens.chatbotsscreen

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.individualproject_4semester.R
import com.example.individualproject_4semester.ui.extensions.ScreenHeader
import com.example.individualproject_4semester.ui.extensions.SelectableButtonRow

@Composable
fun ChatBotsScreen(chatBotsScreenViewModel: ChatBotsScreenViewModel = hiltViewModel(), navController: NavController) {
    val isLoading = chatBotsScreenViewModel.isLoading

    var inputText by remember { mutableStateOf("") }
    var selectedModel by remember { mutableStateOf("gpt-4o-mini") }

    val responseText = chatBotsScreenViewModel.responseText

    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val animatedColor by if (isLoading) {
        infiniteTransition.animateColor(
            initialValue = Color(0xFFFFFDE21),
            targetValue = Color(0xAA1E90FF),
            animationSpec = infiniteRepeatable(
                animation = tween(2500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "color"
        )
    } else {
        remember { mutableStateOf(Color(0xFFFFFDE21)) }
    }

    Column {
        ScreenHeader(R.drawable.chat_bots, "Чат-боты", navController = navController)

        SelectableButtonRow(
            listOf(
                "gpt-4o-mini",
                "gpt-4o",
                "gpt-4.1-mini",
                "gpt-4.1",
                "gpt-4.1-nano"
            ),
            onModelSelected = { model ->
                selectedModel = model
            }
        )

        Text(
            modifier = Modifier.padding(16.dp),
            text = "Ввод:"
        )

        TextField(
            value = inputText,
            onValueChange = { inputText = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
        )

        Button(
            onClick = { chatBotsScreenViewModel.sendMessage(selectedModel, inputText) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .drawBehind() {
                    drawRoundRect(animatedColor, cornerRadius = CornerRadius(32.dp.toPx(), 32.dp.toPx()))
                },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0x00),
                contentColor = Color.Black
            )
        ) {

            Text("Сгенерировать")
        }

        Text(
            modifier = Modifier.padding(16.dp),
            text = "Вывод:"
        )

        Text(
            text = responseText,
            modifier = Modifier.padding(16.dp),
        )

    }
}