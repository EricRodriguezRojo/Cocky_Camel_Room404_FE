package com.example.cocky_camel_room404_fe

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREFS_NAME = "room404_prefs"
    private const val KEY_TOKEN = "jwt_token"
    private const val KEY_ROLE = "user_role"
    private const val KEY_NICKNAME = "user_nickname"
    private const val KEY_UNLOCKED_APPS = "unlocked_apps_"

    private const val KEY_GALLERY_PATCHED = "gallery_patched"
    private const val KEY_INTRO_SEEN = "intro_seen"
    private const val KEY_RESET_EMAIL = "reset_email"

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

    fun saveNickname(context: Context, nickname: String) {
        getPrefs(context).edit().putString(KEY_NICKNAME, nickname).apply()
    }

    fun getNickname(context: Context): String? {
        return getPrefs(context).getString(KEY_NICKNAME, null)
    }

    fun getRole(context: Context): String {
        return getPrefs(context).getString(KEY_ROLE, "User") ?: "User"
    }

    fun saveUnlockedApp(context: Context, appName: String) {
        getPrefs(context).edit().putBoolean(KEY_UNLOCKED_APPS + appName, true).apply()
    }

    fun isAppUnlocked(context: Context, appName: String): Boolean {
        return getPrefs(context).getBoolean(KEY_UNLOCKED_APPS + appName, false)
    }

    fun saveGalleryPatched(context: Context, isPatched: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_GALLERY_PATCHED, isPatched).apply()
    }

    fun isGalleryPatched(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_GALLERY_PATCHED, false)
    }

    fun setIntroSeen(context: Context, seen: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_INTRO_SEEN, seen).apply()
    }

    fun isIntroSeen(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_INTRO_SEEN, false)
    }

    fun resetUnlockedApps(context: Context) {
        val prefs = getPrefs(context)
        val keys = listOf("Correo", "Archivos", "System Update")
        prefs.edit().apply {
            keys.forEach { key ->
                remove(KEY_UNLOCKED_APPS + key)
            }
            remove(KEY_GALLERY_PATCHED)
            remove(KEY_INTRO_SEEN)
            apply()
        }
    }

    fun logout(context: Context) {
        getPrefs(context).edit().clear().apply()
    }

    fun saveResetEmail(context: Context, email: String) {
        getPrefs(context).edit().putString(KEY_RESET_EMAIL, email).apply()
    }

    fun getResetEmail(context: Context): String? {
        return getPrefs(context).getString(KEY_RESET_EMAIL, null)
    }

    fun clearResetEmail(context: Context) {
        getPrefs(context).edit().remove(KEY_RESET_EMAIL).apply()
    }
}