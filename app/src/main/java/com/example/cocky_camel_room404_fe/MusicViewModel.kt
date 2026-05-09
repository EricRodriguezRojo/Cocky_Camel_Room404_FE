package com.example.cocky_camel_room404_fe

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MusicViewModel : ViewModel() {
    private var reproductor: MediaPlayer? = null
    
    var isPlaying by mutableStateOf(false)
        private set
    
    var sliderValue by mutableFloatStateOf(0f)
    var isDragging by mutableStateOf(false)
    var totalDuration by mutableFloatStateOf(0f)
        private set

    fun initMediaPlayer(context: Context, resId: Int) {
        if (reproductor == null) {
            reproductor = MediaPlayer.create(context, resId).apply {
                isLooping = true
                totalDuration = duration.toFloat()
            }
            startTrackingProgress()
        }
    }

    private fun startTrackingProgress() {
        viewModelScope.launch {
            while (isActive) {
                if (isPlaying && !isDragging) {
                    reproductor?.let {
                        sliderValue = it.currentPosition.toFloat()
                    }
                }
                delay(500L)
            }
        }
    }

    fun togglePlayPause() {
        reproductor?.let {
            if (it.isPlaying) {
                it.pause()
                isPlaying = false
            } else {
                it.start()
                isPlaying = true
            }
        }
    }

    fun seekTo(position: Float) {
        reproductor?.seekTo(position.toInt())
        sliderValue = position
    }

    fun skipForward() {
        reproductor?.let {
            var newPos = it.currentPosition + 10000
            if (newPos > it.duration) newPos = it.duration
            it.seekTo(newPos)
            sliderValue = newPos.toFloat()
        }
    }

    fun skipBackward() {
        reproductor?.let {
            var newPos = it.currentPosition - 10000
            if (newPos < 0) newPos = 0
            it.seekTo(newPos)
            sliderValue = newPos.toFloat()
        }
    }

    fun formatTime(ms: Long): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return java.lang.String.format("%d:%02d", minutes, seconds)
    }

    override fun onCleared() {
        super.onCleared()
        reproductor?.let {
            if (it.isPlaying) it.stop()
            it.release()
        }
        reproductor = null
    }
}
