package com.example.cocky_camel_room404_fe

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource 
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

data class Email(
    val sender: String,
    val subject: String,
    val body: String,
    val date: String,
    val isRead: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MailScreen(onBack: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var dbEmails by remember { mutableStateOf<List<Email>>(emptyList()) }
    var selectedEmail by remember { mutableStateOf<Email?>(null) }

    val systemSubject = stringResource(R.string.mail_system_subject)
    val todayText = stringResource(R.string.mail_date_today)

    val staticEmails = listOf(
        Email("Netflix", stringResource(R.string.mail_netflix_subject), stringResource(R.string.mail_netflix_body), "12:30", true),
        Email("Amazon", stringResource(R.string.mail_amazon_subject), stringResource(R.string.mail_amazon_body), stringResource(R.string.mail_date_yesterday), true),
        Email(stringResource(R.string.mail_unknown_sender), stringResource(R.string.mail_error_start_subject), stringResource(R.string.mail_error_start_body), "10 May", false),
        Email("LinkedIn", stringResource(R.string.mail_linkedin_subject), stringResource(R.string.mail_linkedin_body), "08 May", true),
        Email("Steam", stringResource(R.string.mail_steam_subject), stringResource(R.string.mail_steam_body), "05 May", true)
    )

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val response = RetrofitClient.instance.getEmails()
                if (response.isSuccessful) {
                    val dtos = response.body() ?: emptyList()
                    dbEmails = dtos.map { dto ->
                        Email(
                            sender = dto.sender,
                            subject = systemSubject,
                            body = dto.bodyText,
                            date = todayText,
                            isRead = false
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val allEmails = staticEmails + dbEmails

    if (selectedEmail != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
        ) {
            TopAppBar(
                title = { Text("", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { selectedEmail = null }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.back), tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A1A))
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = selectedEmail!!.subject, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFF03A9F4)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Person, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = selectedEmail!!.sender, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        Text(text = stringResource(R.string.mail_to_me), color = Color.Gray, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = selectedEmail!!.date, color = Color.Gray, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = selectedEmail!!.body, color = Color.LightGray, fontSize = 16.sp, lineHeight = 24.sp)
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
        ) {
            TopAppBar(
                title = { Text(stringResource(R.string.mail_inbox_title), color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.back), tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A1A))
            )
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(allEmails) { email ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedEmail = email }
                            .padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFF03A9F4)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = email.sender.firstOrNull()?.toString() ?: "?", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = email.sender, color = if (!email.isRead) Color.White else Color.LightGray, fontWeight = if (!email.isRead) FontWeight.Bold else FontWeight.Normal, fontSize = 16.sp)
                                Text(text = email.date, color = if (!email.isRead) Color.White else Color.Gray, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = email.subject, color = if (!email.isRead) Color.White else Color.LightGray, fontWeight = if (!email.isRead) FontWeight.Bold else FontWeight.Normal, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = email.body, color = Color.Gray, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                    HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp)
                }
            }
        }
    }
}