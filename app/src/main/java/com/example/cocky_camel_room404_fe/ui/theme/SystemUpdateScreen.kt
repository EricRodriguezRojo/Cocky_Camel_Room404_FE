package com.example.cocky_camel_room404_fe

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.BatteryManager
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.sqrt

@Composable
fun SystemUpdateScreen(
    onSaveProgress: (String, Int) -> Unit,
    onFinish: () -> Unit
) {
    val context = LocalContext.current

    var gameStep by remember { mutableStateOf(0) }
    var flashWhite by remember { mutableStateOf(false) }

    var shakeStartTime by remember { mutableLongStateOf(0L) }
    var chargeStartTime by remember { mutableLongStateOf(0L) }

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
            gameStep == 1 -> Color(0xFF883300)
            gameStep == 2 -> Color.Black
            else -> Color(0xFF003300)
        },
        label = ""
    )

    LaunchedEffect(Unit) {
        shakeStartTime = System.currentTimeMillis()
    }

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null && gameStep == 0) {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    val acceleration = sqrt((x * x + y * y + z * z).toDouble()) - SensorManager.GRAVITY_EARTH

                    if (acceleration > 12) {
                        val timeTaken = (System.currentTimeMillis() - shakeStartTime) / 1000
                        onSaveProgress("SHAKE_SYSTEM", timeTaken.toInt())

                        chargeStartTime = System.currentTimeMillis()

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
                        } else {
                            @Suppress("DEPRECATION")
                            vibrator.vibrate(1000)
                        }
                        gameStep = 1
                    }
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        val powerReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Intent.ACTION_POWER_CONNECTED && gameStep == 1) {
                    val timeTaken = (System.currentTimeMillis() - chargeStartTime) / 1000
                    onSaveProgress("CHARGER_SYSTEM", timeTaken.toInt())
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
        if (gameStep == 1) {
            val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
                context.registerReceiver(null, ifilter)
            }
            val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
            val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL

            if (isCharging) {
                delay(2000)
                val timeTaken = (System.currentTimeMillis() - chargeStartTime) / 1000
                onSaveProgress("CHARGER_SYSTEM", timeTaken.toInt())
                gameStep = 2
            }
        }
    }

    LaunchedEffect(gameStep) {
        if (gameStep == 2) {
            delay(4000)
            gameStep = 3
        } else if (gameStep == 3) {
            flashWhite = true
            delay(100)
            flashWhite = false
            delay(100)
            flashWhite = true
            delay(500)
            flashWhite = false
            delay(3500)
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
                        text = stringResource(R.string.system_update_jammed),
                        color = Color.Red.copy(alpha = alphaAnim),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(48.dp))
                    Text(
                        text = stringResource(R.string.system_update_shake),
                        color = Color.White,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        letterSpacing = 4.sp
                    )
                }
            }
            1 -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Filled.Warning,
                        contentDescription = null,
                        tint = Color(0xFFFF9800).copy(alpha = alphaAnim),
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = stringResource(R.string.system_update_insufficient),
                        color = Color(0xFFFF9800),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(48.dp))
                    Text(
                        text = stringResource(R.string.system_update_connect_power),
                        color = Color.White.copy(alpha = alphaAnim),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        letterSpacing = 2.sp
                    )
                }
            }
            2 -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        color = Color.Green,
                        modifier = Modifier.size(60.dp),
                        strokeWidth = 6.dp
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "SYSTEM UPDATING...\nPURGING MALWARE",
                        color = Color.Green.copy(alpha = alphaAnim),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        letterSpacing = 2.sp
                    )
                }
            }
            3 -> {
                if (!flashWhite) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "SYSTEM UPDATED",
                            color = Color.Green,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "${stringResource(R.string.system_update_virus_deleted)}\n${stringResource(R.string.system_update_escaped)}",
                            color = Color.White,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    }
}