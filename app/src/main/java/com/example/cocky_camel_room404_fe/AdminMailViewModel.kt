package com.example.cocky_camel_room404_fe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AdminMailViewModel : ViewModel() {
    var emails by mutableStateOf<List<FakeEmailDto>>(emptyList())
        private set

    var showDialog by mutableStateOf(false)
    var editingEmailId by mutableStateOf<Int?>(null)
    var inputSender by mutableStateOf("")
    var inputBody by mutableStateOf("")

    fun loadEmails() {
        viewModelScope.launch {
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

    fun deleteEmail(id: Int, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.deleteEmail(id)
                if (response.isSuccessful) {
                    loadEmails()
                    onResult("Email eliminado")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveEmail(onResult: (String) -> Unit) {
        if (inputSender.isBlank() || inputBody.isBlank()) return

        viewModelScope.launch {
            try {
                val response = if (editingEmailId == null) {
                    val newEmail = FakeEmailDto(sender = inputSender, bodyText = inputBody)
                    RetrofitClient.instance.createEmail(newEmail)
                } else {
                    val updatedEmail = FakeEmailDto(id = editingEmailId, sender = inputSender, bodyText = inputBody)
                    RetrofitClient.instance.updateEmail(editingEmailId!!, updatedEmail)
                }

                if (response.isSuccessful) {
                    loadEmails()
                    showDialog = false
                    onResult(if (editingEmailId == null) "Email creado" else "Email actualizado")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun openCreateDialog() {
        editingEmailId = null
        inputSender = ""
        inputBody = ""
        showDialog = true
    }

    fun openEditDialog(email: FakeEmailDto) {
        editingEmailId = email.id
        inputSender = email.sender
        inputBody = email.bodyText
        showDialog = true
    }

    fun dismissDialog() {
        showDialog = false
    }
}
