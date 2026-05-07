package com.example.cocky_camel_room404_fe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(onBack: () -> Unit) {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF1A237E), Color(0xFF121212))
    )

    Column(
        modifier = Modifier.fillMaxSize().background(gradient)
    ) {
        TopAppBar(
            title = { Text(stringResource(R.string.weather_title), color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Terrassa", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)

            Text(
                text = stringResource(R.string.weather_corrupted_sky),
                color = Color.LightGray,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red, modifier = Modifier.size(120.dp))

            Text("40.4°", color = Color.White, fontSize = 80.sp, fontWeight = FontWeight.Thin)

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.weather_forecast_error).uppercase(),
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.Gray)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(stringResource(R.string.weather_tomorrow), color = Color.White)
                        Icon(Icons.Default.WbSunny, contentDescription = null, tint = Color.Yellow)
                        Text("ERR°", color = Color.White)
                    }
                }
            }
        }
    }
}