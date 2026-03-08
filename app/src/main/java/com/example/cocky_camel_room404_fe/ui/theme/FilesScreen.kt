package com.example.cocky_camel_room404_fe

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class FileItem(val name: String, val icon: ImageVector, val isFolder: Boolean, val size: String = "")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val items = listOf(
        FileItem("Android", Icons.Default.Folder, true),
        FileItem("DCIM", Icons.Default.Folder, true),
        FileItem("Documents", Icons.Default.Folder, true),
        FileItem("Downloads", Icons.Default.Folder, true),
        FileItem("Pictures", Icons.Default.Folder, true),
        FileItem(".sys_cache", Icons.Default.Folder, true),
        FileItem("config_backup.txt", Icons.Default.Description, false, "12 KB"),
        FileItem("root_exploit.sh", Icons.Default.Code, false, "4 KB")
    )

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF121212))) {
        TopAppBar(
            title = { Text("Archivos", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A1A))
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (item.name == ".sys_cache") {
                                Toast.makeText(context, "ACCESO DENEGADO: Permisos de root requeridos", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Carpeta vacía o archivo corrupto", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        item.icon,
                        contentDescription = null,
                        tint = if (item.name.startsWith(".")) Color.Red else Color(0xFF03A9F4),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = item.name, color = Color.White, fontSize = 16.sp)
                        if (!item.isFolder) {
                            Text(text = item.size, color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }
                Divider(color = Color.DarkGray, thickness = 0.5.dp)
            }
        }
    }
}