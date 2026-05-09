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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    onBack: () -> Unit,
    viewModel: NotesViewModel = viewModel()
) {
    val context = LocalContext.current

    val shoppingTitle = stringResource(R.string.note_shopping_title)
    val shoppingContent = stringResource(R.string.note_shopping_content)
    val shoppingDate = stringResource(R.string.mail_date_today_time)
    val tfgTitle = stringResource(R.string.note_tfg_title)
    val tfgContent = stringResource(R.string.note_tfg_content)
    val tfgDate = stringResource(R.string.mail_date_yesterday_time)
    val dontDeleteTitle = stringResource(R.string.note_dontdelete_title)
    val dontDeleteContent = stringResource(R.string.note_dontdelete_content)
    val gymTitle = stringResource(R.string.note_gym_title)
    val gymContent = stringResource(R.string.note_gym_content)
    val passwordsTitle = stringResource(R.string.note_passwords_title)
    val passwordsContent = stringResource(R.string.note_passwords_content)

    LaunchedEffect(Unit) {
        viewModel.loadNotes(
            shoppingTitle, shoppingContent, shoppingDate,
            tfgTitle, tfgContent, tfgDate,
            dontDeleteTitle, dontDeleteContent,
            gymTitle, gymContent,
            passwordsTitle, passwordsContent
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.notes_title), color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.back), tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A1A))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Toast.makeText(context, context.getString(R.string.notes_error_storage), Toast.LENGTH_SHORT).show()
                },
                containerColor = Color(0xFF03A9F4),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add))
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

            items(viewModel.notes) { note ->
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

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}
