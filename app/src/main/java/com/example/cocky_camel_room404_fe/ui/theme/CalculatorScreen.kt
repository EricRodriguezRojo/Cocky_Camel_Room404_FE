package com.example.cocky_camel_room404_fe

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(onBack: () -> Unit) {
    var displayText by remember { mutableStateOf("") }
    var isGlitching by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val err404 = stringResource(R.string.calc_err_404)
    val errMalware = stringResource(R.string.calc_malware_sys)
    val errCorrupted = stringResource(R.string.calc_corrupted)

    val buttons = listOf(
        listOf("AC", "(", ")", "/"),
        listOf("7", "8", "9", "*"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "del", "=")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        TopAppBar(
            title = { Text("", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = displayText,
                color = if (isGlitching) Color.Red else Color.White,
                fontSize = if (displayText.length > 10) 36.sp else 56.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.End,
                lineHeight = 60.sp
            )
        }

        Divider(color = Color.DarkGray, thickness = 1.dp, modifier = Modifier.padding(horizontal = 24.dp))
        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            for (row in buttons) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (btn in row) {
                        val isOperator = btn in listOf("/", "*", "-", "+", "=")
                        val isAction = btn in listOf("AC", "(", ")")

                        val bgColor = when {
                            isOperator -> Color(0xFF03A9F4)
                            isAction -> Color(0xFF333333)
                            else -> Color(0xFF1E1E1E)
                        }

                        val txtColor = when {
                            isOperator -> Color.White
                            isAction -> Color(0xFF03A9F4)
                            else -> Color.White
                        }

                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(bgColor)
                                .clickable {
                                    if (!isGlitching) {
                                        when (btn) {
                                            "AC" -> displayText = ""
                                            "del" -> if (displayText.isNotEmpty()) displayText = displayText.dropLast(1)
                                            "=" -> {
                                                if (displayText.isNotEmpty()) {
                                                    coroutineScope.launch {
                                                        isGlitching = true
                                                        val originalText = displayText
                                                        displayText = err404
                                                        delay(300)
                                                        displayText = errMalware
                                                        delay(300)
                                                        displayText = errCorrupted
                                                        delay(1000)
                                                        displayText = originalText
                                                        isGlitching = false
                                                    }
                                                }
                                            }
                                            else -> {
                                                if (displayText.length < 15) displayText += btn
                                            }
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (btn == "del") {
                                Icon(
                                    Icons.Filled.Backspace,
                                    contentDescription = stringResource(R.string.delete),
                                    tint = Color.White
                                )
                            } else {
                                Text(text = btn, color = txtColor, fontSize = 28.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}