package com.example.cocky_camel_room404_fe

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MapsViewModel : ViewModel() {
    var hasLocationPermission by mutableStateOf(false)
        private set
    
    var currentLocation by mutableStateOf<Location?>(null)
        private set
    
    var hasCenteredCamera by mutableStateOf(false)
    
    var errorModeActive by mutableStateOf(false)
        private set

    fun updatePermissionStatus(context: Context) {
        hasLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun setPermissionGranted(granted: Boolean) {
        hasLocationPermission = granted
    }

    fun requestCurrentLocation(context: Context, fusedLocationClient: FusedLocationProviderClient) {
        if (!hasLocationPermission) return
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) return

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    currentLocation = location
                } else {
                    val tokenSource = CancellationTokenSource()
                    fusedLocationClient
                        .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, tokenSource.token)
                        .addOnSuccessListener { freshLocation ->
                            if (freshLocation != null) {
                                currentLocation = freshLocation
                            }
                        }
                }
            }
    }

    fun triggerErrorMode() {
        viewModelScope.launch {
            errorModeActive = true
            delay(2200)
            errorModeActive = false
        }
    }
}
