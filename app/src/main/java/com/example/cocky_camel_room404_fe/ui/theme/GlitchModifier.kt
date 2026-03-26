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

// Modificador que aplica un efecto de "Hackeo / Pantalla Rota"
fun Modifier.glitchEffect(isGlitching: Boolean): Modifier = composed {
    if (!isGlitching) return@composed this

    // Variables para controlar lo visual del hackeo
    var shiftX by remember { mutableFloatStateOf(0f) }
    var showRedFilter by remember { mutableStateOf(false) }
    var noiseLines by remember { mutableStateOf(emptyList<Float>()) }

    LaunchedEffect(isGlitching) {
        while (isGlitching) {
            // Desplazamiento horizontal aleatorio
            shiftX = Random.nextInt(-30, 30).toFloat()

            // A veces sale el filtro rojo de alerta, a veces no
            showRedFilter = Random.nextBoolean()

            // Generar posiciones aleatorias para los "píxeles rotos" (bandas horizontales)
            val lines = mutableListOf<Float>()
            for (i in 0..Random.nextInt(5, 25)) {
                lines.add(Random.nextFloat()) // Posición Y en porcentaje (0.0 a 1.0)
            }
            noiseLines = lines

            // Cambia el patrón visual súper rápido
            delay(Random.nextLong(30, 90))
        }
    }

    this.drawWithContent {
        // 1. Dibuja la pantalla entera ligeramente movida hacia los lados
        translate(left = shiftX) {
            this@drawWithContent.drawContent()
        }

        // 2. Aplica el filtro rojo de "Sistema Comprometido"
        if (showRedFilter) {
            drawRect(
                color = Color(0xFF990000).copy(alpha = 0.4f), // Rojo oscuro transparente
                size = size
            )
        }

        // 3. Dibuja las bandas de ruido o "píxeles rotos"
        noiseLines.forEach { percentY ->
            val yPos = size.height * percentY
            val lineHeight = Random.nextFloat() * 15f + 2f // Grosor del píxel roto

            // Elige colores típicos de un glitch
            val color = listOf(
                Color.Red,
                Color.Black,
                Color.White,
                Color.DarkGray,
                Color.Transparent // A veces invisible para dejar huecos
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