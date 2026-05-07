package com.example.cocky_camel_room404_fe

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import kotlinx.coroutines.delay
import kotlin.random.Random

fun Modifier.glitchEffect(isGlitching: Boolean): Modifier = composed {
    if (!isGlitching) return@composed this

    var shiftX by remember { mutableFloatStateOf(0f) }
    var showRedFilter by remember { mutableStateOf(false) }
    var noiseLines by remember { mutableStateOf(emptyList<Float>()) }

    LaunchedEffect(isGlitching) {
        while (isGlitching) {
            shiftX = Random.nextInt(-30, 30).toFloat()

            showRedFilter = Random.nextBoolean()

            val lines = mutableListOf<Float>()
            for (i in 0..Random.nextInt(5, 25)) {
                lines.add(Random.nextFloat())
            }
            noiseLines = lines

            delay(Random.nextLong(30, 90))
        }
    }

    this.drawWithContent {
        translate(left = shiftX) {
            this@drawWithContent.drawContent()
        }

        if (showRedFilter) {
            drawRect(
                color = Color(0xFF990000).copy(alpha = 0.4f),
                size = size
            )
        }


        noiseLines.forEach { percentY ->
            val yPos = size.height * percentY
            val lineHeight = Random.nextFloat() * 15f + 2f

            val color = listOf(
                Color.Red,
                Color.Black,
                Color.White,
                Color.DarkGray,
                Color.Transparent
            ).random()

            if (color != Color.Transparent) {
                drawRect(
                    color = color.copy(alpha = 0.8f),
                    topLeft = Offset(0f, yPos),
                    size = Size(size.width, lineHeight)
                )
            }
        }
    }
}