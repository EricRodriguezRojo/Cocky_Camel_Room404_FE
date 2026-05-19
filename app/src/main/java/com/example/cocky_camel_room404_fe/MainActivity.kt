package com.example.cocky_camel_room404_fe

import android.os.Bundle
import android.widget.Toast
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
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
import com.example.cocky_camel_room404_fe.ui.theme.AdminMailScreen
import com.example.cocky_camel_room404_fe.ui.theme.CalendarScreen
import com.example.cocky_camel_room404_fe.ui.theme.Room404Theme
import kotlinx.coroutines.launch
import com.example.cocky_camel_room404_fe.ui.theme.SystemIntroScreen


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        var startDestination = if (SessionManager.getToken(this) != null) "main_menu" else "login"
        var resetToken: String? = null
        intent?.data?.let { uri ->
            val queryToken = uri.getQueryParameter("token")
            val pathToken = uri.lastPathSegment?.takeIf { it.isNotBlank() && it != uri.host }
            resetToken = queryToken ?: pathToken
            if (!resetToken.isNullOrBlank()) {
                SessionManager.saveResetEmail(this, "")
                startDestination = "enter_token"
            }
        }

        setContent {
            Room404Theme {
                val context = LocalContext.current

                val navController = rememberNavController()
                var appToUnlock by remember { mutableStateOf("") }
                var requiredPin by remember { mutableStateOf("") }
                var recoveryEmail by remember { mutableStateOf("") }

                var isGalleryPatched by remember { mutableStateOf(SessionManager.isGalleryPatched(context)) }

                val scope = rememberCoroutineScope()

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("login") { LoginScreen(onLoginSuccess = { navController.navigate("main_menu") { popUpTo("login") { inclusive = true } } }, onNavigateToRegister = { navController.navigate("register") }, onNavigateToForgotPassword = { navController.navigate("forgot_password") }) }
                        composable("register") { RegisterScreen(onRegisterSuccess = { navController.navigate("login") }, onNavigateToLogin = { navController.navigate("login") }) }
                        composable("forgot_password") {
                            ForgotPasswordScreen(
                                onNavigateToLogin = { navController.popBackStack() },
                                onNavigateToToken = { email ->
                                    recoveryEmail = email
                                    navController.navigate("enter_token")
                                }
                            )
                        }

                        composable("enter_token") { backStackEntry ->
                            var prefilledToken by remember { mutableStateOf("") }
                            LaunchedEffect(Unit) {
                                if (prefilledToken.isEmpty() && resetToken != null) {
                                    prefilledToken = resetToken!!
                                    resetToken = null
                                }
                            }
                            TokenEntryScreen(
                                prefilledToken = prefilledToken,
                                recoveryEmail = recoveryEmail,
                                onVerified = { token -> navController.navigate("reset_password/${Uri.encode(token)}") },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable("reset_password/{token}") { backStackEntry ->
                            val token = backStackEntry.arguments?.getString("token")
                            ResetPasswordScreen(
                                token = token,
                                recoveryEmail = recoveryEmail,
                                onResetSuccess = {
                                    recoveryEmail = ""
                                    navController.navigate("login") {
                                        popUpTo("reset_password/{token}") { inclusive = true }
                                    }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable("main_menu") {
                            MainMenuScreen(
                                onNewGame = {
                                    SessionManager.resetUnlockedApps(context)
                                    TimeTracker.forceReset()
                                    navController.navigate("system_intro")
                                },
                                onContinue = {
                                    TimeTracker.start()
                                    if (SessionManager.isIntroSeen(context)) {
                                        navController.navigate("fake_os")
                                    } else {
                                        navController.navigate("system_intro")
                                    }
                                },
                                onSettings = { navController.navigate("settings") },
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
                            }, onSkip = {
                                TimeTracker.start()
                                navController.navigate("fake_os") {
                                    popUpTo("system_intro") { inclusive = true }
                                }
                            })
                        }

                        composable("ranking") { RankingScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }

                        composable("fake_os") {
                            FakeOSScreen(onAppOpened = { appResId ->
                                if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                                    when (appResId) {
                                        R.string.app_files -> {
                                            if (SessionManager.isAppUnlocked(context, "Archivos")) {
                                                navController.navigate("files") { launchSingleTop = true }
                                            } else {
                                                appToUnlock = "Archivos"
                                                requiredPin = "0024"
                                                navController.navigate("lock_screen") { launchSingleTop = true }
                                            }
                                        }
                                        R.string.app_gallery -> {
                                            if (isGalleryPatched) navController.navigate("gallery") { launchSingleTop = true }
                                            else Toast.makeText(context, "ERROR: App corrupta. Reinstale vía APK.", Toast.LENGTH_LONG).show()
                                        }
                                        R.string.app_settings -> navController.navigate("system_settings") { launchSingleTop = true }
                                        R.string.app_sudoku -> navController.navigate("sudoku") { launchSingleTop = true }
                                        R.string.app_messages -> navController.navigate("messages") { launchSingleTop = true }
                                        R.string.app_notes -> navController.navigate("notes") { launchSingleTop = true }
                                        R.string.app_calculator -> navController.navigate("calculator") { launchSingleTop = true }
                                        R.string.app_calendar -> navController.navigate("calendar") { launchSingleTop = true }
                                        R.string.app_clock -> navController.navigate("clock") { launchSingleTop = true }
                                        R.string.app_music -> navController.navigate("music") { launchSingleTop = true }
                                        R.string.app_weather -> navController.navigate("weather") { launchSingleTop = true }
                                        R.string.app_maps -> navController.navigate("maps") { launchSingleTop = true }
                                        R.string.app_phone -> navController.navigate("phone") { launchSingleTop = true }
                                        R.string.app_camera -> navController.navigate("camera") { launchSingleTop = true }
                                        R.string.app_internet -> navController.navigate("internet") { launchSingleTop = true }
                                        R.string.app_playstore -> navController.navigate("play_store") { launchSingleTop = true }

                                        R.string.app_mail -> {
                                            val role = SessionManager.getRole(context)
                                            if (role != null && role.equals("Admin", ignoreCase = true)) {
                                                navController.navigate("admin_mail") { launchSingleTop = true }
                                            } else if (SessionManager.isAppUnlocked(context, "Correo")) {
                                                navController.navigate("mail") { launchSingleTop = true }
                                            } else {
                                                appToUnlock = "Correo"
                                                requiredPin = "7429"
                                                navController.navigate("lock_screen") { launchSingleTop = true }
                                            }
                                        }

                                        R.string.app_system_update -> {
                                            if (SessionManager.isAppUnlocked(context, "System Update")) {
                                                navController.navigate("system_update") { launchSingleTop = true }
                                            } else {
                                                appToUnlock = "System Update"
                                                requiredPin = "3728"
                                                navController.navigate("lock_screen") { launchSingleTop = true }
                                            }
                                        }
                                        R.string.app_exit -> navController.navigate("main_menu") { popUpTo("fake_os") { inclusive = true } }
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
                                                RetrofitClient.instance.completePuzzle(token = "Bearer $token", puzzleName = pName, body = mapOf("timeSeconds" to segundos.toInt()))
                                            }
                                        } catch (e: Exception) {}
                                    }

                                    when (appToUnlock) {
                                        "Correo" -> {
                                            val role = SessionManager.getRole(context)
                                            if (role != null && role.equals("Admin", ignoreCase = true)) {
                                                navController.navigate("admin_mail") { popUpTo("fake_os") }
                                            } else {
                                                navController.navigate("mail") { popUpTo("fake_os") }
                                            }
                                        }
                                        "Archivos" -> navController.navigate("files") { popUpTo("fake_os") }
                                        else -> navController.navigate("system_update") { popUpTo("fake_os") }
                                    }
                                },
                                onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() },
                                context = context
                            )
                        }

                        composable("files") {
                            FilesScreen(
                                onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() },
                                onPatchInstalled = {
                                    isGalleryPatched = true
                                    SessionManager.saveGalleryPatched(context, true)
                                }
                            )
                        }

                        composable("system_update") {
                            SystemUpdateScreen(
                                onSaveProgress = { puzzleName, timeSeconds ->
                                    scope.launch {
                                        try {
                                            val token = SessionManager.getToken(context)
                                            if (token != null) {
                                                RetrofitClient.instance.completePuzzle(
                                                    token = "Bearer $token",
                                                    puzzleName = puzzleName,
                                                    body = mapOf("timeSeconds" to timeSeconds)
                                                )
                                            }
                                        } catch (e: Exception) {
                                        }
                                    }
                                },
                                onFinish = {
                                    navController.navigate("main_menu") {
                                        popUpTo(0)
                                    }
                                }
                            )
                        }

                        composable("sudoku") { SudokuScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }
                        composable("gallery") { GalleryScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }
                        composable("mail") { MailScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }
                        composable("admin_mail") { AdminMailScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }
                        composable("messages") { MessagesScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }
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
                        composable("settings") { SettingsScreen(onBack = { navController.popBackStack() }) }
                        composable("system_settings") { SystemSettingsScreen(onBack = { if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) navController.popBackStack() }) }
                    }
                }
            }
        }
    }
}