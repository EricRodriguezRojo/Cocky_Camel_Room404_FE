package com.example.cocky_camel_room404_fe

import android.media.MediaPlayer
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicScreen(onBack: () -> Unit) {
    val contexto = LocalContext.current

    val reproductor = remember {
        val mp = MediaPlayer.create(contexto, R.raw.cancion_404)
        mp.isLooping = true
        mp
    }

    var reproduciendo by remember { mutableStateOf(false) }
    var valorSlider by remember { mutableFloatStateOf(0f) }
    var arrastrando by remember { mutableStateOf(false) }
    val duracionTotal = reproductor.duration.toFloat()

    val rotacion = remember { Animatable(0f) }

    LaunchedEffect(reproduciendo) {
        while (reproduciendo && isActive) {
            rotacion.animateTo(
                targetValue = rotacion.value + 360f,
                animationSpec = tween(durationMillis = 4000, easing = LinearEasing)
            )
        }
    }

    LaunchedEffect(reproduciendo, arrastrando) {
        while (reproduciendo && !arrastrando && isActive) {
            valorSlider = reproductor.currentPosition.toFloat()
            delay(500L)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (reproductor.isPlaying) {
                reproductor.pause()
            }
            reproductor.release()
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
                        text = "REPRODUCIENDO AHORA",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
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
                    Text("Artista Desconocido", color = Color(0xFFAAAAAA), fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Slider(
                    value = valorSlider,
                    onValueChange = { nuevoValor ->
                        arrastrando = true
                        valorSlider = nuevoValor
                    },
                    onValueChangeFinished = {
                        reproductor.seekTo(valorSlider.toInt())
                        arrastrando = false
                    },
                    valueRange = 0f..duracionTotal,
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
                    Text(formatoTiempo(valorSlider.toLong()), color = Color(0xFFAAAAAA), fontSize = 12.sp)
                    Text(formatoTiempo(duracionTotal.toLong()), color = Color(0xFFAAAAAA), fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        var nuevaPos = reproductor.currentPosition - 10000
                        if (nuevaPos < 0) {
                            nuevaPos = 0
                        }
                        reproductor.seekTo(nuevaPos)
                        valorSlider = nuevaPos.toFloat()
                    }) {
                        Icon(Icons.Default.FastRewind, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                    }

                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable {
                                if (reproduciendo) {
                                    reproductor.pause()
                                } else {
                                    reproductor.start()
                                }
                                reproduciendo = !reproduciendo
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        var icono = Icons.Default.PlayArrow
                        if (reproduciendo) {
                            icono = Icons.Default.Pause
                        }

                        Icon(
                            imageVector = icono,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    IconButton(onClick = {
                        var nuevaPos = reproductor.currentPosition + 10000
                        if (nuevaPos > reproductor.duration) {
                            nuevaPos = reproductor.duration
                        }
                        reproductor.seekTo(nuevaPos)
                        valorSlider = nuevaPos.toFloat()
                    }) {
                        Icon(Icons.Default.FastForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                    }
                }
            }
        }
    }
}

fun formatoTiempo(milisegundos: Long): String {
    val segundosTotales = milisegundos / 1000
    val minutos = segundosTotales / 60
    val segundos = segundosTotales % 60
    return java.lang.String.format("%d:%02d", minutos, segundos)
}