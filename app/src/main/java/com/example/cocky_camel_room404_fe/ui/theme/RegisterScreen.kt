package com.example.cocky_camel_room404_fe

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var nickname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

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
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.app_name).uppercase(),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraLight,
                    letterSpacing = 8.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                Surface(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    color = Color.Black.copy(alpha = 0.75f),
                    shape = MaterialTheme.shapes.medium,
                    border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.2f))
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        MinimalistField(
                            value = nickname,
                            onValueChange = { nickname = it },
                            label = stringResource(R.string.register_nickname_label)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        MinimalistField(
                            value = email,
                            onValueChange = { email = it },
                            label = stringResource(R.string.login_email_label)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        MinimalistField(
                            value = password,
                            onValueChange = { password = it },
                            label = stringResource(R.string.register_password_label),
                            isPassword = true
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        if (isLoading) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        } else {
                            InteractionButton(
                                text = stringResource(R.string.register_create_button),
                                onClick = {
                                    if (email.isBlank() || password.isBlank() || nickname.isBlank()) {
                                        Toast.makeText(context, context.getString(R.string.register_required_fields), Toast.LENGTH_SHORT).show()
                                        return@InteractionButton
                                    }

                                    coroutineScope.launch {
                                        isLoading = true
                                        try {
                                            val newUser = User(
                                                email = email,
                                                nickname = nickname,
                                                password = password,
                                                role = "User",
                                                isPremium = true
                                            )
                                            val response = RetrofitClient.instance.register(newUser)

                                            if (response.isSuccessful) {
                                                Toast.makeText(context, context.getString(R.string.register_success), Toast.LENGTH_SHORT).show()
                                                onRegisterSuccess()
                                            } else {
                                                Toast.makeText(context, context.getString(R.string.register_email_exists), Toast.LENGTH_LONG).show()
                                            }
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "${context.getString(R.string.login_critical_error)}: ${e.message}", Toast.LENGTH_LONG).show()
                                        } finally {
                                            isLoading = false
                                        }
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(onClick = onNavigateToLogin) {
                    Text(
                        text = stringResource(R.string.register_login_link),
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}