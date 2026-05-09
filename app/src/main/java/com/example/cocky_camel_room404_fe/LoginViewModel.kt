package com.example.cocky_camel_room404_fe

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
        private set

    fun onLoginClick(context: Context, onLoginSuccess: () -> Unit, onToast: (String) -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            onToast(context.getString(R.string.login_missing_credentials))
            return
        }

        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.instance.login(email, password)
                if (response.isSuccessful) {
                    val loginData = response.body()
                    loginData?.token?.let { SessionManager.saveToken(context, it) }
                    val userRole = loginData?.role ?: "User"
                    SessionManager.saveRole(context, userRole)

                    try {
                        val userResp = RetrofitClient.instance.getUser(email)
                        if (userResp.isSuccessful) {
                            val user = userResp.body()
                            user?.nickname?.let { SessionManager.saveNickname(context, it) }
                        }
                    } catch (e: Exception) {}

                    onToast(loginData?.message ?: context.getString(R.string.login_connected))
                    onLoginSuccess()
                } else {
                    onToast(context.getString(R.string.login_invalid_credentials))
                }
            } catch (e: Exception) {
                onToast("${context.getString(R.string.login_critical_error)}: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun onGoogleLogin(context: Context, account: GoogleSignInAccount, onLoginSuccess: () -> Unit, onToast: (String) -> Unit) {
        val idToken = account.idToken ?: return
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.instance.googleLogin(mapOf("idToken" to idToken))
                if (response.isSuccessful) {
                    val loginData = response.body()
                    loginData?.token?.let { SessionManager.saveToken(context, it) }
                    val userRole = loginData?.role ?: "User"
                    SessionManager.saveRole(context, userRole)

                    try {
                        val userEmail = account.email
                        if (!userEmail.isNullOrBlank()) {
                            val userResp = RetrofitClient.instance.getUser(userEmail)
                            if (userResp.isSuccessful) {
                                val user = userResp.body()
                                user?.nickname?.let { SessionManager.saveNickname(context, it) }
                            }
                        }
                    } catch (e: Exception) {}

                    onToast(loginData?.message ?: context.getString(R.string.login_access_granted))
                    onLoginSuccess()
                } else {
                    onToast(context.getString(R.string.login_google_error))
                }
            } catch (e: Exception) {
                onToast("${context.getString(R.string.login_network_error)}: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
}
