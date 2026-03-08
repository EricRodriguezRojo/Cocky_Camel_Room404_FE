package com.example.cocky_camel_room404_fe

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Note(val title: String, val content: String, val date: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    val notes = listOf(
        Note(
            title = "Lista de la compra",
            content = "- Leche\n- Huevos\n- Pan de molde\n- Café (MUCHO CAFÉ)",
            date = "Hoy, 09:30"
        ),
        Note(
            title = "Ideas TFG",
            content = "Hacer una app que simule un móvil hackeado. Se llamará Room 404. El logo tiene que ser el camello de Cocky Camel sí o sí.",
            date = "Ayer, 16:20"
        ),
        Note(
            title = "NO BORRAR",
            content = "El sistema está actuando raro desde que me bajé aquel archivo. A veces la pantalla parpadea. Si pasa algo, el código de reseteo me lo mandaron por correo.",
            date = "10 May, 23:15"
        ),
        Note(
            title = "Gimnasio - Rutina",
            content = "Lunes: Pecho y Tríceps\nMartes: Espalda y Bíceps\nMiércoles: Pierna (no saltarse el día de pierna por favor)",
            date = "01 May, 08:00"
        ),
        Note(
            title = "Contraseñas (Temporales)",
            content = "Wifi casa: cockycamel2026\nNetflix: (cambiada, pedir a mamá)",
            date = "15 Abr, 12:45"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notas", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A1A))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Toast.makeText(context, "Error de escritura: Almacenamiento corrupto", Toast.LENGTH_SHORT).show()
                },
                containerColor = Color(0xFF03A9F4),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir nota")
            }
        },
        containerColor = Color(0xFF121212)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            items(notes) { note ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1E1E1E))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = note.title,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = note.date,
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = note.content,
                            color = Color.LightGray,
                            fontSize = 14.sp,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) } // Espacio para que el botón flotante no tape la última nota
        }
    }
}