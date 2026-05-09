package com.example.cocky_camel_room404_fe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CalculatorViewModel : ViewModel() {
    var displayText by mutableStateOf("")
        private set
    
    var isGlitching by mutableStateOf(false)
        private set

    fun onButtonClick(
        btn: String,
        err404: String,
        errMalware: String,
        errCorrupted: String
    ) {
        if (isGlitching) return

        when (btn) {
            "AC" -> displayText = ""
            "del" -> if (displayText.isNotEmpty()) displayText = displayText.dropLast(1)
            "=" -> {
                if (displayText.isNotEmpty()) {
                    triggerGlitchSequence(err404, errMalware, errCorrupted)
                }
            }
            else -> {
                if (displayText.length < 15) {
                    displayText += btn
                }
            }
        }
    }

    private fun triggerGlitchSequence(
        err404: String,
        errMalware: String,
        errCorrupted: String
    ) {
        viewModelScope.launch {
            isGlitching = true
            val originalText = displayText
            displayText = err404
            delay(300)
            displayText = errMalware
            delay(300)
            displayText = errCorrupted
            delay(1000)
            displayText = originalText
            isGlitching = false
        }
    }
}
