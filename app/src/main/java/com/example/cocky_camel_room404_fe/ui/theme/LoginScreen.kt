package com.example.cocky_camel_room404_fe

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val context = LocalContext.current

    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("436902612551-pt3s24i3uth56jebunl199phsh3d30ks.apps.googleusercontent.com")
            .requestEmail()
            .build()
    }
    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                viewModel.onGoogleLogin(
                    context = context,
                    account = account,
                    onLoginSuccess = onLoginSuccess,
                    onToast = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                )
            }
        } catch (e: ApiException) {
            Toast.makeText(context, context.getString(R.string.login_google_canceled), Toast.LENGTH_SHORT).show()
        }
    }

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
                    color = MaterialTheme.colorScheme.primary,
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
                        MinimalistField(
                            value = viewModel.email,
                            onValueChange = { viewModel.email = it },
                            label = stringResource(R.string.login_email_label)
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        MinimalistField(
                            value = viewModel.password,
                            onValueChange = { viewModel.password = it },
                            label = stringResource(R.string.login_password_label),
                            isPassword = true
                        )

                        TextButton(
                            onClick = onNavigateToForgotPassword,
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(top = 4.dp, bottom = 16.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.login_forgot_password_link),
                                color = Color.White.copy(alpha = 0.75f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 1.sp
                            )
                        }

                        if (viewModel.isLoading) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        } else {
                            InteractionButton(
                                text = stringResource(R.string.login_access_button),
                                onClick = {
                                    viewModel.onLoginClick(
                                        context = context,
                                        onLoginSuccess = onLoginSuccess,
                                        onToast = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                                    )
                                }
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = stringResource(R.string.login_or_divider),
                                color = Color.White.copy(alpha = 0.3f),
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.height(24.dp))

                            InteractionButton(
                                text = stringResource(R.string.login_google_button),
                                isOutlined = true,
                                iconId = R.drawable.ic_google,
                                onClick = {
                                    googleLauncher.launch(googleSignInClient.signInIntent)
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            InteractionButton(
                                text = stringResource(R.string.login_register_link),
                                isOutlined = true,
                                onClick = onNavigateToRegister
                            )
                        }
                    }
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
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 11.sp, letterSpacing = 2.sp) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = keyboardOptions,
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

/**
 * BOTÓN UNIFICADO:
 * Ahora soporta estilo sólido y delineado (isOutlined), además de permitir un icono opcional.
 * Esto mantiene la misma animación, altura y proporciones para todos los botones.
 */
@Composable
fun InteractionButton(
    text: String,
    onClick: () -> Unit,
    isOutlined: Boolean = false,
    iconId: Int? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.96f else 1f, label = "button_scale")

    val buttonModifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .scale(scale)

    if (isOutlined) {
        OutlinedButton(
            onClick = onClick,
            interactionSource = interactionSource,
            modifier = buttonModifier,
            shape = MaterialTheme.shapes.extraSmall,
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White
            )
        ) {
            ButtonContent(text = text, iconId = iconId)
        }
    } else {
        Button(
            onClick = onClick,
            interactionSource = interactionSource,
            modifier = buttonModifier,
            shape = MaterialTheme.shapes.extraSmall,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.Black
            )
        ) {
            ButtonContent(text = text, iconId = iconId)
        }
    }
}

@Composable
private fun ButtonContent(text: String, iconId: Int?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (iconId != null) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = "Google Icon",
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            fontSize = 12.sp
        )
    }
}