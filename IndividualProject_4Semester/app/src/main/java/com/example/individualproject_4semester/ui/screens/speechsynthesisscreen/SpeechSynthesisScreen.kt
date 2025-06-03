package com.example.individualproject_4semester.ui.screens.speechsynthesisscreen

import android.content.Context
import android.net.Uri
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import com.example.individualproject_4semester.R
import com.example.individualproject_4semester.data.helpers.formatMillis
import com.example.individualproject_4semester.data.helpers.rawResourceToFile
import com.example.individualproject_4semester.ui.extensions.ScreenHeader
import com.example.individualproject_4semester.ui.extensions.SelectableButtonRow
import com.example.individualproject_4semester.ui.extensions.SettingsDropdown
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun SpeechSynthesisScreen(speechSynthesisScreenViewModel: SpeechSynthesisScreenViewModel = hiltViewModel(), navController: NavController) {
    var isLoading = speechSynthesisScreenViewModel.isLoading
    val context = LocalContext.current

    var inputText by remember { mutableStateOf("") }

    var selectedModel by remember { mutableStateOf("gpt-4o-mini-tts") }

    var selectedVoice by remember { mutableStateOf("nova") }

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
        ScreenHeader(R.drawable.speech_synthesis, "Синтез речи", navController = navController)

        SelectableButtonRow(
            listOf(
                "gpt-4o-mini-tts",
                "tts-1",
                "tts-1-hd"
            ),
            onModelSelected = { model -> selectedModel = model },
            isArrowIconActivated = false
        )

        Text(
            modifier = Modifier.padding(16.dp),
            text = "Ввод:"
        )

        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
        )

        SettingsDropdown(
            label = "Голос",
            options = listOf("nova", "alloy", "echo", "fable", "onyx", "shimmer"),
            onSelect = { selected -> selectedVoice = selected })


        Button(
            onClick = {
                speechSynthesisScreenViewModel.generateVoice(
                    model = selectedModel,
                    input = inputText,
                    voice = selectedVoice
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .drawBehind() {
                    drawRoundRect(
                        animatedColor,
                        cornerRadius = CornerRadius(32.dp.toPx(), 32.dp.toPx())
                    )
                },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0x00),
                contentColor = Color.Black
            )
        ) {

            Text("Сгенерировать")
        }

        ExoAudioPlayerUI(context, speechSynthesisScreenViewModel.outputFile)

    }
}

// Метод сгенерировал Chat-GPT 4o-mini
@Composable
fun ExoAudioPlayerUI(
    context: Context,
    audioFile: File?
) {
    val defaultFile = remember {
        rawResourceToFile(context, R.raw.shoot, "shoot.wav")
    }

    val resolvedAudioFile = audioFile ?: defaultFile

    val player = remember {
        ExoPlayer.Builder(context).build()
    }

    var isPlaying by remember { mutableStateOf(false) }
    var duration by remember { mutableStateOf(0L) }
    var currentPosition by remember { mutableStateOf(0L) }

    // Загружаем новый файл, если он изменился
    LaunchedEffect(resolvedAudioFile) {
        val mediaItem = MediaItem.fromUri(Uri.fromFile(resolvedAudioFile))
        player.setMediaItem(mediaItem)
        player.prepare()
    }

    // Обновляем длительность, когда готов
    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    duration = player.duration.coerceAtLeast(0)
                }
            }
        }
        player.addListener(listener)

        onDispose {
            player.release()
        }
    }

    // Обновление прогресса
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            player.play()
            while (player.isPlaying) {
                currentPosition = player.currentPosition
                delay(500)
            }
        } else {
            player.pause()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = { isPlaying = !isPlaying },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(if (isPlaying) "⏹ Стоп" else "▶ Воспроизвести")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            value = currentPosition.toFloat(),
            onValueChange = {
                currentPosition = it.toLong()
            },
            onValueChangeFinished = {
                player.seekTo(currentPosition)
            },
            valueRange = 0f..(duration.coerceAtLeast(1).toFloat()),
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "${formatMillis(currentPosition.toInt())} / ${formatMillis(duration.toInt())}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}