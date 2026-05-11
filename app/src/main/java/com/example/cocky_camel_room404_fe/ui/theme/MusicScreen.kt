package com.example.cocky_camel_room404_fe

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.isActive

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicScreen(
    onBack: () -> Unit,
    viewModel: MusicViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.initMediaPlayer(context, R.raw.cancion_404)
    }

    val rotacion = remember { Animatable(0f) }

    LaunchedEffect(viewModel.isPlaying) {
        while (viewModel.isPlaying && isActive) {
            rotacion.animateTo(
                targetValue = rotacion.value + 360f,
                animationSpec = tween(durationMillis = 4000, easing = LinearEasing)
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF2B323A), Color(0xFF121212))
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.music_now_playing),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back), tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                modifier = Modifier.padding(top = 8.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(320.dp)
                        .shadow(24.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.Black)
                        .rotate(rotacion.value),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.music_cover),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.Black)
                    )
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color.DarkGray)
                    )
                }

                Spacer(modifier = Modifier.height(64.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("404_VOID.mp3", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(stringResource(R.string.music_unknown_artist), color = Color(0xFFAAAAAA), fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Slider(
                    value = viewModel.sliderValue,
                    onValueChange = { nuevoValor ->
                        viewModel.isDragging = true
                        viewModel.sliderValue = nuevoValor
                    },
                    onValueChangeFinished = {
                        viewModel.seekTo(viewModel.sliderValue)
                        viewModel.isDragging = false
                    },
                    valueRange = 0f..viewModel.totalDuration.coerceAtLeast(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color.White,
                        inactiveTrackColor = Color(0xFF444444)
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(viewModel.formatTime(viewModel.sliderValue.toLong()), color = Color(0xFFAAAAAA), fontSize = 12.sp)
                    Text(viewModel.formatTime(viewModel.totalDuration.toLong()), color = Color(0xFFAAAAAA), fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.skipBackward() }) {
                        Icon(Icons.Default.FastRewind, contentDescription = stringResource(R.string.music_rewind), tint = Color.White, modifier = Modifier.size(36.dp))
                    }

                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { viewModel.togglePlayPause() },
                        contentAlignment = Alignment.Center
                    ) {
                        val icono = if (viewModel.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow

                        Icon(
                            imageVector = icono,
                            contentDescription = stringResource(R.string.music_play_pause),
                            tint = Color.Black,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    IconButton(onClick = { viewModel.skipForward() }) {
                        Icon(Icons.Default.FastForward, contentDescription = stringResource(R.string.music_forward), tint = Color.White, modifier = Modifier.size(36.dp))
                    }
                }
            }
        }
    }
}
