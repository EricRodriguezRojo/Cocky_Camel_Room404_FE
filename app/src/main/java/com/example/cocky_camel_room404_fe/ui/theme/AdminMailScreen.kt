package com.example.cocky_camel_room404_fe

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMailScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var emails by remember { mutableStateOf<List<FakeEmailDto>>(emptyList()) }

    var showDialog by remember { mutableStateOf(false) }
    var editingEmailId by remember { mutableStateOf<Int?>(null) }
    var inputSender by remember { mutableStateOf("") }
    var inputBody by remember { mutableStateOf("") }

    val msgDeleted = stringResource(R.string.email_deleted)
    val msgCreated = stringResource(R.string.email_created)
    val msgUpdated = stringResource(R.string.email_updated)

    fun loadEmails() {
        coroutineScope.launch {
            try {
                val response = RetrofitClient.instance.getEmails()
                if (response.isSuccessful) {
                    emails = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteEmail(id: Int) {
        coroutineScope.launch {
            try {
                val response = RetrofitClient.instance.deleteEmail(id)
                if (response.isSuccessful) {
                    loadEmails()
                    Toast.makeText(context, msgDeleted, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun createEmail(sender: String, body: String) {
        coroutineScope.launch {
            try {
                val newEmail = FakeEmailDto(sender = sender, bodyText = body)
                val response = RetrofitClient.instance.createEmail(newEmail)
                if (response.isSuccessful) {
                    loadEmails()
                    showDialog = false
                    Toast.makeText(context, msgCreated, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateEmail(id: Int, sender: String, body: String) {
        coroutineScope.launch {
            try {
                val updatedEmail = FakeEmailDto(id = id, sender = sender, bodyText = body)
                val response = RetrofitClient.instance.updateEmail(id, updatedEmail)
                if (response.isSuccessful) {
                    loadEmails()
                    showDialog = false
                    Toast.makeText(context, msgUpdated, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    LaunchedEffect(Unit) {
        loadEmails()
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(if (editingEmailId == null) stringResource(R.string.new_email) else stringResource(R.string.edit_email))
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = inputSender,
                        onValueChange = { inputSender = it },
                        label = { Text(stringResource(R.string.sender)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = inputBody,
                        onValueChange = { inputBody = it },
                        label = { Text(stringResource(R.string.message_content)) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (inputSender.isNotBlank() && inputBody.isNotBlank()) {
                        if (editingEmailId == null) {
                            createEmail(inputSender, inputBody)
                        } else {
                            updateEmail(editingEmailId!!, inputSender, inputBody)
                        }
                    }
                }) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.admin_panel_emails), color = Color.White) },
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
                    editingEmailId = null
                    inputSender = ""
                    inputBody = ""
                    showDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_email), tint = Color.Black)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFF121212))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(emails) { email ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${stringResource(R.string.from)}: ${email.sender}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = email.bodyText, color = Color.LightGray, fontSize = 14.sp)
                            }
                            Row {
                                IconButton(onClick = {
                                    editingEmailId = email.id
                                    inputSender = email.sender
                                    inputBody = email.bodyText
                                    showDialog = true
                                }) {
                                    Icon(Icons.Filled.Edit, contentDescription = stringResource(R.string.edit), tint = Color.White)
                                }
                                IconButton(onClick = { email.id?.let { deleteEmail(it) } }) {
                                    Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.delete), tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}