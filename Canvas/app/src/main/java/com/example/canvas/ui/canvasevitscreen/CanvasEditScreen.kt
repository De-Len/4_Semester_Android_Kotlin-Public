package com.example.canvas.ui.canvasevitscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.navigation.NavHostController
import com.example.canvas.data.model.base64ToBitmap
import com.example.canvas.data.model.bitmapToBase64
import com.example.canvas.data.model.combineBitmaps
import com.example.canvas.data.model.drawPathsToBitmap
import com.example.canvas.viewmodel.CanvasEditViewModel
import com.example.canvas.viewmodel.DrawingAction
import com.example.canvas.viewmodel.allColor
import com.example.canvas.viewmodel.allThickness
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.text.style.TextAlign
import com.example.canvas.data.model.saveImageToGallery


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CanvasEditScreen(
    navController: NavHostController,
    canvasEditViewModel: CanvasEditViewModel,
    id: Int? = -1,
    titleDefault: String = "",
    contentDefault: String = "",
) {
    val titleState = remember { mutableStateOf(titleDefault) }
    val contentState = remember { mutableStateOf(contentDefault) }
    val canvasSize = remember { mutableStateOf(IntSize.Zero) }
    val showCanvasImage = remember { mutableStateOf(true) }
    val imageBitmapState: MutableState<ImageBitmap?> = remember { mutableStateOf(null) }


    val onAction: (DrawingAction) -> Unit = { action -> canvasEditViewModel.onAction(action)}
    val onThicknessChange: (Float) -> Unit = { thickness ->
        canvasEditViewModel.onAction(DrawingAction.OnSelectThickness(thickness)) }

    val canvasState by canvasEditViewModel.state.collectAsState()

    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            // Конвертируем изображение в Bitmap и сохраняем его
            uri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(navController.context.contentResolver, uri)
                imageBitmapState.value = bitmap.asImageBitmap()
            }
        }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Редактирование",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Закрыть")
                }
            },
            actions = {
                IconButton(onClick = {
                    val width = canvasSize.value.width.takeIf { it > 0 } ?: 1
                    val height = canvasSize.value.height.takeIf { it > 0 } ?: 1
                    if (id == -1) {
                        canvasEditViewModel.insert(titleState.value, bitmapToBase64(drawPathsToBitmap(width, height, canvasState.paths))) //TODO
                    }
                    else {
                        val previousImage: String = contentState.value
                        if (previousImage == "") {
                            val newImage: String = bitmapToBase64(drawPathsToBitmap(width, height, canvasState.paths))
                            canvasEditViewModel.updateById(id, titleState.value, newImage) //TODO
                        }
                        else {
                            val newImage: String = bitmapToBase64(drawPathsToBitmap(width, height, canvasState.paths))
                            canvasEditViewModel.updateById(id, titleState.value, bitmapToBase64(combineBitmaps(previousImage, newImage))) //TODO
                        }

                    }
                    navController.popBackStack()
                }) {
                    Icon(Icons.Filled.Check, contentDescription = "Сохранить")
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

        Text(
            text = "Холст:",
            modifier = Modifier.padding(16.dp)
        )

        Box( // Накладываем друг на друга
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .onSizeChanged { newSize ->
                    canvasSize.value = newSize
                }
                .clip(RoundedCornerShape(16.dp))  // Обрезка изображения с закругленными углами

        ) {
            if (id != -1 && showCanvasImage.value && imageBitmapState.value == null) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawImage(
                        image = base64ToBitmap(contentState.value).asImageBitmap()
                    )
                }
            }
            else if (imageBitmapState.value != null) {
                Canvas(modifier = Modifier.fillMaxWidth()) {
                    drawImage(imageBitmapState.value!!)
                }
                contentState.value = bitmapToBase64(imageBitmapState.value!!.asAndroidBitmap())
            }

            DrawingCanvas(
                paths = canvasState.paths,
                currentPath = canvasState.currentPath,
                onAction = onAction,
                modifier = Modifier.fillMaxSize()
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            allThickness.fastForEach { thickness ->
                ThicknessPreviewButton(thickness, onThicknessChange)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {

            allColor.fastForEach { color ->
                val isSelected = canvasState.selectedColor == color
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            val scale = if(isSelected) 1.2f else 1f
                            scaleX = scale
                            scaleY = scale
                        }
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = 2.dp,
                            color = if (isSelected) {
                                Color.Black
                            } else {
                                Color.Transparent
                            },
                            shape = CircleShape
                        )
                        .clickable {
                            canvasEditViewModel.onAction(DrawingAction.OnSelectColor(color))
                        }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            Button(onClick = {
                selectImageLauncher.launch("image/*")
            }) {
                Text("Загрузить")
            }

            Button(
                onClick = {
                    canvasEditViewModel.onAction(DrawingAction.OnClearCanvasClick)
                    contentState.value = ""
                    showCanvasImage.value = false
                }
            ) {
                Text("Очистить")
            }

            Button(
                onClick = {
                    val width = canvasSize.value.width.takeIf { it > 0 } ?: 1
                    val height = canvasSize.value.height.takeIf { it > 0 } ?: 1

                    val previousImage: String = contentState.value
                    if (previousImage == "") {
                        saveImageToGallery(navController.context, drawPathsToBitmap(width, height, canvasState.paths), titleState.value) //TODO
                    }
                    else {
                        val newImage: String = bitmapToBase64(drawPathsToBitmap(width, height, canvasState.paths))
                        saveImageToGallery(navController.context, combineBitmaps(previousImage, newImage), titleState.value) //TODO
                    }

                }
            ) {
                Text("Сохранить в галерею", textAlign = TextAlign.Center)
            }
        }
    }

}

@Composable
fun ThicknessPreviewButton(
    thickness: Float,
    onClick: (Float) -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            .clickable { onClick(thickness) },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize(0.6f)) {
            val path = Path().apply {
                moveTo(size.width * 0.1f, size.height * 0.5f)
                quadraticTo(
                    size.width * 0.5f, size.height * 0.1f,
                    size.width * 0.9f, size.height * 0.5f
                )
            }

            drawPath(
                path = path,
                color = Color.Black,
                style = Stroke(width = thickness, cap = StrokeCap.Round)
            )
        }
    }
}