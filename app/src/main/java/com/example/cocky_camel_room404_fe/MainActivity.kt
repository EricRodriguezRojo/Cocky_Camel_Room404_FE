package com.example.cocky_camel_room404_fe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.cocky_camel_room404_fe.ui.theme.Room404Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Room404Theme {
                // 0: Login, 1: Register, 2: Menu
                var currentScreen by remember { mutableStateOf(0) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (currentScreen) {
                        0 -> LoginScreen(
                            onLoginSuccess = { currentScreen = 2 },
                            onNavigateToRegister = { currentScreen = 1 }
                        )
                        1 -> RegisterScreen(
                            onRegisterSuccess = { currentScreen = 0 },
                            onNavigateToLogin = { currentScreen = 0 }
                        )
                    }
                }
            }
        }
    }
}