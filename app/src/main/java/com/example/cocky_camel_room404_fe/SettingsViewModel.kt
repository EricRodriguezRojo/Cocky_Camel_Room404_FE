package com.example.cocky_camel_room404_fe

import android.content.Context
import android.media.AudioManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    var currentVolume by mutableFloatStateOf(0f)
    var maxVolume by mutableFloatStateOf(0f)
        private set

    fun initVolume(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
    }

    fun setVolume(context: Context, volume: Float) {
        currentVolume = volume
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume.toInt(), 0)
    }

    fun setLanguage(langCode: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(langCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}
