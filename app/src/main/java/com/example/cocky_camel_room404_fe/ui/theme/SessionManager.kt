package com.example.cocky_camel_room404_fe

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREFS_NAME = "room404_prefs"
    private const val KEY_TOKEN = "jwt_token"
    private const val KEY_ROLE = "user_role"
    private const val KEY_NICKNAME = "user_nickname"
    private const val KEY_UNLOCKED_APPS = "unlocked_apps_"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(context: Context, token: String) {
        getPrefs(context).edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(context: Context): String? {
        return getPrefs(context).getString(KEY_TOKEN, null)
    }

    fun saveRole(context: Context, role: String) {
        getPrefs(context).edit().putString(KEY_ROLE, role).apply()
    }

    fun getRole(context: Context): String {
        return getPrefs(context).getString(KEY_ROLE, "User") ?: "User"
    }

    fun saveNickname(context: Context, nickname: String) {
        getPrefs(context).edit().putString(KEY_NICKNAME, nickname).apply()
    }

    fun getNickname(context: Context): String {
        return getPrefs(context).getString(KEY_NICKNAME, "User") ?: "User"
    }

    // Metodos para gestionar apps desbloqueadas
    fun saveUnlockedApp(context: Context, appName: String) {
        getPrefs(context).edit().putBoolean(KEY_UNLOCKED_APPS + appName, true).apply()
    }

    fun isAppUnlocked(context: Context, appName: String): Boolean {
        return getPrefs(context).getBoolean(KEY_UNLOCKED_APPS + appName, false)
    }

    fun resetUnlockedApps(context: Context) {
        val prefs = getPrefs(context)
        val keys = listOf("Correo", "Archivos", "System Update")
        prefs.edit().apply {
            keys.forEach { key ->
                remove(KEY_UNLOCKED_APPS + key)
            }
            apply()
        }
    }

    fun logout(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}