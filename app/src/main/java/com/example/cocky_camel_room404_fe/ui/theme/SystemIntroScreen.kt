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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay

data class TerminalLine(val text: String, val color: Color)

@Composable
fun SystemIntroScreen(onFinished: () -> Unit) {
    val context = LocalContext.current

    val anonLabel = stringResource(R.string.intro_anon)
    val nickname = SessionManager.getNickname(context) ?: anonLabel

    val lines = listOf(
        TerminalLine(stringResource(R.string.intro_welcome, nickname), Color.Yellow),
        TerminalLine(stringResource(R.string.intro_booting), Color.Green),
        TerminalLine(stringResource(R.string.intro_error_boot), Color.Red),
        TerminalLine(stringResource(R.string.intro_malware), Color.Red),
        TerminalLine(stringResource(R.string.intro_attempt_update), Color.Green),
        TerminalLine(stringResource(R.string.intro_access_denied), Color.Red),
        TerminalLine(stringResource(R.string.intro_intercepting), Color.Yellow),
        TerminalLine(stringResource(R.string.intro_hacker_msg), Color.White),
        TerminalLine(stringResource(R.string.intro_objective), Color.White)
    )

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