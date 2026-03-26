package com.example.cocky_camel_room404_fe

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.NetworkCell
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

data class FakeApp(
    val name: String,
    val icon: ImageVector,
    val color: Color,
    val isFunctional: Boolean = false,
    val requiresPin: Boolean = false
)


@Composable
fun FakeOSScreen(
    onAppOpened: (String) -> Unit
) {
    val context = LocalContext.current

    var isGlitching by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(Random.nextLong(5000, 15000))

            isGlitching = true

            delay(Random.nextLong(300, 800))

            isGlitching = false
        }
    }

    val desktopApps = listOf(
        FakeApp("Galería", Icons.Filled.PhotoAlbum, Color(0xFF9C27B0), true),
        FakeApp("Correo", Icons.Filled.Email, Color(0xFFD32F2F), true, true),
        FakeApp("Notas", Icons.Filled.Edit, Color(0xFFFFC107), true),
        FakeApp("Ajustes", Icons.Filled.Settings, Color(0xFF607D8B)),
        FakeApp("Calculadora", Icons.Filled.Calculate, Color(0xFF455A64), true),
        FakeApp("Calendario", Icons.Filled.DateRange, Color(0xFF03A9F4), true),
        FakeApp("Reloj", Icons.Filled.AccessTime, Color(0xFF00BCD4), true),
        FakeApp("Sudoku", Icons.Filled.GridOn, Color(0xFF8BC34A), true),
        FakeApp("Música", Icons.Filled.MusicNote, Color(0xFFE91E63), true),
        FakeApp("Internet", Icons.Filled.Public, Color(0xFF2196F3), true),
        FakeApp("Play Store", Icons.Filled.PlayArrow, Color(0xFF4CAF50)),
        FakeApp("Tiempo", Icons.Filled.WbSunny, Color(0xFFFFEB3B), true),
        FakeApp("Archivos", Icons.Filled.Folder, Color(0xFFFF9800), true),
        FakeApp("Maps", Icons.Filled.LocationOn, Color(0xFF4CAF50), true),
        FakeApp("System Update", Icons.Filled.Warning, Color(0xFFFF0000), true, true)
    )

    val dockApps = listOf(
        FakeApp("Teléfono", Icons.Filled.Phone, Color(0xFF4CAF50), true),
        FakeApp("Mensajes", Icons.AutoMirrored.Filled.Message, Color(0xFF2196F3), true),
        FakeApp("Cámara", Icons.Filled.CameraAlt, Color(0xFF333333), true)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .glitchEffect(isGlitching)
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_fakeos),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            FakeStatusBar(modifier = Modifier.padding(top = 12.dp))

            Spacer(modifier = Modifier.height(24.dp))

            FakeGoogleSearchBar()

            Spacer(modifier = Modifier.height(24.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(desktopApps) { app ->
                    AppIcon(app = app) {
                        if (app.isFunctional) {
                            onAppOpened(app.name)
                        } else {
                            Toast.makeText(context, "${app.name} no responde...", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                dockApps.forEach { app ->
                    AppIcon(app = app, showLabel = false) {
                        if (app.isFunctional) {
                            onAppOpened(app.name)
                        } else {
                            Toast.makeText(context, "Error al abrir ${app.name}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp, top = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(108.dp)
                        .height(4.dp)
                        .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(2.dp))
                )
            }
        }
    }
}

@Composable
fun FakeGoogleSearchBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(48.dp)
            .shadow(4.dp, RoundedCornerShape(24.dp))
            .background(Color(0xFF2A2A2A).copy(alpha = 0.95f))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Search, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Buscar...",
            color = Color.LightGray,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Icon(Icons.Filled.Mic, contentDescription = null, tint = Color.LightGray)
    }
}

@Composable
fun FakeStatusBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "04:04",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            style = TextStyle(shadow = Shadow(color = Color.Black, blurRadius = 4f))
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Wifi, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            Icon(Icons.Filled.NetworkCell, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            Icon(Icons.Filled.BatteryFull, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun AppIcon(app: FakeApp, showLabel: Boolean = true, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .shadow(6.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(app.color),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = app.icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
        }

        if (showLabel) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = app.name,
                color = Color.White,
                fontSize = 12.sp,
                maxLines = 2,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.8f),
                        offset = Offset(0f, 2f),
                        blurRadius = 4f
                    )
                )
            )
        }
    }
}