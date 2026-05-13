package com.example.cocky_camel_room404_fe

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun ResetPasswordScreen(
    token: String?,
    recoveryEmail: String = "",
    onResetSuccess: () -> Unit,
    onBack: () -> Unit
) {
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }
    var feedbackColor by remember { mutableStateOf(Color.White) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val colorPrimary = MaterialTheme.colorScheme.primary
    val colorError = MaterialTheme.colorScheme.error

    LaunchedEffect(Unit) { visible = true }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondo_sistema),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(800)) + slideInVertically(initialOffsetY = { 40 })
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(R.string.app_name).uppercase(), fontSize = 28.sp, fontWeight = FontWeight.ExtraLight, color = colorPrimary)

                Spacer(modifier = Modifier.height(18.dp))

                Surface(modifier = Modifier.fillMaxWidth(), color = Color.Black.copy(alpha = 0.75f), shape = MaterialTheme.shapes.medium, border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.2f))) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Ingresá la nueva contraseña", color = Color.White)

                        Spacer(modifier = Modifier.height(12.dp))

                        TextField(
                            value = password,
                            onValueChange = { password = it; if (feedbackMessage.isNotBlank()) feedbackMessage = "" },
                            label = { Text("Nueva contraseña", color = Color.White.copy(alpha = 0.7f)) },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = colorPrimary,
                                focusedIndicatorColor = colorPrimary,
                                unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f)
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        TextField(
                            value = repeatPassword,
                            onValueChange = { repeatPassword = it; if (feedbackMessage.isNotBlank()) feedbackMessage = "" },
                            label = { Text("Repetir contraseña", color = Color.White.copy(alpha = 0.7f)) },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = colorPrimary,
                                focusedIndicatorColor = colorPrimary,
                                unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f)
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (feedbackMessage.isNotBlank()) {
                            Text(text = feedbackMessage, color = feedbackColor)
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        if (isLoading) {
                            CircularProgressIndicator(color = colorPrimary, modifier = Modifier.size(36.dp))
                        } else {
                            Button(onClick = {
                                if (password.isBlank() || repeatPassword.isBlank()) {
                                    feedbackMessage = "Completá ambos campos"
                                    feedbackColor = colorError
                                    return@Button
                                }
                                if (password != repeatPassword) {
                                    feedbackMessage = "Las contraseñas no coinciden"
                                    feedbackColor = colorError
                                    return@Button
                                }
                                if (token.isNullOrBlank()) {
                                    feedbackMessage = "Token faltante"
                                    feedbackColor = colorError
                                    return@Button
                                }

                                coroutineScope.launch {
                                    isLoading = true
                                    feedbackMessage = ""
                                    try {
                                        val email = recoveryEmail.ifBlank { SessionManager.getResetEmail(context) ?: "" }
                                        val response = RetrofitClient.instance.resetPassword(mapOf("email" to email, "token" to token, "password" to password))
                                        if (response.isSuccessful) {
                                            SessionManager.clearResetEmail(context)
                                            onResetSuccess()
                                        } else {
                                            feedbackMessage = response.errorBody()?.string() ?: "Error al resetear contraseña"
                                            feedbackColor = colorError
                                        }
                                    } catch (e: Exception) {
                                        feedbackMessage = "Error de red: ${e.localizedMessage ?: ""}"
                                        feedbackColor = colorError
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            }, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                                Text(text = "Cambiar contraseña")
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        TextButton(onClick = onBack) { Text(text = "Volver", color = Color.White.copy(alpha = 0.65f)) }
                    }
                }
            }
        }
    }
}
