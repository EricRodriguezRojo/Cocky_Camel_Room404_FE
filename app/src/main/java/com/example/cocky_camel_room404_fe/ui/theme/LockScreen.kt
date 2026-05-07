package com.example.cocky_camel_room404_fe

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LockScreen(
    appName: String,
    correctPin: String,
    onSuccess: () -> Unit,
    onBack: () -> Unit,
    context: Context
) {
    var enteredPin by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val vibrator = remember { context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }

    val pinDotColor by animateColorAsState(
        targetValue = if (isError) Color(0xFFCF6679) else MaterialTheme.colorScheme.primary,
        label = ""
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondo_sistema),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.85f)))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.back), tint = Color.White)
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = if (appName == "System Update") Icons.Filled.Warning else Icons.Filled.Lock,
                    contentDescription = null,
                    tint = if (appName == "System Update") Color(0xFFCF6679) else Color.White,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.system_locked_title),
                    color = if (appName == "System Update") Color(0xFFCF6679) else Color.White,
                    fontSize = 20.sp,
                    letterSpacing = 4.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.lock_enter_pin),
                    color = Color.LightGray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(vertical = 32.dp)
            ) {
                for (i in 0 until 4) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .border(
                                width = 2.dp,
                                color = if (i < enteredPin.length) pinDotColor else Color.DarkGray,
                                shape = CircleShape
                            )
                            .background(
                                if (i < enteredPin.length) pinDotColor else Color.Transparent
                            )
                    )
                }
            }

            Column(
                modifier = Modifier.padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val padNumbers = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("", "0", "del")
                )

                for (row in padNumbers) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (key in row) {
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                if (key.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .size(72.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF1E1E1E))
                                            .clickable {
                                                if (key == "del") {
                                                    if (enteredPin.isNotEmpty()) enteredPin = enteredPin.dropLast(1)
                                                } else {
                                                    if (enteredPin.length < 4 && !isError) {
                                                        enteredPin += key

                                                        if (enteredPin.length == 4) {
                                                            if (enteredPin == correctPin) {
                                                                SessionManager.saveUnlockedApp(context, appName)
                                                                onSuccess()
                                                            } else {
                                                                isError = true

                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                    vibrator.vibrate(VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE))
                                                                } else {
                                                                    @Suppress("DEPRECATION")
                                                                    vibrator.vibrate(400)
                                                                }

                                                                coroutineScope.launch {
                                                                    delay(400)
                                                                    enteredPin = ""
                                                                    isError = false
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (key == "del") {
                                            Icon(Icons.Filled.Backspace, contentDescription = null, tint = Color.White)
                                        } else {
                                            Text(text = key, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Light)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}