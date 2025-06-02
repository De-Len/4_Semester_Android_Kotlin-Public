package com.example.individualproject_4semester.ui.screens.speechrecognitionscreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.individualproject_4semester.R
import com.example.individualproject_4semester.data.helpers.uriToFile
import com.example.individualproject_4semester.ui.extensions.ScreenHeader
import com.example.individualproject_4semester.ui.extensions.SelectableButtonRow

@Composable
fun SpeechRecognitionScreen(speechRecognitionScreenViewModel: SpeechRecognitionScreenViewModel = hiltViewModel(), navController: NavController) {
    val isLoading = speechRecognitionScreenViewModel.isLoading
    val context = LocalContext.current
    var inputText by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            speechRecognitionScreenViewModel.audioFile = uriToFile(it, context)
        }
    }

    var selectedModel by remember { mutableStateOf("gpt-4o-mini-transcribe") }

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
    }
    else {
        remember { mutableStateOf(Color(0xFFFFFDE21)) }
    }

    Column {
        ScreenHeader(R.drawable.speech_recognition, "Распознание речи", navController = navController)

        SelectableButtonRow(
            listOf(
                "gpt-4o-mini-transcribe",
                "gpt-4o-transcribe",
                "whisper-1"),
            onModelSelected = { model -> selectedModel = model }
        )

        Button(
            onClick = { launcher.launch("audio/*") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFFDE21),
                contentColor = Color.Black
            )
        ) {
            Text("Выбрать аудиофайл")
        }

        Button(
            onClick = { speechRecognitionScreenViewModel.transcribeAudio(selectedModel) },
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

            Text("Распознать")
        }

        Text(modifier = Modifier.padding(16.dp), text = "Вывод:")

        Text(modifier = Modifier.padding(16.dp), text = speechRecognitionScreenViewModel.audioRecognizedText.toString())

    }
}