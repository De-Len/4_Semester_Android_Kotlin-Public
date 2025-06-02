package com.example.individualproject_4semester.ui.screens.imagegenerationscreen

import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.individualproject_4semester.R
import com.example.individualproject_4semester.data.helpers.saveImageToGallery
import com.example.individualproject_4semester.ui.extensions.ScreenHeader
import com.example.individualproject_4semester.ui.extensions.SelectableButtonRow
import com.example.individualproject_4semester.ui.extensions.SettingsDropdown
import kotlinx.coroutines.launch

// https://developer.android.com/develop/ui/compose/animation/quick-guide?hl=en#repeat-animation

@Composable
fun ImageGenerationScreen(imageGenerationScreenViewModel: ImageGenerationScreenViewModel = hiltViewModel(), navController: NavController) {
    val isLoading = imageGenerationScreenViewModel.isLoading
    val context = LocalContext.current

    var inputText by remember { mutableStateOf("") }
    var imageBitmap = imageGenerationScreenViewModel.imageBitmap
    if (imageBitmap == null) {
        imageBitmap =
            BitmapFactory.decodeResource(context.resources, R.drawable.cat).asImageBitmap()
    }

    var selectedModel by remember { mutableStateOf("dall-e-3") }
    var selectedSize by remember { mutableStateOf("1024x1024") }
    var selectedQuality by remember { mutableStateOf("low") }
    var selectedBackground by remember { mutableStateOf("opaque") }

    val scrollState = rememberScrollState()

    val coroutineScope = rememberCoroutineScope()

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

    Column(modifier = Modifier.verticalScroll(scrollState)) {
        ScreenHeader(R.drawable.gen_images, "Генерация изображений", navController = navController)

        SelectableButtonRow(
            listOf(
                "dall-e-3",
                "gpt-image-1",
            ),
            isArrowIconActivated = false,
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
        if (selectedModel == "gpt-image-1") {
            selectedSize = "1024x1024"
            selectedQuality = "low"
            selectedBackground = "opaque"

            SettingsDropdown(
                label = "Размер",
                options = listOf("1024x1024", "1024x1536", "1536x1024"),
                onSelect = { selected -> selectedSize = selected })
            SettingsDropdown(
                label = "Качество",
                options = listOf("low", "medium", "high"),
                onSelect = { selected -> selectedQuality = selected })
            SettingsDropdown(
                label = "Фон",
                options = listOf("opaque", "transparent"),
                onSelect = { selected -> selectedBackground = selected })

            Button(
                onClick = {
                    imageGenerationScreenViewModel.generateImage(
                        model = selectedModel,
                        input = inputText,
                        size = selectedSize,
                        quality = selectedQuality,
                        background = selectedBackground
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

        } else {
            selectedSize = "1024x1024"
            selectedQuality = "standard"
            SettingsDropdown(
                label = "Размер",
                options = listOf("1024x1024", "1792x1024", "1024x1792"),
                onSelect = { selected -> selectedSize = selected })
            SettingsDropdown(
                label = "Качество",
                options = listOf("standard", "hd"),
                onSelect = { selected -> selectedQuality = selected })

            Button(
                onClick = {
                    imageGenerationScreenViewModel.generateImage(
                        model = selectedModel,
                        input = inputText,
                        size = selectedSize,
                        quality = selectedQuality,
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
        }

        Row {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Вывод:"
            )

            Spacer(modifier = Modifier.weight(1f))


            IconButton(
                onClick = {
                    coroutineScope.launch {
                        val bitmapToSave = imageBitmap?.asAndroidBitmap()
                        if (bitmapToSave != null) {
                            val success = saveImageToGallery(context, bitmapToSave)
                            Toast.makeText(
                                context,
                                if (success) "Изображение сохранено" else "Ошибка при сохранении",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(context, "Изображение отсутствует", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Сохранить")
            }
        }


        Image(
            painter = BitmapPainter(imageBitmap),
            contentDescription = "Сгенерированное изображение",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}




