package com.example.cocky_camel_room404_fe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

data class Email(
    val sender: String,
    val subject: String,
    val body: String,
    val date: String,
    val isRead: Boolean = true
)

class MailViewModel : ViewModel() {
    var dbEmails by mutableStateOf<List<Email>>(emptyList())
        private set
    
    var selectedEmail by mutableStateOf<Email?>(null)

    fun loadEmails(systemSubject: String, todayText: String) {
        viewModelScope.launch {
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

    fun selectEmail(email: Email?) {
        selectedEmail = email
    }
}
