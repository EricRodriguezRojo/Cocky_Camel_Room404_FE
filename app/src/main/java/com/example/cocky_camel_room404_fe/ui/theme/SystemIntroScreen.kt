package com.example.cocky_camel_room404_fe

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay

data class TerminalLine(val text: String, val color: Color)

@Composable
fun SystemIntroScreen(onFinished: () -> Unit) {
    val context = LocalContext.current
    val nickname = SessionManager.getNickname(context) ?: "ANÓNIMO"

    val lines = remember {
        listOf(
            TerminalLine("BIENVENIDO, $nickname", Color.Yellow),
            TerminalLine("BOOTING SAFE_MODE...", Color.Green),
            TerminalLine("ERROR: BOOT SECTOR CORRUPTED.", Color.Red),
            TerminalLine("MALWARE DETECTADO: Room404_Architect.exe", Color.Red),
            TerminalLine("ATTEMPTING SYSTEM UPDATE...", Color.Green),
            TerminalLine("ACCESS DENIED. CORE APPS ENCRYPTED.", Color.Red),
            TerminalLine(">> INTERCEPTANDO SEÑAL...", Color.Yellow),
            TerminalLine(">> THE ARCHITECT: ¿Pensabas que un simple System Update te salvaría? Tu dispositivo es mío. Si quieres recuperarlo, tendrás que jugar a mi juego. Las respuestas que buscas están ocultas en tus propias aplicaciones.", Color.White),
            TerminalLine(">> OBJETIVO: Desencripta las aplicaciones ancla, encuentra el código maestro y fuerza el SYSTEM UPDATE.", Color.White)
        )
    }

    var visibleLines by remember { mutableStateOf(emptyList<TerminalLine>()) }
    var currentLineText by remember { mutableStateOf("") }
    var currentIndex by remember { mutableIntStateOf(0) }
    var isCursorVisible by remember { mutableStateOf(true) }
    var sequenceFinished by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        while (true) {
            isCursorVisible = !isCursorVisible
            delay(500)
        }
    }

    LaunchedEffect(Unit) {
        for (i in lines.indices) {
            val fullText = lines[i].text
            currentIndex = i
            currentLineText = ""
            
            for (char in fullText) {
                currentLineText += char
                delay(30)
            }
            
            visibleLines = visibleLines + lines[i].copy(text = currentLineText)
            currentLineText = ""
            delay(500)
        }
        currentIndex = lines.size
        sequenceFinished = true
        delay(1500)
        onFinished()
    }

    LaunchedEffect(visibleLines.size, currentLineText) {
        if (visibleLines.isNotEmpty() || currentLineText.isNotEmpty()) {
            listState.animateScrollToItem((visibleLines.size).coerceAtLeast(0))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
            .clickable(enabled = sequenceFinished) { onFinished() }
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(visibleLines) { line ->
                TerminalText(line.text, line.color)
            }
            
            if (currentIndex < lines.size) {
                item {
                    Row {
                        TerminalText(currentLineText, lines[currentIndex].color)
                        if (isCursorVisible) {
                            TerminalText("█", lines[currentIndex].color)
                        }
                    }
                }
            } else {
                item {
                    if (isCursorVisible) {
                        TerminalText("█", Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun TerminalText(text: String, color: Color) {
    Text(
        text = text,
        color = color,
        fontFamily = FontFamily.Monospace,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}
