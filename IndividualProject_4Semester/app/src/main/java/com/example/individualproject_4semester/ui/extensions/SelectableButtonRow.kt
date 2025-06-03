package com.example.individualproject_4semester.ui.extensions

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun SelectableButtonRow(
    options: List<String>,
    modifier: Modifier = Modifier,
    isArrowIconActivated: Boolean = true,
    onModelSelected: (String) -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    Box(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            items(options.size) { index ->
                val isSelected = index == selectedIndex

                Button(
                    onClick = {
                        selectedIndex = index
                        onModelSelected(options[index])
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Color(0xFFFFFDE21) else Color.LightGray,
                        contentColor = if (isSelected) Color.Black else Color.Black
                    )
                ) {
                    Text(text = options[index])
                }
            }
        }

        if (isArrowIconActivated) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Scroll left",
                tint = Color.DarkGray,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .zIndex(1f)
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Scroll right",
                tint = Color.DarkGray,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .zIndex(1f)
            )
        }

    }
}