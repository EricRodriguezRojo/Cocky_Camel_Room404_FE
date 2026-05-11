package com.example.cocky_camel_room404_fe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class TerminalLine(val text: String, val color: Color)

class SystemIntroViewModel : ViewModel() {
    var visibleLines by mutableStateOf(emptyList<TerminalLine>())
        private set
    var currentLineText by mutableStateOf("")
        private set
    var currentIndex by mutableIntStateOf(0)
        private set
    var isCursorVisible by mutableStateOf(true)
        private set
    var sequenceFinished by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            while (true) {
                isCursorVisible = !isCursorVisible
                delay(500)
            }
        }
    }

    fun startSequence(lines: List<TerminalLine>, onFinished: () -> Unit) {
        if (currentIndex > 0 || visibleLines.isNotEmpty()) return

        viewModelScope.launch {
            for (i in lines.indices) {
                val fullText = lines[i].text
                currentIndex = i
                currentLineText = ""

                for (char in fullText) {
                    currentLineText += char
                    delay(30)
                }

                visibleLines = visibleLines + lines[i].copy(text = currentLineText)
                currentLineText = ""
                delay(500)
            }
            currentIndex = lines.size
            sequenceFinished = true
            delay(1500)
            onFinished()
        }
    }
}
