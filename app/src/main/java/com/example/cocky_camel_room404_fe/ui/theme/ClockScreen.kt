package com.example.cocky_camel_room404_fe

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset

data class Alarm(val time: String, val label: String, val isEnabled: Boolean, val isSystemLocked: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClockScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    val accessDeniedMsg = stringResource(R.string.access_denied_task_blocked)
    val functionDisabledMsg = stringResource(R.string.function_disabled_temp)

    val initialAlarms = listOf(
        Alarm("07:00", stringResource(R.string.alarm_dam_class), true),
        Alarm("08:30", stringResource(R.string.alarm_project_meeting), false),
        Alarm("04:04", stringResource(R.string.alarm_system_disabled), true, true),
        Alarm("14:15", stringResource(R.string.alarm_lunch), true),
        Alarm("19:30", stringResource(R.string.alarm_gym), false)
    )

    var alarms by remember { mutableStateOf(initialAlarms) }
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(stringResource(R.string.clock_title), color = Color.White, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.back), tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF121212))
                )
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color(0xFF121212),
                    contentColor = Color(0xFF03A9F4),
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = Color(0xFF03A9F4)
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text(stringResource(R.string.tab_alarms), color = if (selectedTab == 0) Color(0xFF03A9F4) else Color.Gray) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text(stringResource(R.string.tab_world), color = if (selectedTab == 1) Color(0xFF03A9F4) else Color.Gray) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text(stringResource(R.string.tab_stopwatch), color = if (selectedTab == 2) Color(0xFF03A9F4) else Color.Gray) }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Toast.makeText(context, functionDisabledMsg, Toast.LENGTH_SHORT).show()
                },
                containerColor = Color(0xFF03A9F4),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        },
        containerColor = Color(0xFF121212)
    ) { paddingValues ->
        if (selectedTab == 0) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                items(alarms) { alarm ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF1E1E1E))
                            .padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = alarm.time,
                                color = if (alarm.isSystemLocked) Color.Red else if (alarm.isEnabled) Color.White else Color.Gray,
                                fontSize = 42.sp,
                                fontWeight = FontWeight.Light
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = alarm.label,
                                color = if (alarm.isSystemLocked) Color.Red.copy(alpha = 0.8f) else Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                        Switch(
                            checked = alarm.isEnabled,
                            onCheckedChange = { isChecked ->
                                if (alarm.isSystemLocked) {
                                    Toast.makeText(context, accessDeniedMsg, Toast.LENGTH_LONG).show()
                                } else {
                                    alarms = alarms.map {
                                        if (it.time == alarm.time) it.copy(isEnabled = isChecked) else it
                                    }
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = if (alarm.isSystemLocked) Color.Red else Color(0xFF03A9F4),
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color(0xFF333333)
                            )
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text(functionDisabledMsg, color = Color.Gray, fontSize = 16.sp)
            }
        }
    }
}