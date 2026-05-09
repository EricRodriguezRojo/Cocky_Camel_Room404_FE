package com.example.cocky_camel_room404_fe.ui.theme

import android.media.MediaPlayer
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cocky_camel_room404_fe.R
import com.example.cocky_camel_room404_fe.SessionManager
import com.example.cocky_camel_room404_fe.SystemIntroViewModel
import com.example.cocky_camel_room404_fe.TerminalLine

@Composable
fun SystemIntroScreen(
    onFinished: () -> Unit,
    viewModel: SystemIntroViewModel = viewModel()
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()

    val anonLabel = stringResource(R.string.intro_anon)
    val nickname = SessionManager.getNickname(context) ?: anonLabel

    val lines = remember {
        listOf(
            TerminalLine(context.getString(R.string.intro_welcome, nickname), Color.Yellow),
            TerminalLine(context.getString(R.string.intro_booting), Color.Green),
            TerminalLine(context.getString(R.string.intro_error_boot), Color.Red),
            TerminalLine(context.getString(R.string.intro_malware), Color.Red),
            TerminalLine(context.getString(R.string.intro_attempt_update), Color.Green),
            TerminalLine(context.getString(R.string.intro_access_denied), Color.Red),
            TerminalLine(context.getString(R.string.intro_intercepting), Color.Yellow),
            TerminalLine(context.getString(R.string.intro_hacker_msg), Color.White),
            TerminalLine(context.getString(R.string.intro_objective), Color.White)
        )
    }

    LaunchedEffect(Unit) {
        viewModel.startSequence(lines) {
            SessionManager.setIntroSeen(context, true)
            onFinished()
        }
    }

    LaunchedEffect(viewModel.visibleLines.size, viewModel.currentLineText) {
        if (viewModel.visibleLines.isNotEmpty() || viewModel.currentLineText.isNotEmpty()) {
            listState.animateScrollToItem((viewModel.visibleLines.size).coerceAtLeast(0))
        }
    }

    DisposableEffect(Unit) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.glitch).apply {
            isLooping = true
            start()
        }
        onDispose {
            if (mediaPlayer.isPlaying) mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
            .clickable(enabled = viewModel.sequenceFinished) { onFinished() }
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(viewModel.visibleLines) { line ->
                TerminalText(line.text, line.color)
            }

            if (viewModel.currentIndex < lines.size) {
                item {
                    Row {
                        TerminalText(viewModel.currentLineText, lines[viewModel.currentIndex].color)
                        if (viewModel.isCursorVisible) {
                            TerminalText("█", lines[viewModel.currentIndex].color)
                        }
                    }
                }
            } else if (viewModel.isCursorVisible) {
                item { TerminalText("█", Color.White) }
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