package com.example.canvas.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.canvas.data.database.CanvasDao
import com.example.canvas.data.database.CanvasEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CanvasEditViewModel @Inject constructor(
    private val canvasDao: CanvasDao
) : ViewModel() {

    private val _state = MutableStateFlow(DrawingState())
    val state = _state.asStateFlow()

    fun onAction(action: DrawingAction) {
        when (action) {
            DrawingAction.OnClearCanvasClick -> onClearCanvasClick()
            is DrawingAction.OnDraw -> onDraw(action.offset)
            DrawingAction.OnNewPathStart -> onNewPathStart()
            DrawingAction.OnPathEnd -> onPathEnd()
            is DrawingAction.OnSelectColor -> onSelectorColor(action.color)
            is DrawingAction.OnSelectThickness -> onSelectThickness(action.thickness)
        }
    }

    private fun onSelectThickness(thickness: Float) {
        _state.update { it.copy(selectedThickness = thickness) }
    }

    private fun onSelectorColor(color: Color) {
        _state.update { it.copy(
            selectedColor = color
        ) }
    }

    private fun onPathEnd() {
        val currentPathData = state.value.currentPath ?: return
        _state.update { it.copy(
            currentPath = null,
            paths = it.paths + currentPathData
        ) }    }

    private fun onNewPathStart() {
        _state.update { it.copy(
            currentPath = PathData(
                id = System.currentTimeMillis().toString(),
                color = it.selectedColor,
                paths = emptyList(),
                thickness = it.selectedThickness,
            )
        ) }
    }

    private fun onDraw(offset: Offset) {
        val currentPathData = state.value.currentPath ?: return
        _state.update { it.copy(
            currentPath = currentPathData.copy(
                paths = currentPathData.paths + offset
            )
        ) }
    }

    private fun onClearCanvasClick() {
        _state.update { it.copy(
            currentPath = null,
            paths = emptyList()
        ) }
    }

    fun insert(title: String, content: String) {
        val note = CanvasEntity(title = title, content = content)
        viewModelScope.launch {
            canvasDao.insert(note)
        }
    }

    fun updateById(id: Int?, title: String, content: String) {
        viewModelScope.launch {
            canvasDao.updateById(id, title, content)
        }
    }
}


data class DrawingState(
    val selectedColor: Color = Color.Black,
    val selectedThickness: Float = 10f,
    val currentPath: PathData? = null,
    val paths: List<PathData> = emptyList()
)

val allColor = listOf(
    Color.Black,
    Color.Red,
    Color.Blue,
    Color.Green,
    Color.Yellow,
    Color.Magenta,
    Color.Cyan,
    )

val allThickness = listOf(
    10f,
    20f,
    30f
)

data class PathData(
    val id: String,
    val color: Color,
    val paths: List<Offset>,
    val thickness: Float
)

sealed interface DrawingAction {
    data object OnNewPathStart: DrawingAction
    data class OnDraw(val offset: Offset): DrawingAction
    data object OnPathEnd: DrawingAction
    data class OnSelectColor(val color: Color): DrawingAction
    data object OnClearCanvasClick: DrawingAction
    data class OnSelectThickness(val thickness: Float): DrawingAction

}
