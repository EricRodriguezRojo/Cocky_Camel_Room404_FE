package com.example.cocky_camel_room404_fe

import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Chat(val id: String, val name: String, val lastMessage: String, val time: String)
data class Message(val text: String, val isFromMe: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val chats = listOf(
        Chat("1", "Desconocido", "¿Lo has encontrado ya?", "11:45"),
        Chat("2", "Mamá", "Acuérdate de comprar pan.", "Ayer"),
        Chat("3", "Compañeros de piso", "Alguien se ha dejado el fuego encendido.", "Ayer"),
        Chat("4", "Vodafone", "Tu factura ya está disponible.", "Lunes")
    )

    var currentChat by remember { mutableStateOf<Chat?>(null) }
    var chatMessages by remember { mutableStateOf(listOf<Message>()) }
    var inputText by remember { mutableStateOf("") }

    var isGlitching by remember { mutableStateOf(false) }
    var showGlitchOverlay by remember { mutableStateOf(false) }
    var showSystemMessage by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val offsetX by infiniteTransition.animateFloat(
        initialValue = if (isGlitching) -15f else 0f,
        targetValue = if (isGlitching) 15f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(50, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    val colorOverlay = if (showGlitchOverlay) Color.Red.copy(alpha = 0.4f) else Color.Transparent

    LaunchedEffect(currentChat) {
        if (currentChat?.id == "1") {
            chatMessages = listOf(
                Message("No sé de qué me hablas.", true),
                Message("Sabes perfectamente de qué hablo.", false),
                Message("El tiempo se acaba.", false),
                Message("¿Lo has encontrado ya?", false)
            )
        } else if (currentChat != null) {
            chatMessages = listOf(Message(currentChat!!.lastMessage, false))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .graphicsLayer { translationX = offsetX }
    ) {
        if (currentChat == null) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopAppBar(
                    title = { Text("Mensajes", color = Color.White, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A1A))
                )

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(chats) { chat ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { currentChat = chat }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(if(chat.id == "1") Color.Red else Color(0xFF03A9F4)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.Person, contentDescription = null, tint = Color.White)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = chat.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                                    Text(text = chat.time, color = Color.Gray, fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = chat.lastMessage,
                                    color = Color.LightGray,
                                    fontSize = 14.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                TopAppBar(
                    title = { Text(currentChat!!.name, color = Color.White, fontSize = 18.sp) },
                    navigationIcon = {
                        IconButton(onClick = { currentChat = null }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A1A))
                )

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    reverseLayout = true
                ) {
                    items(chatMessages.reversed()) { msg ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = if (msg.isFromMe) Arrangement.End else Arrangement.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (msg.isFromMe) Color(0xFF03A9F4) else Color(0xFF2A2A2A))
                                    .padding(12.dp)
                            ) {
                                Text(text = msg.text, color = Color.White, fontSize = 16.sp)
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1A1A1A))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(24.dp)),
                        placeholder = { Text("Mensaje de texto", color = Color.Gray) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF2A2A2A),
                            unfocusedContainerColor = Color(0xFF2A2A2A),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                val textSent = inputText.trim()
                                chatMessages = chatMessages + Message(textSent, true)
                                inputText = ""

                                if (currentChat?.id == "1" && textSent.equals("malware", ignoreCase = true)) {

                                    val segundosTardados = TimeTracker.getSecondsElapsedAndReset()

                                    coroutineScope.launch {
                                        try {
                                            val token = SessionManager.getToken(context)
                                            if (token != null) {
                                                RetrofitClient.instance.triggerMalware("Bearer $token")
                                                RetrofitClient.instance.completePuzzle(
                                                    token = "Bearer $token",
                                                    puzzleName = "Malware Enigma",
                                                    body = mapOf("timeSeconds" to segundosTardados)
                                                )
                                            }
                                        } catch (e: Exception) {
                                        }
                                    }

                                    coroutineScope.launch {
                                        isGlitching = true
                                        showGlitchOverlay = true

                                        delay(800)

                                        isGlitching = false
                                        showGlitchOverlay = false

                                        showSystemMessage = true
                                        delay(4000)
                                        showSystemMessage = false
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF03A9F4))
                    ) {
                        Icon(Icons.Filled.Send, contentDescription = null, tint = Color.White)
                    }
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize().background(colorOverlay))

        if (showSystemMessage) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.95f))
                        .border(2.dp, Color.Red, RoundedCornerShape(12.dp))
                        .padding(24.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "SYSTEM_OVERRIDE",
                            color = Color.Red,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Has forzado una brecha de seguridad. El sistema ha generado un enigma de recuperación.",
                            color = Color.White,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "REVISA TU BANDEJA DE ENTRADA REAL.",
                            color = Color.Red,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}