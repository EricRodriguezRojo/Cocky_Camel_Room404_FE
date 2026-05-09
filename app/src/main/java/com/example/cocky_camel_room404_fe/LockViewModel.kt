package com.example.cocky_camel_room404_fe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LockViewModel : ViewModel() {
    var enteredPin by mutableStateOf("")
        private set
    
    var isError by mutableStateOf(false)
        private set

    fun onKeyClick(
        key: String,
        correctPin: String,
        onVibrate: () -> Unit,
        onSuccess: () -> Unit
    ) {
        if (isError) return

        if (key == "del") {
            if (enteredPin.isNotEmpty()) {
                enteredPin = enteredPin.dropLast(1)
            }
        } else {
            if (enteredPin.length < 4) {
                enteredPin += key
                if (enteredPin.length == 4) {
                    if (enteredPin == correctPin) {
                        onSuccess()
                    } else {
                        handleError(onVibrate)
                    }
                }
            }
        }
    }

    private fun handleError(onVibrate: () -> Unit) {
        isError = true
        onVibrate()
        viewModelScope.launch {
            delay(400)
            enteredPin = ""
            isError = false
        }
    }
}
