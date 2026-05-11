package com.example.cocky_camel_room404_fe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class CalendarEvent(val day: Int, val title: String, val time: String, val description: String)

class CalendarViewModel : ViewModel() {
    var selectedDay by mutableIntStateOf(22)
    
    var events by mutableStateOf<List<CalendarEvent>>(emptyList())
        private set

    fun loadEvents(initialEvents: List<CalendarEvent>) {
        if (events.isEmpty()) {
            events = initialEvents
        }
    }

    fun onDayClick(day: Int) {
        selectedDay = day
    }
}
