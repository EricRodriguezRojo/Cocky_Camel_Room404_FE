package com.example.cocky_camel_room404_fe

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class FakeApp(
    val name: String,
    val icon: ImageVector,
    val color: Color,
    val isFunctional: Boolean = false,
    val hasNotification: Boolean = false
)

@Composable
fun FakeOSScreen(
    onAppOpened: (String) -> Unit
) {
    val context = LocalContext.current

    var searchText by remember { mutableStateOf("") }
    var isInternetErrorVisible by remember { mutableStateOf(false) }

    val desktopApps = listOf(
        FakeApp("Galería", Icons.Filled.PhotoAlbum, Color(0xFF9C27B0), true),
        FakeApp("Correo", Icons.Filled.Email, Color(0xFF9A0F0F), true, hasNotification = true),
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
        FakeApp("System Update", Icons.Filled.Warning, Color(0xFFFF0000), true)
    )

    val dockApps = listOf(
        FakeApp("Teléfono", Icons.Filled.Phone, Color(0xFF4CAF50), true),
        FakeApp("Mensajes", Icons.AutoMirrored.Filled.Message, Color(0xFF2196F3), true, hasNotification = true),
        FakeApp("Cámara", Icons.Filled.CameraAlt, Color(0xFF333333), true)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondo_fakeos),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxSize()) {
            FakeStatusBar(modifier = Modifier.padding(top = 12.dp))

            Spacer(modifier = Modifier.height(24.dp))

            FakeGoogleSearchBar(
                value = searchText,
                onValueChange = { searchText = it },
                onTriggerError = { isInternetErrorVisible = true }
            )

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
                        if (app.isFunctional) onAppOpened(app.name)
                        else Toast.makeText(context, "${app.name} no responde...", Toast.LENGTH_SHORT).show()
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
                        onAppOpened(app.name)
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

        if (isInternetErrorVisible) {
            InternetErrorDialog(onDismiss = { isInternetErrorVisible = false })
        }
    }
}

@Composable
fun FakeGoogleSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onTriggerError: () -> Unit
) {
    val context = LocalContext.current

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
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.clickable { onTriggerError() }
        )

        Spacer(modifier = Modifier.width(12.dp))

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
            cursorBrush = SolidColor(Color.White),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onTriggerError() }),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text("Buscar...", color = Color.Gray, fontSize = 16.sp)
                }
                innerTextField()
            }
        )

        Icon(
            imageVector = Icons.Filled.Mic,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.clickable {
                Toast.makeText(context, "Error: Búsqueda de voz no disponible.", Toast.LENGTH_SHORT).show()
            }
        )
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "04:04",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                style = TextStyle(shadow = Shadow(color = Color.Black, blurRadius = 4f))
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.SignalCellularAlt, null, tint = Color.White, modifier = Modifier.size(16.dp))
            Icon(Icons.Filled.Wifi, null, tint = Color.White, modifier = Modifier.size(16.dp))
            Icon(Icons.Filled.BatteryFull, null, tint = Color.White, modifier = Modifier.size(16.dp))
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
        Box(contentAlignment = Alignment.TopEnd) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp))
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

            if (app.hasNotification) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Color.Red, CircleShape)
                        .padding(2.dp)
                )
            }
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

@Composable
fun InternetErrorDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Filled.WifiOff, contentDescription = null, tint = Color.White) },
        title = {
            Text(
                text = "Sin Conexión",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = "Parece que no tienes acceso a internet. Revisa tu conexión de datos o Wi-Fi e inténtalo de nuevo.",
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("ACEPTAR", color = Color(0xFF03A9F4), fontWeight = FontWeight.Bold)
            }
        },
        containerColor = Color(0xFF1A1A1A),
        titleContentColor = Color.White,
        textContentColor = Color.LightGray
    )
}