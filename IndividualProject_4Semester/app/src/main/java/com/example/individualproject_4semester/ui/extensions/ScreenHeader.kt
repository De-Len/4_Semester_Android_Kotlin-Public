package com.example.individualproject_4semester.ui.extensions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ScreenHeader(
    imageRes: Int,
    title: String,
    subtitle: String = "",
    navController: NavController? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
//            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
    ) {

        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier.matchParentSize()
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.8f)
                    ),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                ))
        )

        if (navController != null) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(vertical = 16.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
            }
        }


        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {


            Text(text = title, color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold, lineHeight = 36.sp)
            Text(text = subtitle, color = Color.White.copy(alpha = 0.85f))
        }
    }
}
