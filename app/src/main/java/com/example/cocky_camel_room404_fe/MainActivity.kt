package com.example.cocky_camel_room404_fe

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.cocky_camel_room404_fe.ui.theme.Room404Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        setContent {
            Room404Theme {
                var currentScreen by remember { mutableStateOf(0) }
                var appToUnlock by remember { mutableStateOf("") }
                var requiredPin by remember { mutableStateOf("") }
                val context = LocalContext.current

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    when (currentScreen) {
                        0 -> LoginScreen(onLoginSuccess = { currentScreen = 2 }, onNavigateToRegister = { currentScreen = 1 })
                        1 -> RegisterScreen(onRegisterSuccess = { currentScreen = 0 }, onNavigateToLogin = { currentScreen = 0 })
                        2 -> MainMenuScreen(onNewGame = { currentScreen = 3 }, onContinue = { currentScreen = 3 }, onSettings = { }, onRanking = { })
                        3 -> FakeOSScreen(onAppOpened = { appName ->
                            when (appName) {
                                "Sudoku" -> currentScreen = 4
                                "Galería" -> currentScreen = 5
                                "Mensajes" -> currentScreen = 8
                                "Notas" -> currentScreen = 10
                                "Calculadora" -> currentScreen = 11
                                "Calendario" -> currentScreen = 12
                                "Reloj" -> currentScreen = 13
                                "Música" -> currentScreen = 14
                                "Tiempo" -> currentScreen = 15
                                "Archivos" -> currentScreen = 16
                                "Maps" -> currentScreen = 17
                                "Teléfono" -> currentScreen = 18
                                "Cámara" -> currentScreen = 19
                                "Internet" -> currentScreen = 20
                                "Correo" -> { appToUnlock = "Correo"; requiredPin = "7429"; currentScreen = 6 }
                                "System Update" -> { appToUnlock = "System Update"; requiredPin = "0404"; currentScreen = 6 }
                                else -> Toast.makeText(context, "Abriendo $appName...", Toast.LENGTH_SHORT).show()
                            }
                        })
                        4 -> SudokuScreen(onBack = { currentScreen = 3 })
                        5 -> GalleryScreen(onBack = { currentScreen = 3 })
                        6 -> LockScreen(appName = appToUnlock, correctPin = requiredPin, onSuccess = {
                            currentScreen = if (appToUnlock == "Correo") 7 else 9
                        }, onBack = { currentScreen = 3 })
                        7 -> MailScreen(onBack = { currentScreen = 3 })
                        8 -> MessagesScreen(onBack = { currentScreen = 3 })
                        9 -> SystemUpdateScreen(onFinish = { currentScreen = 2 })
                        10 -> NotesScreen(onBack = { currentScreen = 3 })
                        11 -> CalculatorScreen(onBack = { currentScreen = 3 })
                        12 -> CalendarScreen(onBack = { currentScreen = 3 })
                        13 -> ClockScreen(onBack = { currentScreen = 3 })
                        14 -> MusicScreen(onBack = { currentScreen = 3 })
                        15 -> WeatherScreen(onBack = { currentScreen = 3 })
                        16 -> FilesScreen(onBack = { currentScreen = 3 })
                        17 -> MapsScreen(onBack = { currentScreen = 3 })
                        18 -> PhoneScreen(onBack = { currentScreen = 3 })
                        19 -> CameraScreen(onBack = { currentScreen = 3 })
                        20 -> InternetScreen(onBack = { currentScreen = 3 })
                    }
                }
            }
        }
    }
}