package com.example.cocky_camel_room404_fe

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class FileItem(val name: String, val icon: ImageVector, val isFolder: Boolean, val size: String = "")

class FilesViewModel : ViewModel() {
    var isInstalling by mutableStateOf(false)
        private set
    var installProgress by mutableFloatStateOf(0f)
        private set

    fun startInstallation(context: Context, onPatchInstalled: () -> Unit) {
        if (isInstalling) return
        isInstalling = true
        installProgress = 0f

        viewModelScope.launch {
            while (installProgress < 1f) {
                delay(50)
                installProgress += 0.02f
            }

            val segundos = TimeTracker.getSecondsElapsedAndReset()
            try {
                val token = SessionManager.getToken(context)
                if (token != null) {
                    RetrofitClient.instance.completePuzzle(
                        token = "Bearer $token",
                        puzzleName = "Gallery Patch",
                        body = mapOf("timeSeconds" to segundos)
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            onPatchInstalled()
            isInstalling = false
        }
    }
}
