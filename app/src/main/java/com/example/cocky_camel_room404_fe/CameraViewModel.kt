package com.example.cocky_camel_room404_fe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CameraViewModel : ViewModel() {
    var isError by mutableStateOf(false)
        private set

    fun triggerError() {
        if (isError) return
        viewModelScope.launch {
            isError = true
            delay(1500)
            isError = false
        }
    }
}
