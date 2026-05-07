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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainMenuScreen(
    onNewGame: () -> Unit,
    onContinue: () -> Unit,
    onSettings: () -> Unit,
    onRanking: () -> Unit,
    onLogout: () -> Unit
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
                        InteractionButton(text = stringResource(R.string.menu_new_game), onClick = onNewGame)
                        Spacer(modifier = Modifier.height(20.dp))

                        InteractionButton(text = stringResource(R.string.menu_continue), onClick = onContinue)
                        Spacer(modifier = Modifier.height(20.dp))

                        InteractionButton(text = stringResource(R.string.menu_settings), onClick = onSettings)
                        Spacer(modifier = Modifier.height(20.dp))

                        InteractionButton(text = stringResource(R.string.menu_ranking), onClick = onRanking)

                        Spacer(modifier = Modifier.height(32.dp))

                        OutlinedButton(
                            onClick = {
                                SessionManager.logout(context)
                                onLogout()
                            },
                            modifier = Modifier.fillMaxWidth().height(45.dp),
                            shape = MaterialTheme.shapes.extraSmall,
                            border = BorderStroke(0.5.dp, Color.Red.copy(alpha = 0.4f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red.copy(alpha = 0.7f))
                        ) {
                            Text(
                                stringResource(R.string.menu_terminate_session),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                        }
                    }
                }
            }
        }
    }
}