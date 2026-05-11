package com.example.cocky_camel_room404_fe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SystemUpdateViewModel : ViewModel() {
    var gameStep by mutableStateOf(0)
    var flashWhite by mutableStateOf(false)
    
    private var shakeStartTime = 0L
    private var chargeStartTime = 0L

    fun initShakeTime() {
        shakeStartTime = System.currentTimeMillis()
    }

    fun onShakeDetected(onSaveProgress: (String, Int) -> Unit) {
        if (gameStep == 0) {
            val timeTaken = (System.currentTimeMillis() - shakeStartTime) / 1000
            onSaveProgress("SHAKE_SYSTEM", timeTaken.toInt())
            chargeStartTime = System.currentTimeMillis()
            gameStep = 1
        }
    }

    fun onPowerConnected(onSaveProgress: (String, Int) -> Unit) {
        if (gameStep == 1) {
            val timeTaken = (System.currentTimeMillis() - chargeStartTime) / 1000
            onSaveProgress("CHARGER_SYSTEM", timeTaken.toInt())
            gameStep = 2
            startUpdatingProcess()
        }
    }

    fun checkInitialCharging(isCharging: Boolean, onSaveProgress: (String, Int) -> Unit) {
        if (gameStep == 1 && isCharging) {
            viewModelScope.launch {
                delay(2000)
                val timeTaken = (System.currentTimeMillis() - chargeStartTime) / 1000
                onSaveProgress("CHARGER_SYSTEM", timeTaken.toInt())
                gameStep = 2
                startUpdatingProcess()
            }
        }
    }

    private fun startUpdatingProcess() {
        viewModelScope.launch {
            delay(4000)
            gameStep = 3
        }
    }

    fun startSuccessSequence(onFinish: () -> Unit) {
        viewModelScope.launch {
            flashWhite = true
            delay(100)
            flashWhite = false
            delay(100)
            flashWhite = true
            delay(500)
            flashWhite = false
            delay(3500)
            onFinish()
        }
    }
}
