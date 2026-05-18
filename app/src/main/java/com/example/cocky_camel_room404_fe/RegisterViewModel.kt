package com.example.cocky_camel_room404_fe

import android.content.Context
import android.util.Patterns
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    var nickname by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
        private set

    val isEmailValid by derivedStateOf {
        email.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    val isPasswordStrong by derivedStateOf {
        password.isEmpty() || password.length >= 6
    }

    val canSubmit by derivedStateOf {
        !isLoading &&
                (email.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(email).matches()) &&
                (password.isEmpty() || password.length >= 6)
    }

    fun onRegisterClick(context: Context, onRegisterSuccess: () -> Unit, onToast: (String) -> Unit) {
        if (nickname.isBlank() || email.isBlank() || password.isBlank()) {
            onToast(context.getString(R.string.register_required_fields))
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onToast(context.getString(R.string.register_invalid_email))
            return
        }
        if (password.length < 6) {
            onToast(context.getString(R.string.register_weak_password))
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