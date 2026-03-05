package com.example.cocky_camel_room404_fe.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// ui.theme/Theme.kt
private val DarkColorScheme = darkColorScheme(
    primary = OrangeCyber,       // Este es el color de tus botones principales
    onPrimary = Color.Black,     // El color del texto dentro de los botones
    background = DarkBackground,  // El fondo de la app
    surface = SurfaceGray,       // El fondo de tarjetas o menús
    onSurface = Color.White      // Texto general
)

@Composable
fun Room404Theme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}