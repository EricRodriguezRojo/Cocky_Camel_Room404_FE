package com.example.cocky_camel_room404_fe

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SystemSettingRow(
    val titleRes: Int,
    val subtitleRes: Int,
    val icon: ImageVector,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SystemSettingsScreen(onBack: () -> Unit) {
    val sections = listOf(
        SystemSettingRow(R.string.sys_wifi, R.string.sys_wifi_sub, Icons.Default.Wifi, Color(0xFF4CAF50)),
        SystemSettingRow(R.string.sys_display, R.string.sys_display_sub, Icons.Default.Brightness6, Color(0xFFFF9800)),
        SystemSettingRow(R.string.sys_battery, R.string.sys_battery_sub, Icons.Default.BatteryFull, Color(0xFFF44336)),
        SystemSettingRow(R.string.sys_storage, R.string.sys_storage_sub, Icons.Default.Storage, Color(0xFF2196F3)),
        SystemSettingRow(R.string.sys_security, R.string.sys_security_sub, Icons.Default.Security, Color(0xFF9C27B0)),
        SystemSettingRow(R.string.sys_about, R.string.sys_about_sub, Icons.Default.Info, Color(0xFF607D8B))
    )

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.app_settings),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF121212),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF121212)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF121212))
        ) {
            item {
                Text(
                    text = stringResource(R.string.sys_category_device),
                    modifier = Modifier.padding(16.dp),
                    color = Color(0xFFBB86FC),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(sections) { item ->
                ListItem(
                    headlineContent = { Text(stringResource(item.titleRes), color = Color.White) },
                    supportingContent = { Text(stringResource(item.subtitleRes), color = Color.Gray) },
                    leadingContent = {
                        Icon(item.icon, contentDescription = null, tint = item.color, modifier = Modifier.size(28.dp))
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    modifier = Modifier.clickable { /* Simulación de click */ }
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = Color.DarkGray, modifier = Modifier.padding(horizontal = 16.dp))
                Text(
                    text = "Android Version: 4.0.4\nKernel: Room404-Stable",
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = Color.DarkGray,
                    fontSize = 11.sp
                )
            }
        }
    }
}