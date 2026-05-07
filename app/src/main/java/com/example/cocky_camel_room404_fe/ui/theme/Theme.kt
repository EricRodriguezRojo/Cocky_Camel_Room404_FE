package com.example.cocky_camel_room404_fe.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// ui.theme/Theme.kt
private val DarkColorScheme = darkColorScheme(
    primary = OrangeCyber,
    onPrimary = Color.Black,
    background = DarkBackground,
    surface = SurfaceGray,
    onSurface = Color.White
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