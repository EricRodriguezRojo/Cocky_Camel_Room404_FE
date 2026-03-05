package com.example.cocky_camel_room404_fe

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    // Estados para los inputs basados en la base de datos del PDF
    var nickname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Estado para la animación de entrada
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo (Igual que el Login)
        Image(
            painter = painterResource(id = R.drawable.fondo_sistema),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Contenido Animado
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(initialOffsetY = { 50 })
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Título de Registro
                Text(
                    text = "NEW_NODE",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraLight,
                    letterSpacing = 8.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // El Rectángulo Negro (Misma opacidad y borde que el Login)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    color = Color.Black.copy(alpha = 0.75f),
                    shape = MaterialTheme.shapes.medium,
                    border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.2f))
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Campo Nickname
                        MinimalistField(
                            value = nickname,
                            onValueChange = { nickname = it },
                            label = "ASSIGN_NICKNAME"
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Campo Email
                        MinimalistField(
                            value = email,
                            onValueChange = { email = it },
                            label = "EMAIL_ADDRESS"
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Campo Password
                        MinimalistField(
                            value = password,
                            onValueChange = { password = it },
                            label = "SECURITY_KEY",
                            isPassword = true
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Botón de Registro
                        InteractionButton(
                            text = "CREATE CREDENTIALS",
                            onClick = onRegisterSuccess
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Enlace para volver
                TextButton(onClick = onNavigateToLogin) {
                    Text(
                        "ALREADY REGISTERED? LOGIN",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

// COMPONENTES REUTILIZADOS (Copiados de tu Login para asegurar 0 errores)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun String.MinimalistField(
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(this, fontSize = 11.sp, letterSpacing = 2.sp) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = Color.White.copy(alpha = 0.5f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        visualTransformation = if (isPassword)
            androidx.compose.ui.text.input.PasswordVisualTransformation()
        else androidx.compose.ui.text.input.VisualTransformation.None,
        shape = MaterialTheme.shapes.small
    )
}