package com.example.cocky_camel_room404_fe

import android.media.MediaPlayer
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PhoneScreen(
    onBack: () -> Unit,
    viewModel: PhoneViewModel = viewModel()
) {
    val context = LocalContext.current

    val hackerMessage = stringResource(R.string.phone_hacker_message)
    val encryptingStr = stringResource(R.string.phone_encrypting)
    val callingStr = stringResource(R.string.phone_calling)
    val noNumberStr = stringResource(R.string.phone_no_number)
    val architectName = stringResource(R.string.event_architect_title)
    val backStr = stringResource(R.string.back)
    val callActionStr = stringResource(R.string.phone_call_action)
    val endCallStr = stringResource(R.string.phone_end_call)
    val sysErrorStr = stringResource(R.string.calc_err_404)

    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.llamada) }
    val tonoPlayer = remember { MediaPlayer.create(context, R.raw.tono) }

    DisposableEffect(Unit) {
        onDispose {
            if (mediaPlayer.isPlaying) mediaPlayer.stop()
            mediaPlayer.release()
            if (tonoPlayer.isPlaying) tonoPlayer.stop()
            tonoPlayer.release()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF121212))) {
        if (!viewModel.isCalling) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .padding(top = 24.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = backStr, tint = Color.White)
                }
            }

            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.BottomCenter) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = viewModel.number, color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Light, maxLines = 1)
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
                                    .clickable { viewModel.addDigit(key) },
                                contentAlignment = Alignment.Center
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
                            viewModel.startCall(
                                context = context,
                                encryptingStr = encryptingStr,
                                callingStr = callingStr,
                                noNumberStr = noNumberStr,
                                architectName = architectName,
                                sysErrorStr = sysErrorStr,
                                hackerMessage = hackerMessage,
                                onStartTono = { tonoPlayer.start() },
                                onStopTono = { 
                                    if (tonoPlayer.isPlaying) tonoPlayer.pause()
                                    tonoPlayer.seekTo(0)
                                },
                                onStartHackerAudio = { mediaPlayer.start() }
                            )
                        },
                        modifier = Modifier.size(72.dp).clip(CircleShape).background(Color(0xFF4CAF50))
                    ) {
                        Icon(Icons.Default.Call, contentDescription = callActionStr, tint = Color.White, modifier = Modifier.size(32.dp))
                    }
                    IconButton(onClick = { viewModel.removeLastDigit() }) {
                        Icon(Icons.Default.Backspace, contentDescription = null, tint = Color.Gray)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (viewModel.number == "6295") Color.Black else Color(0xFF121212)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (viewModel.number == "6295") {
                    Icon(Icons.Default.Terminal, contentDescription = null, tint = Color(0xFF00FF00), modifier = Modifier.size(80.dp))
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = viewModel.callStatus, color = Color(0xFF00FF00), fontSize = 20.sp, fontWeight = FontWeight.Bold)

                    Box(modifier = Modifier.fillMaxWidth().height(250.dp).padding(24.dp)) {
                        Text(
                            text = viewModel.typewriterText,
                            color = Color(0xFF00FF00),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    }
                } else {
                    Box(modifier = Modifier.size(120.dp).clip(CircleShape).background(Color.DarkGray), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(80.dp))
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(text = viewModel.number, color = Color.White, fontSize = 32.sp)
                    Text(text = viewModel.callStatus, color = Color.Gray, fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(48.dp))

                IconButton(
                    onClick = {
                        viewModel.endCall {
                            if (mediaPlayer.isPlaying) mediaPlayer.pause()
                            mediaPlayer.seekTo(0)
                            if (tonoPlayer.isPlaying) tonoPlayer.pause()
                            tonoPlayer.seekTo(0)
                        }
                    },
                    modifier = Modifier.size(72.dp).clip(CircleShape).background(Color.Red)
                ) {
                    Icon(Icons.Default.CallEnd, contentDescription = endCallStr, tint = Color.White, modifier = Modifier.size(32.dp))
                }
            }
        }
    }
}
