package com.example.cocky_camel_room404_fe

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.sqrt

@Composable
fun SystemUpdateScreen(onFinish: () -> Unit) {
    val context = LocalContext.current
    var gameStep by remember { mutableStateOf(0) }
    var flashWhite by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    val bgColor by animateColorAsState(
        targetValue = when {
            flashWhite -> Color.White
            gameStep == 0 -> Color(0xFF330000)
            gameStep == 1 -> Color.Black
            else -> Color(0xFF003300)
        },
        label = ""
    )

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null && gameStep == 0) {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    val acceleration = sqrt((x * x + y * y + z * z).toDouble()) - SensorManager.GRAVITY_EARTH
                    if (acceleration > 12) {
                        gameStep = 1
                    }
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        val powerReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Intent.ACTION_POWER_CONNECTED && gameStep == 1) {
                    gameStep = 2
                }
            }
        }

        sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_UI)
        context.registerReceiver(powerReceiver, IntentFilter(Intent.ACTION_POWER_CONNECTED))

        onDispose {
            sensorManager.unregisterListener(sensorListener)
            context.unregisterReceiver(powerReceiver)
        }
    }

    LaunchedEffect(gameStep) {
        if (gameStep == 2) {
            flashWhite = true
            delay(150)
            flashWhite = false
            delay(150)
            flashWhite = true
            delay(500)
            flashWhite = false
            delay(4000)
            onFinish()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        when (gameStep) {
            0 -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Filled.Warning,
                        contentDescription = null,
                        tint = Color.Red.copy(alpha = alphaAnim),
                        modifier = Modifier.size(100.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "SYSTEM JAMMED\nCRITICAL ERROR",
                        color = Color.Red.copy(alpha = alphaAnim),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(48.dp))
                    Text(
                        text = "SHAKE DEVICE TO OVERRIDE",
                        color = Color.White,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        letterSpacing = 4.sp
                    )
                }
            }
            1 -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ENERGY INSUFFICIENT\nFOR CORE DELETION",
                        color = Color.Red,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "CONNECT POWER SOURCE\nIMMEDIATELY",
                        color = Color.White.copy(alpha = alphaAnim),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        letterSpacing = 2.sp
                    )
                }
            }
            2 -> {
                if (!flashWhite) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "VIRUS DELETED",
                            color = Color.Green,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "YOU ESCAPED ROOM 404",
                            color = Color.White,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}