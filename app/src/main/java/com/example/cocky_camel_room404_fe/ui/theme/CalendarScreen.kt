package com.example.cocky_camel_room404_fe.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cocky_camel_room404_fe.CalendarEvent
import com.example.cocky_camel_room404_fe.CalendarViewModel
import com.example.cocky_camel_room404_fe.R

import androidx.compose.foundation.lazy.items as lazyColumnItems
import androidx.compose.foundation.lazy.grid.items as lazyGridItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onBack: () -> Unit,
    viewModel: CalendarViewModel = viewModel()
) {
    val events = listOf(
        CalendarEvent(3, stringResource(R.string.event_dentist_title), "16:00", stringResource(R.string.event_dentist_desc)),
        CalendarEvent(7, stringResource(R.string.event_exam_title), "09:00", stringResource(R.string.event_exam_desc)),
        CalendarEvent(10, stringResource(R.string.event_dayzero_title), "00:00", stringResource(R.string.event_dayzero_desc)),
        CalendarEvent(10, stringResource(R.string.event_bread_title), "14:00", stringResource(R.string.event_bread_desc)),
        CalendarEvent(22, stringResource(R.string.event_architect_title), "18:30", stringResource(R.string.event_architect_desc)),
        CalendarEvent(14, stringResource(R.string.event_mom_title), stringResource(R.string.event_allday), stringResource(R.string.event_mom_desc)),
        CalendarEvent(28, stringResource(R.string.event_deletion_title), "23:59", stringResource(R.string.event_deletion_desc))
    )

    LaunchedEffect(events) {
        viewModel.loadEvents(events)
    }

    val daysOfWeek = listOf(
        stringResource(R.string.day_l),
        stringResource(R.string.day_m),
        stringResource(R.string.day_x),
        stringResource(R.string.day_j),
        stringResource(R.string.day_v),
        stringResource(R.string.day_s),
        stringResource(R.string.day_d)
    )

    val emptyDaysBeforeStart = 2
    val totalDaysInMonth = 30

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        TopAppBar(
            title = { Text(stringResource(R.string.calendar_month_year), color = Color.White, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back), tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A1A))
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                daysOfWeek.forEach { day ->
                    Text(
                        text = day,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(emptyDaysBeforeStart) {
                    Box(modifier = Modifier.aspectRatio(1f))
                }

                items(totalDaysInMonth) { index ->
                    val day = index + 1
                    val hasEvents = viewModel.events.any { it.day == day }
                    val isSelected = day == viewModel.selectedDay

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(if (isSelected) Color(0xFF03A9F4) else Color.Transparent)
                            .clickable { viewModel.onDayClick(day) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = day.toString(),
                                color = if (isSelected) Color.White else Color.LightGray,
                                fontSize = 16.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                            if (hasEvents && !isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF03A9F4))
                                )
                            }
                        }
                    }
                }
            }
        }

        HorizontalDivider(color = Color.DarkGray, thickness = 1.dp)

        val selectedEvents = viewModel.events.filter { it.day == viewModel.selectedDay }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (selectedEvents.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.no_events_msg),
                        color = Color.Gray,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp)
                    )
                }
            } else {
                lazyColumnItems(selectedEvents) { event ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1E1E1E))
                            .padding(16.dp)
                    ) {
                        Row {
                            Text(
                                text = event.time,
                                color = Color(0xFF03A9F4),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.width(60.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = event.title,
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = event.description,
                                    color = Color.LightGray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
