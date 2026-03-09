package com.example.cocky_camel_room404_fe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InternetScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Fake Browser Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF1F3F4))
                .padding(top = 16.dp, bottom = 8.dp, start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Fake URL Bar
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "https://www.google.com",
                    color = Color.DarkGray,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            IconButton(onClick = {  }) {
                Icon(Icons.Filled.Refresh, contentDescription = "Refresh", tint = Color.Black)
            }
        }

        // Error Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.WifiOff,
                contentDescription = "No Internet",
                tint = Color.Gray,
                modifier = Modifier.size(72.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "No internet connection",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF202124)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Try:\n• Checking the network cables, modem, and router\n• Reconnecting to Wi-Fi",
                fontSize = 15.sp,
                color = Color(0xFF5F6368),
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "ERR_INTERNET_DISCONNECTED",
                fontSize = 12.sp,
                color = Color(0xFF5F6368),
                fontWeight = FontWeight.Medium
            )
        }
    }
}