package com.example.cocky_camel_room404_fe

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    val context = LocalContext.current
    
    LaunchedEffect(Unit) {
        viewModel.initVolume(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        TopAppBar(
            title = { Text(stringResource(R.string.menu_settings_title), color = Color.White, fontSize = 18.sp) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A1A))
        )

        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Language, contentDescription = null, tint = Color(0xFF03A9F4))
                Spacer(modifier = Modifier.width(12.dp))
                Text(stringResource(R.string.settings_language), color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                LanguageButton(viewModel, "ca", stringResource(R.string.lang_ca), Modifier.weight(1f))
                LanguageButton(viewModel, "es", stringResource(R.string.lang_es), Modifier.weight(1f))
                LanguageButton(viewModel, "en", stringResource(R.string.lang_en), Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(40.dp))
            HorizontalDivider(color = Color.DarkGray)
            Spacer(modifier = Modifier.height(40.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.VolumeUp, contentDescription = null, tint = Color(0xFF03A9F4))
                Spacer(modifier = Modifier.width(12.dp))
                Text(stringResource(R.string.settings_volume), color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Slider(
                value = viewModel.currentVolume,
                onValueChange = {
                    viewModel.setVolume(context, it)
                },
                valueRange = 0f..viewModel.maxVolume.coerceAtLeast(1f),
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF03A9F4),
                    activeTrackColor = Color(0xFF03A9F4)
                )
            )
        }
    }
}

@Composable
fun LanguageButton(viewModel: SettingsViewModel, langCode: String, label: String, modifier: Modifier) {
    OutlinedButton(
        onClick = {
            viewModel.setLanguage(langCode)
        },
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color(0xFF333333))
    ) {
        Text(label, color = Color.LightGray, fontSize = 12.sp)
    }
}
