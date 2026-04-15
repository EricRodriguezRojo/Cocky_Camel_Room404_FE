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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cocky_camel_room404_fe.ui.theme.Room404Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        setContent {
            Room404Theme {
                val navController = rememberNavController()
                var appToUnlock by remember { mutableStateOf("") }
                var requiredPin by remember { mutableStateOf("") }
                val context = LocalContext.current

                val startDestination = remember {
                    if (SessionManager.getToken(context) != null) "main_menu" else "login"
                }

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = {
                                    navController.navigate("main_menu") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToRegister = { navController.navigate("register") }
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                onRegisterSuccess = { navController.navigate("login") },
                                onNavigateToLogin = { navController.navigate("login") }
                            )
                        }

                        composable("main_menu") {
                            MainMenuScreen(
                                onNewGame = { navController.navigate("fake_os") },
                                onContinue = { navController.navigate("fake_os") },
                                onSettings = { },
                                onRanking = { },
                                onLogout = {
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("fake_os") {
                            FakeOSScreen(onAppOpened = { appName ->
                                when (appName) {
                                    "Sudoku" -> navController.navigate("sudoku")
                                    "Galería" -> navController.navigate("gallery")
                                    "Mensajes" -> navController.navigate("messages")
                                    "Notas" -> navController.navigate("notes")
                                    "Calculadora" -> navController.navigate("calculator")
                                    "Calendario" -> navController.navigate("calendar")
                                    "Reloj" -> navController.navigate("clock")
                                    "Música" -> navController.navigate("music")
                                    "Tiempo" -> navController.navigate("weather")
                                    "Archivos" -> navController.navigate("files")
                                    "Maps" -> navController.navigate("maps")
                                    "Teléfono" -> navController.navigate("phone")
                                    "Cámara" -> navController.navigate("camera")
                                    "Internet" -> navController.navigate("internet")
                                    "Play Store" -> navController.navigate("play_store")
                                    "Correo" -> {
                                        appToUnlock = "Correo"
                                        requiredPin = "7429"
                                        navController.navigate("lock_screen")
                                    }
                                    "System Update" -> {
                                        appToUnlock = "System Update"
                                        requiredPin = "0404"
                                        navController.navigate("lock_screen")
                                    }
                                    "EXIT" -> navController.navigate("main_menu")
                                    else -> Toast.makeText(context, "Abriendo $appName...", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                        composable("sudoku") { SudokuScreen(onBack = { navController.popBackStack() }) }
                        composable("gallery") { GalleryScreen(onBack = { navController.popBackStack() }) }
                        composable("lock_screen") {
                            LockScreen(
                                appName = appToUnlock,
                                correctPin = requiredPin,
                                onSuccess = {
                                    if (appToUnlock == "Correo") navController.navigate("mail") {
                                        popUpTo("fake_os")
                                    } else navController.navigate("system_update") {
                                        popUpTo("fake_os")
                                    }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("mail") { MailScreen(onBack = { navController.popBackStack() }) }
                        composable("messages") { MessagesScreen(onBack = { navController.popBackStack() }) }
                        composable("system_update") { SystemUpdateScreen(onFinish = { navController.navigate("main_menu") { popUpTo(0) } }) }
                        composable("notes") { NotesScreen(onBack = { navController.popBackStack() }) }
                        composable("calculator") { CalculatorScreen(onBack = { navController.popBackStack() }) }
                        composable("calendar") { CalendarScreen(onBack = { navController.popBackStack() }) }
                        composable("clock") { ClockScreen(onBack = { navController.popBackStack() }) }
                        composable("music") { MusicScreen(onBack = { navController.popBackStack() }) }
                        composable("weather") { WeatherScreen(onBack = { navController.popBackStack() }) }
                        composable("files") { FilesScreen(onBack = { navController.popBackStack() }) }
                        composable("maps") { MapsScreen(onBack = { navController.popBackStack() }) }
                        composable("phone") { PhoneScreen(onBack = { navController.popBackStack() }) }
                        composable("camera") { CameraScreen(onBack = { navController.popBackStack() }) }
                        composable("internet") { InternetScreen(onBack = { navController.popBackStack() }) }
                        composable("play_store") { PlayStoreScreen(onBack = { navController.popBackStack() }) }
                    }
                }
            }
        }
    }
}