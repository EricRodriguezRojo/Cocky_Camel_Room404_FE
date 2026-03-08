package com.example.cocky_camel_room404_fe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicScreen(onBack: () -> Unit) {
    var isPlaying by remember { mutableStateOf(false) }
    var sliderPosition by remember { mutableStateOf(0.3f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        TopAppBar(
            title = { Text("Reproduciendo", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Carátula del álbum (Simulada)
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF1E1E1E)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.MusicNote, contentDescription = null, tint = Color(0xFF03A9F4), modifier = Modifier.size(100.dp))
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text("404_VOID.mp3", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Artista Desconocido", color = Color.Gray, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(32.dp))

            Slider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                colors = SliderDefaults.colors(thumbColor = Color(0xFF03A9F4), activeTrackColor = Color(0xFF03A9F4))
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("1:24", color = Color.Gray, fontSize = 12.sp)
                Text("4:04", color = Color.Red, fontSize = 12.sp) // Guiño al tiempo total
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) { Icon(Icons.Default.SkipPrevious, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp)) }

                Surface(
                    onClick = { isPlaying = !isPlaying },
                    shape = RoundedCornerShape(100.dp),
                    color = Color(0xFF03A9F4)
                ) {
                    Icon(
                        if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.padding(16.dp).size(48.dp),
                        tint = Color.White
                    )
                }

                IconButton(onClick = {}) { Icon(Icons.Default.SkipNext, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp)) }
            }
        }
    }
}