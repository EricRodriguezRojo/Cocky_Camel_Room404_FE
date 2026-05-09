package com.example.cocky_camel_room404_fe

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    var nickname by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
        private set

    fun onRegisterClick(context: Context, onRegisterSuccess: () -> Unit, onToast: (String) -> Unit) {
        if (email.isBlank() || password.isBlank() || nickname.isBlank()) {
            onToast(context.getString(R.string.register_required_fields))
            return
        }

        viewModelScope.launch {
            isLoading = true
            try {
                val newUser = User(
                    email = email,
                    nickname = nickname,
                    password = password,
                    role = "User",
                    isPremium = true
                )
                val response = RetrofitClient.instance.register(newUser)

                if (response.isSuccessful) {
                    onToast(context.getString(R.string.register_success))
                    onRegisterSuccess()
                } else {
                    onToast(context.getString(R.string.register_email_exists))
                }
            } catch (e: Exception) {
                onToast("${context.getString(R.string.login_critical_error)}: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
}
