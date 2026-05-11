package com.example.cocky_camel_room404_fe

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Chat(val id: String, val name: String, val lastMessage: String, val time: String)
data class Message(val text: String, val isFromMe: Boolean)

class MessagesViewModel : ViewModel() {
    var currentChat by mutableStateOf<Chat?>(null)
        private set
    
    var chatMessages by mutableStateOf(listOf<Message>())
        private set
    
    var inputText by mutableStateOf("")

    var isGlitching by mutableStateOf(false)
        private set
    
    var showGlitchOverlay by mutableStateOf(false)
        private set
    
    var showSystemMessage by mutableStateOf(false)
        private set

    fun selectChat(chat: Chat?, unknownMsgs: List<String>) {
        currentChat = chat
        if (chat == null) {
            chatMessages = emptyList()
        } else if (chat.id == "1") {
            chatMessages = unknownMsgs.map { Message(it, false) }
        } else {
            chatMessages = listOf(Message(chat.lastMessage, false))
        }
    }

    fun sendMessage(context: Context) {
        if (inputText.isBlank()) return
        
        val textSent = inputText.trim()
        chatMessages = chatMessages + Message(textSent, true)
        inputText = ""

        if (currentChat?.id == "1" && textSent.equals("malware", ignoreCase = true)) {
            handleMalwareTrigger(context)
        }
    }

    private fun handleMalwareTrigger(context: Context) {
        val segundosTardados = TimeTracker.getSecondsElapsedAndReset()

        viewModelScope.launch {
            try {
                val token = SessionManager.getToken(context)
                if (token != null) {
                    RetrofitClient.instance.triggerMalware("Bearer $token")
                    RetrofitClient.instance.completePuzzle(
                        token = "Bearer $token",
                        puzzleName = "Malware Enigma",
                        body = mapOf("timeSeconds" to segundosTardados.toInt())
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        viewModelScope.launch {
            isGlitching = true
            showGlitchOverlay = true
            delay(800)
            isGlitching = false
            showGlitchOverlay = false

            showSystemMessage = true
            delay(5000)
            showSystemMessage = false
        }
    }
}
