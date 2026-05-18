package com.example.cocky_camel_room404_fe

import android.util.Patterns
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

    var senderError by mutableStateOf(false)
    var bodyError by mutableStateOf(false)

    var showForbiddenError by mutableStateOf(false)
        private set

    fun loadEmails() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getEmails()
                if (response.isSuccessful) {
                    emails = response.body() ?: emptyList()
                } else if (response.code() == 403) {
                    showForbiddenError = true
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
                } else if (response.code() == 403) {
                    showForbiddenError = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveEmail(onResult: (String) -> Unit) {
        senderError = false
        bodyError = false

        val trimmedSender = inputSender.trim()
        val trimmedBody = inputBody.trim()

        if (trimmedSender.isEmpty()) senderError = true
        if (trimmedBody.isEmpty()) bodyError = true

        if (senderError || bodyError) {
            onResult("Por favor, rellena todos los campos")
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedSender).matches()) {
            senderError = true
            onResult("El formato del remitente no es un correo válido")
            return
        }

        viewModelScope.launch {
            try {
                val response = if (editingEmailId == null) {
                    val newEmail = FakeEmailDto(sender = trimmedSender, bodyText = trimmedBody)
                    RetrofitClient.instance.createEmail(newEmail)
                } else {
                    val updatedEmail = FakeEmailDto(id = editingEmailId, sender = trimmedSender, bodyText = trimmedBody)
                    RetrofitClient.instance.updateEmail(editingEmailId!!, updatedEmail)
                }

                if (response.isSuccessful) {
                    loadEmails()
                    showDialog = false
                    onResult(if (editingEmailId == null) "Email creado" else "Email actualizado")
                } else if (response.code() == 403) {
                    showForbiddenError = true
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
        clearErrors()
        showDialog = true
    }

    fun openEditDialog(email: FakeEmailDto) {
        editingEmailId = email.id
        inputSender = email.sender
        inputBody = email.bodyText
        clearErrors()
        showDialog = true
    }

    fun dismissDialog() {
        showDialog = false
        clearErrors()
    }

    fun dismissForbiddenError() {
        showForbiddenError = false
    }

    private fun clearErrors() {
        senderError = false
        bodyError = false
    }
}