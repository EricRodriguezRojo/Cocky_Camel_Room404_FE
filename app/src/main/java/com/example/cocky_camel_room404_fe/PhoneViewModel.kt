package com.example.cocky_camel_room404_fe

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PhoneViewModel : ViewModel() {
    var number by mutableStateOf("")
    var isCalling by mutableStateOf(false)
    var callStatus by mutableStateOf("")
    var typewriterText by mutableStateOf("")
    var showHackerText by mutableStateOf(false)

    fun addDigit(digit: String) {
        if (number.length < 15) {
            number += digit
        }
    }

    fun removeLastDigit() {
        if (number.isNotEmpty()) {
            number = number.dropLast(1)
        }
    }

    fun startCall(
        context: Context,
        encryptingStr: String,
        callingStr: String,
        noNumberStr: String,
        architectName: String,
        sysErrorStr: String,
        hackerMessage: String,
        onStartTono: () -> Unit,
        onStopTono: () -> Unit,
        onStartHackerAudio: () -> Unit
    ) {
        if (number.isEmpty()) return
        
        isCalling = true
        if (number == "6295") {
            callStatus = encryptingStr
            val segundosTardados = TimeTracker.getSecondsElapsedAndReset()

            viewModelScope.launch {
                try {
                    val token = SessionManager.getToken(context)
                    if (token != null) {
                        RetrofitClient.instance.completePuzzle(
                            token = "Bearer $token",
                            puzzleName = "The Architect",
                            body = mapOf("timeSeconds" to segundosTardados)
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            viewModelScope.launch {
                onStartTono()
                delay(2500)
                onStopTono()

                callStatus = architectName
                showHackerText = true
                onStartHackerAudio()

                hackerMessage.forEach { char ->
                    typewriterText += char
                    delay(65)
                }
            }
        } else {
            callStatus = callingStr
            viewModelScope.launch {
                delay(2000)
                if (number == "404" || number == "0404") {
                    callStatus = sysErrorStr
                    delay(2000)
                    isCalling = false
                } else {
                    callStatus = noNumberStr
                    delay(1500)
                    isCalling = false
                }
            }
        }
    }

    fun endCall(onStopAudio: () -> Unit) {
        isCalling = false
        onStopAudio()
        typewriterText = ""
        callStatus = ""
        showHackerText = false
    }
}
