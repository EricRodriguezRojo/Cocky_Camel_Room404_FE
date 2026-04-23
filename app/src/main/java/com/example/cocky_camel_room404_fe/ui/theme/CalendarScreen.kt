package com.example.cocky_camel_room404_fe

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class CalendarEvent(val day: Int, val title: String, val time: String, val description: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(onBack: () -> Unit) {
    var selectedDay by remember { mutableStateOf(22) }

    val events = listOf(
        CalendarEvent(3, "Dentista", "16:00", "Revisión anual. Llevar radiografías."),
        CalendarEvent(7, "Examen DAM", "09:00", "Entregar el proyecto a primera hora."),
        CalendarEvent(10, "DÍA CERO", "00:00", "No debí descargar ese archivo. El teléfono hace cosas raras."),
        CalendarEvent(10, "Comprar pan", "14:00", "2 barras integrales."),
        CalendarEvent(22, "Llamar a 'The Architect' - 6295", "18:30", "Él sabe cómo entrar en el sistema de archivos. Dice que tiene el parche de la galería listo."),
        CalendarEvent(14, "Cumpleaños Mamá", "Todo el día", "Comprar regalo y llamar por la tarde."),
        CalendarEvent(28, "Límite de borrado", "23:59", "Si no lo soluciono para hoy, perderé todos mis datos.")
    )

    val daysOfWeek = listOf("L", "M", "X", "J", "V", "S", "D")

    val emptyDaysBeforeStart = 2
    val totalDaysInMonth = 30

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        TopAppBar(
            title = { Text("Abril 2026", color = Color.White, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
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
                    val hasEvents = events.any { it.day == day }
                    val isSelected = day == selectedDay

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(if (isSelected) Color(0xFF03A9F4) else Color.Transparent)
                            .clickable { selectedDay = day },
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

        val selectedEvents = events.filter { it.day == selectedDay }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (selectedEvents.isEmpty()) {
                item {
                    Text(
                        text = "No hay eventos para este día",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp)
                    )
                }
            } else {
                items(selectedEvents) { event ->
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