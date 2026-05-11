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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilesScreen(
    onBack: () -> Unit, 
    onPatchInstalled: () -> Unit,
    viewModel: FilesViewModel = viewModel()
) {
    val context = LocalContext.current

    val items = listOf(
        FileItem("Android", Icons.Default.Folder, true),
        FileItem(".sys_cache", Icons.Default.Folder, true),
        FileItem("gallery_fix_v2.apk", Icons.Default.Android, false, "1.2 MB"),
        FileItem("config_backup.txt", Icons.Default.Description, false, "12 KB"),
        FileItem("root_exploit.sh", Icons.Default.Code, false, "4 KB")
    )

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF121212))) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text(stringResource(R.string.files_internal_storage), color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back), tint = Color.White)
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
                                when (item.name) {
                                    "gallery_fix_v2.apk" -> {
                                        viewModel.startInstallation(context, onPatchInstalled)
                                    }
                                    ".sys_cache" -> {
                                        Toast.makeText(context, context.getString(R.string.files_access_denied), Toast.LENGTH_SHORT).show()
                                    }
                                    else -> {
                                        Toast.makeText(context, context.getString(R.string.files_corrupt), Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            item.icon,
                            contentDescription = null,
                            tint = if (item.name.contains("apk")) Color(0xFF4CAF50)
                            else if (item.name.startsWith(".")) Color.Red
                            else Color(0xFF03A9F4),
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
                    HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp)
                }
            }
        }

        if (viewModel.isInstalling) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        progress = { viewModel.installProgress },
                        color = Color(0xFF4CAF50),
                        strokeWidth = 4.dp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = stringResource(R.string.files_installing_patch),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${(viewModel.installProgress * 100).toInt()}%",
                        color = Color(0xFF4CAF50),
                        fontSize = 18.sp
                    )
                }
            }
            
            LaunchedEffect(viewModel.isInstalling) {
                if (!viewModel.isInstalling && viewModel.installProgress >= 0.99f) {
                    Toast.makeText(context, context.getString(R.string.files_gallery_success), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
