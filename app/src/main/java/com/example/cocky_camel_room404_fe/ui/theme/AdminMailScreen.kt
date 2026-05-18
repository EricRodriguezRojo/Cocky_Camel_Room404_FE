package com.example.cocky_camel_room404_fe.ui.theme

import android.widget.Toast
import androidx.compose.foundation.background
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
import com.example.cocky_camel_room404_fe.AdminMailViewModel
import com.example.cocky_camel_room404_fe.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMailScreen(
    onBack: () -> Unit,
    viewModel: AdminMailViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadEmails()
    }

    if (viewModel.showForbiddenError) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissForbiddenError() },
            icon = { Icon(Icons.Default.Security, contentDescription = null, tint = Color.Red) },
            title = { Text(stringResource(R.string.admin_security_error_title)) },
            text = { Text(stringResource(R.string.admin_security_error_text)) },
            confirmButton = {
                Button(onClick = { viewModel.dismissForbiddenError() }) {
                    Text(stringResource(R.string.admin_understood))
                }
            }
        )
    }

    if (viewModel.showDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDialog() },
            title = {
                Text(
                    if (viewModel.editingEmailId == null)
                        stringResource(R.string.admin_new_message)
                    else
                        stringResource(R.string.admin_edit_message)
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = viewModel.inputSender,
                        onValueChange = {
                            viewModel.inputSender = it
                            if (viewModel.senderError) viewModel.senderError = false
                        },
                        label = { Text(stringResource(R.string.admin_sender)) },
                        isError = viewModel.senderError,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = viewModel.inputBody,
                        onValueChange = {
                            viewModel.inputBody = it
                            if (viewModel.bodyError) viewModel.bodyError = false
                        },
                        label = { Text(stringResource(R.string.admin_message_body)) },
                        isError = viewModel.bodyError,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 150.dp),
                        maxLines = 10
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.saveEmail { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text(stringResource(R.string.admin_save))
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissDialog() }) {
                    Text(stringResource(R.string.admin_cancel))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.admin_panel_title), color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.admin_back_desc),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A1A))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openCreateDialog() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(R.string.admin_add_desc),
                    tint = Color.Black
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize().background(Color(0xFF121212))
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                items(viewModel.emails) { email ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${stringResource(R.string.admin_from)}: ${email.sender}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = email.bodyText,
                                    color = Color.LightGray,
                                    fontSize = 14.sp,
                                    maxLines = 2
                                )
                            }
                            Row {
                                IconButton(onClick = { viewModel.openEditDialog(email) }) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = stringResource(R.string.admin_edit_desc),
                                        tint = Color.White
                                    )
                                }
                                IconButton(onClick = { email.id?.let {
                                    viewModel.deleteEmail(it) { msg ->
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                    }
                                } }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = stringResource(R.string.admin_delete_desc),
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}