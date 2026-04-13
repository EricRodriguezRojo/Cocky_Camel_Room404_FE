package com.example.cocky_camel_room404_fe

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREFS_NAME = "room404_prefs"
    private const val KEY_TOKEN = "jwt_token"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(context: Context, token: String) {
        getPrefs(context).edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(context: Context): String? {
        return getPrefs(context).getString(KEY_TOKEN, null)
    }

    fun logout(context: Context) {
        getPrefs(context).edit().remove(KEY_TOKEN).apply()
    }
}