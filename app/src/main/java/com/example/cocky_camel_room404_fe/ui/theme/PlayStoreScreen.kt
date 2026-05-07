package com.example.cocky_camel_room404_fe

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.outlined.SettingsSystemDaydream
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayStoreScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val playStoreGreen = Color(0xFF01875f)
    val secondaryText = Color(0xFF5f6368)
    val topBarIconColor = Color(0xFF4A4A4A)

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(Color.White)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "04:04",
                            color = topBarIconColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.SignalCellularAlt,
                            contentDescription = null,
                            tint = topBarIconColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Icon(
                            Icons.Filled.Wifi,
                            contentDescription = null,
                            tint = topBarIconColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Icon(
                            Icons.Filled.BatteryFull,
                            contentDescription = null,
                            tint = topBarIconColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.app_playstore),
                            color = Color(0xFF1F1F1F),
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                                tint = Color(0xFF1F1F1F)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.22f))

            Icon(
                imageVector = Icons.Outlined.SettingsSystemDaydream,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = Color(0xFFbcc1c6)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.playstore_no_internet_msg),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                color = secondaryText,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    Toast.makeText(context, context.getString(R.string.playstore_retry_toast), Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = playStoreGreen),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(44.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Text(stringResource(R.string.playstore_retry_button), fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.weight(0.78f))
        }
    }
}