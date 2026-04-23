package com.example.cocky_camel_room404_fe

object TimeTracker {
    private var startTime: Long = 0

    fun start() {
        if (startTime == 0L) {
            startTime = System.currentTimeMillis()
        }
    }

    fun getSecondsElapsedAndReset(): Int {
        if (startTime == 0L) return 0
        val now = System.currentTimeMillis()
        val seconds = ((now - startTime) / 1000).toInt()

        startTime = now

        return if (seconds > 0) seconds else 1
    }

    fun forceReset() {
        startTime = 0L
    }
}