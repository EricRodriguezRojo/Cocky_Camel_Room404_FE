package com.example.cocky_camel_room404_fe

import android.util.Patterns
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToToken: (String) -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }
    var feedbackColor by remember { mutableStateOf(Color.White) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()


    val colorPrimary = MaterialTheme.colorScheme.primary
    val colorError = MaterialTheme.colorScheme.error

    val sendButtonText = stringResource(R.string.forgot_password_send_button)
    val requiredEmailMsg = context.getString(R.string.forgot_password_required_email)
    val invalidEmailMsg = context.getString(R.string.forgot_password_invalid_email)
    val successMsg = context.getString(R.string.forgot_password_success)
    val errorMsg = context.getString(R.string.forgot_password_error)
    val networkErrorMsg = context.getString(R.string.login_network_error)

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
            enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(initialOffsetY = { 50 })
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.app_name).uppercase(),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraLight,
                    letterSpacing = 8.sp,
                    color = colorPrimary,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

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
                        Text(
                            text = stringResource(R.string.forgot_password_title),
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        MinimalistField(
                            value = email,
                            onValueChange = {
                                email = it
                                if (feedbackMessage.isNotBlank()) feedbackMessage = ""
                            },
                            label = stringResource(R.string.forgot_password_email_label),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        if (feedbackMessage.isNotBlank()) {
                            Text(
                                text = feedbackMessage,
                                color = feedbackColor,
                                fontSize = 12.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        if (isLoading) {
                            CircularProgressIndicator(
                                color = colorPrimary,
                                modifier = Modifier.size(40.dp)
                            )
                        } else {
                            Button(
                                onClick = {
                                    val trimmedEmail = email.trim()
                                    when {
                                        trimmedEmail.isBlank() -> {
                                            feedbackMessage = requiredEmailMsg
                                            feedbackColor = colorError
                                        }
                                        !Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches() -> {
                                            feedbackMessage = invalidEmailMsg
                                            feedbackColor = colorError
                                        }
                                        else -> {
                                            coroutineScope.launch {
                                                isLoading = true
                                                feedbackMessage = ""
                                                try {
                                                    val response = RetrofitClient.instance.forgotPassword(
                                                        ForgotPasswordRequest(email = trimmedEmail)
                                                    )

                                                    if (response.isSuccessful) {
                                                        val body = response.body()?.toString()
                                                        feedbackMessage = if (!body.isNullOrBlank()) body else successMsg
                                                        feedbackColor = colorPrimary
                                                    } else {
                                                        feedbackMessage = "Email enviado. Revisa tu bandeja de entrada."
                                                        feedbackColor = colorPrimary
                                                    }
                                                    // Guardar email y navegar sin importar éxito/error
                                                    SessionManager.saveResetEmail(context, trimmedEmail)
                                                    kotlinx.coroutines.delay(1500)
                                                    onNavigateToToken(trimmedEmail)
                                                } catch (e: Exception) {
                                                    feedbackMessage = "Email enviado. Revisa tu bandeja de entrada."
                                                    feedbackColor = colorPrimary
                                                    // Aún así, guardar email y navegar
                                                    SessionManager.saveResetEmail(context, trimmedEmail)
                                                    kotlinx.coroutines.delay(1500)
                                                    onNavigateToToken(trimmedEmail)
                                                } finally {
                                                    isLoading = false
                                                }
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = MaterialTheme.shapes.extraSmall,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorPrimary,
                                    contentColor = Color.Black
                                )
                            ) {
                                Text(
                                    text = sendButtonText,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (feedbackMessage == successMsg || feedbackColor == colorPrimary) {
                    TextButton(onClick = { onNavigateToToken(email.trim()) }) {
                        Text(
                            text = "Ingresar token",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }

                TextButton(
                    onClick = onNavigateToLogin,
                    enabled = !isLoading
                ) {
                    Text(
                        text = stringResource(R.string.forgot_password_back_link),
                        color = Color.White.copy(alpha = 0.65f),
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinimalistField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White.copy(alpha = 0.7f)) },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f)
        ),
        keyboardOptions = keyboardOptions,
        singleLine = true
    )
}