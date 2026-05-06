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
import androidx.lifecycle.Lifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cocky_camel_room404_fe.ui.theme.Room404Theme
import kotlinx.coroutines.launch

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
                val  navController = rememberNavController()
                var appToUnlock by remember { mutableStateOf("") }
                var requiredPin by remember { mutableStateOf("") }
                var isGalleryPatched by remember { mutableStateOf(false) }

                val context = LocalContext.current
                val scope = rememberCoroutineScope()

                val startDestination = remember {
                    if (SessionManager.getToken(context) != null) "main_menu" else "login"
                }

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("login") { LoginScreen(onLoginSuccess = { navController.navigate("main_menu") { popUpTo("login") { inclusive = true } } }, onNavigateToRegister = { navController.navigate("register") }) }
                        composable("register") { RegisterScreen(onRegisterSuccess = { navController.navigate("login") }, onNavigateToLogin = { navController.navigate("login") }) }

                        composable("main_menu") {
                            MainMenuScreen(
                                onNewGame = { 
                                    SessionManager.resetUnlockedApps(context)
                                    TimeTracker.forceReset()
                                    navController.navigate("system_intro") 
                                },
                                onContinue = { TimeTracker.start(); navController.navigate("fake_os") },
                                onSettings = { },
                                onRanking = { navController.navigate("ranking") },
                                onLogout = { SessionManager.logout(context); navController.navigate("login") { popUpTo(0) { inclusive = true } } }
                            )
                        }

                        composable("system_intro") {
                            SystemIntroScreen(onFinished = {
                                TimeTracker.start()
                                navController.navigate("fake_os") {
                                    popUpTo("system_intro") { inclusive = true }
                                }
                            })
                        }

                        composable("ranking") { RankingScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }

                        composable("fake_os") {
                            FakeOSScreen(onAppOpened = { appName ->
                                if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                                    when (appName) {
                                        "Archivos" -> {
                                            if (SessionManager.isAppUnlocked(context, "Archivos")) {
                                                navController.navigate("files") { launchSingleTop = true }
                                            } else {
                                                appToUnlock = "Archivos"
                                                requiredPin = "0024"
                                                navController.navigate("lock_screen") { launchSingleTop = true }
                                            }
                                        }
                                        "Galería" -> {
                                            if (isGalleryPatched) navController.navigate("gallery") { launchSingleTop = true }
                                            else Toast.makeText(context, "ERROR: App corrupta. Reinstale vía APK.", Toast.LENGTH_LONG).show()
                                        }
                                        "Sudoku" -> navController.navigate("sudoku") { launchSingleTop = true }
                                        "Mensajes" -> navController.navigate("messages") { launchSingleTop = true }
                                        "Notas" -> navController.navigate("notes") { launchSingleTop = true }
                                        "Calculadora" -> navController.navigate("calculator") { launchSingleTop = true }
                                        "Calendario" -> navController.navigate("calendar") { launchSingleTop = true }
                                        "Reloj" -> navController.navigate("clock") { launchSingleTop = true }
                                        "Música" -> navController.navigate("music") { launchSingleTop = true }
                                        "Tiempo" -> navController.navigate("weather") { launchSingleTop = true }
                                        "Maps" -> navController.navigate("maps") { launchSingleTop = true }
                                        "Teléfono" -> navController.navigate("phone") { launchSingleTop = true }
                                        "Cámara" -> navController.navigate("camera") { launchSingleTop = true }
                                        "Internet" -> navController.navigate("internet") { launchSingleTop = true }
                                        "Play Store" -> navController.navigate("play_store") { launchSingleTop = true }
                                        "Correo" -> {
                                            if (SessionManager.isAppUnlocked(context, "Correo")) {
                                                if (SessionManager.getRole(context) == "Admin") navController.navigate("admin_mail") { launchSingleTop = true }
                                                else navController.navigate("mail") { launchSingleTop = true }
                                            } else {
                                                appToUnlock = "Correo"
                                                requiredPin = "7429"
                                                navController.navigate("lock_screen") { launchSingleTop = true }
                                            }
                                        }
                                        "System Update" -> {
                                            if (SessionManager.isAppUnlocked(context, "System Update")) {
                                                navController.navigate("system_update") { launchSingleTop = true }
                                            } else {
                                                appToUnlock = "System Update"
                                                requiredPin = "0404"
                                                navController.navigate("lock_screen") { launchSingleTop = true }
                                            }
                                        }
                                        "EXIT" -> navController.navigate("main_menu") { popUpTo("fake_os") { inclusive = true } }
                                        else -> Toast.makeText(context, "Abriendo $appName...", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            })
                        }

                        composable("lock_screen") {
                            LockScreen(
                                appName = appToUnlock,
                                correctPin = requiredPin,
                                onSuccess = {
                                    val segundos = TimeTracker.getSecondsElapsedAndReset()
                                    scope.launch {
                                        try {
                                            val token = SessionManager.getToken(context)
                                            if (token != null) {
                                                val pName = when(appToUnlock) {
                                                    "Correo" -> "Mail Access"
                                                    "Archivos" -> "System Breach"
                                                    else -> "System Override"
                                                }
                                                RetrofitClient.instance.completePuzzle(token = "Bearer $token", puzzleName = pName, body = mapOf("timeSeconds" to segundos))
                                            }
                                        } catch (e: Exception) {}
                                    }

                                    when (appToUnlock) {
                                        "Correo" -> {
                                            if (SessionManager.getRole(context) == "Admin") navController.navigate("admin_mail") { popUpTo("fake_os") }
                                            else navController.navigate("mail") { popUpTo("fake_os") }
                                        }
                                        "Archivos" -> navController.navigate("files") { popUpTo("fake_os") }
                                        else -> navController.navigate("system_update") { popUpTo("fake_os") }
                                    }
                                },
                                onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() },
                                context = context
                            )
                        }

                        composable("files") { FilesScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }, onPatchInstalled = { isGalleryPatched = true }) }
                        composable("sudoku") { SudokuScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }
                        composable("gallery") { GalleryScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }
                        composable("mail") { MailScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }
                        composable("admin_mail") { AdminMailScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }
                        composable("messages") { MessagesScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }
                        composable("system_update") { SystemUpdateScreen(onFinish = { navController.navigate("main_menu") { popUpTo(0) } }) }
                        composable("notes") { NotesScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }
                        composable("calculator") { CalculatorScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }
                        composable("calendar") { CalendarScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }
                        composable("clock") { ClockScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }
                        composable("music") { MusicScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }
                        composable("weather") { WeatherScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }
                        composable("maps") { MapsScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }
                        composable("phone") { PhoneScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }
                        composable("camera") { CameraScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }
                        composable("internet") { InternetScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }
                        composable("play_store") { PlayStoreScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }
                    }
                }
            }
        }
    }
}