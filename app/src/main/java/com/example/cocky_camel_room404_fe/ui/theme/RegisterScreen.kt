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
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val context = LocalContext.current

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
                            value = viewModel.nickname,
                            onValueChange = { viewModel.nickname = it },
                            label = stringResource(R.string.register_nickname_label)
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        MinimalistField(
                            value = viewModel.email,
                            onValueChange = { viewModel.email = it },
                            label = stringResource(R.string.login_email_label)
                        )
                        if (!viewModel.isEmailValid) {
                            Text(
                                text = "Formato de correo electrónico inválido (ejemplo@mail.com)",
                                color = Color.Red,
                                fontSize = 11.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp, start = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        MinimalistField(
                            value = viewModel.password,
                            onValueChange = { viewModel.password = it },
                            label = stringResource(R.string.register_password_label),
                            isPassword = true
                        )
                        if (!viewModel.isPasswordStrong) {
                            Text(
                                text = "Contraseña débil (mínimo 6 caracteres)",
                                color = Color.Red,
                                fontSize = 11.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp, start = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        if (viewModel.isLoading) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        } else {
                            InteractionButton(
                                text = stringResource(R.string.register_create_button),
                                enabled = viewModel.canSubmit,
                                onClick = {
                                    viewModel.onRegisterClick(
                                        context = context,
                                        onRegisterSuccess = onRegisterSuccess,
                                        onToast = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                                    )
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