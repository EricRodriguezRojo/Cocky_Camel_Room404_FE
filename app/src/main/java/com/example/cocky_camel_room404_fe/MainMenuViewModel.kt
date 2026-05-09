package com.example.cocky_camel_room404_fe

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainMenuViewModel : ViewModel() {
    var isVisible by mutableStateOf(false)
        private set

    fun onAppear() {
        isVisible = true
    }

    fun logout(context: Context, onLogout: () -> Unit) {
        SessionManager.logout(context)
        onLogout()
    }
}
