package com.example.cocky_camel_room404_fe

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PhoneScreen(onBack: () -> Unit) {
    var number by remember { mutableStateOf("") }
    var isCalling by remember { mutableStateOf(false) }
    var callStatus by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF121212))) {
        if (!isCalling) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .padding(top = 24.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                }
            }

            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.BottomCenter) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = number, color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Light, maxLines = 1)
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                val keys = listOf(listOf("1", "2", "3"), listOf("4", "5", "6"), listOf("7", "8", "9"), listOf("*", "0", "#"))
                for (row in keys) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        for (key in row) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF1E1E1E))
                                    .clickable { if (number.length < 15) number += key },
                                contentAlignment = Alignment.Center // Esto ya centra el texto solo
                            ) {
                                Text(text = key, color = Color.White, fontSize = 28.sp)
                            }
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.size(72.dp))
                    IconButton(
                        onClick = {
                            if (number.isNotEmpty()) {
                                isCalling = true
                                callStatus = "Llamando..."
                                scope.launch {
                                    delay(2000)
                                    if (number == "404" || number == "0404") {
                                        callStatus = "ERROR: SYSTEM_BUSY"
                                        delay(2000)
                                    } else {
                                        callStatus = "Línea interceptada"
                                        delay(1500)
                                    }
                                    isCalling = false
                                }
                            }
                        },
                        modifier = Modifier.size(72.dp).clip(CircleShape).background(Color(0xFF4CAF50))
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                    }
                    IconButton(onClick = { if (number.isNotEmpty()) number = number.dropLast(1) }) {
                        Icon(Icons.Default.Backspace, contentDescription = null, tint = Color.Gray)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().background(if (callStatus.contains("ERROR")) Color(0xFF330000) else Color(0xFF121212)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.size(120.dp).clip(CircleShape).background(Color.DarkGray), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(80.dp))
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text(text = number, color = Color.White, fontSize = 32.sp)
                Text(text = callStatus, color = if (callStatus.contains("ERROR")) Color.Red else Color.Gray, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(100.dp))
                IconButton(
                    onClick = { isCalling = false },
                    modifier = Modifier.size(72.dp).clip(CircleShape).background(Color.Red)
                ) {
                    Icon(Icons.Default.CallEnd, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                }
            }
        }
    }
}