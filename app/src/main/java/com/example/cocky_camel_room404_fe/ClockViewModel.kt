package com.example.cocky_camel_room404_fe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class Alarm(val time: String, val label: String, val isEnabled: Boolean, val isSystemLocked: Boolean = false)

class ClockViewModel : ViewModel() {
    var alarms by mutableStateOf<List<Alarm>>(emptyList())
        private set
    
    var selectedTab by mutableIntStateOf(0)

    fun loadAlarms(initialAlarms: List<Alarm>) {
        if (alarms.isEmpty()) {
            alarms = initialAlarms
        }
    }

    fun toggleAlarm(time: String, isChecked: Boolean, onLocked: () -> Unit) {
        val alarm = alarms.find { it.time == time }
        if (alarm?.isSystemLocked == true) {
            onLocked()
        } else {
            alarms = alarms.map {
                if (it.time == time) it.copy(isEnabled = isChecked) else it
            }
        }
    }

    fun selectTab(index: Int) {
        selectedTab = index
    }
}
