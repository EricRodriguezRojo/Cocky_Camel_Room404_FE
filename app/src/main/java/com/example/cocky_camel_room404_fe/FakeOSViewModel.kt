package com.example.cocky_camel_room404_fe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class FakeOSViewModel : ViewModel() {
    var isGlitching by mutableStateOf(false)
        private set
    
    var showExitConfirm by mutableStateOf(false)
    var searchQuery by mutableStateOf("")

    init {
        startGlitchLoop()
    }

    private fun startGlitchLoop() {
        viewModelScope.launch {
            while (true) {
                delay(Random.nextLong(5000, 15000))
                isGlitching = true
                delay(Random.nextLong(300, 800))
                isGlitching = false
            }
        }
    }

    fun openExitConfirm() {
        showExitConfirm = true
    }

    fun dismissExitConfirm() {
        showExitConfirm = false
    }

    fun onSearch(onResult: (String) -> Unit) {
        if (searchQuery.isNotEmpty()) {
            onResult("Error de conexión de red")
        }
    }
}
