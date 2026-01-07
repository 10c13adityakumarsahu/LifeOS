package com.example.lifeos.ui

import androidx.compose.ui.Alignment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.runtime.collectAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lifeos.ui.components.BottomNavBar
import com.example.lifeos.ui.dashboard.DashboardScreen
import com.example.lifeos.ui.finance.FinanceScreen
import com.example.lifeos.ui.fitness.FitnessScreen
import com.example.lifeos.ui.navigation.LifeOSDestinations
import com.example.lifeos.ui.settings.SettingsScreen
import com.example.lifeos.ui.tasks.TasksScreen

@Composable
fun LifeOSApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val settingsViewModel: com.example.lifeos.ui.settings.SettingsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = AppViewModelProvider.Factory)
    val settings by settingsViewModel.settings.collectAsState()
    var hasUnlocked by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    if (settings != null && settings!!.pinCode != null && !hasUnlocked) {
        // ... (Pin Screen)
        com.example.lifeos.ui.auth.PinLockScreen(
             isSettingUp = false,
             onPinCorrect = { hasUnlocked = true },
             onPinSet = { input ->
                 if (input == settings!!.pinCode) {
                     hasUnlocked = true
                 }
             }
        )
     } else {
        // Observer for Focus Mode Side Effects (Sound)
        val context = androidx.compose.ui.platform.LocalContext.current
        
        androidx.compose.runtime.LaunchedEffect(settings?.currentFocusMode, settings?.isSilentModeEnabledForFocus) {
             val audioManager = context.getSystemService(android.content.Context.AUDIO_SERVICE) as android.media.AudioManager
             val notificationManager = context.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
             val currentSettings = settings ?: return@LaunchedEffect

             if (currentSettings.currentFocusMode != "None") {
                 // Create Notification Channel if needed
                 val channelId = "focus_mode_channel"
                 val channelName = "Focus Mode"
                 val importance = android.app.NotificationManager.IMPORTANCE_LOW
                 val channel = android.app.NotificationChannel(channelId, channelName, importance)
                 notificationManager.createNotificationChannel(channel)
                 
                 // Show Notification
                 val notification = android.app.Notification.Builder(context, channelId)
                     .setContentTitle("Focus Mode On")
                     .setContentText("${currentSettings.currentFocusMode} mode is active.")
                     .setSmallIcon(com.example.lifeos.R.drawable.ic_launcher)
                     .setOngoing(true)
                     .build()
                 notificationManager.notify(1001, notification)

                 if (currentSettings.isSilentModeEnabledForFocus) {
                     // Capture and Set Silent
                     if (currentSettings.preFocusRingerMode == -1) {
                          settingsViewModel.updatePreFocusRingerMode(audioManager.ringerMode)
                     }
                     
                     if (notificationManager.isNotificationPolicyAccessGranted) {
                          audioManager.ringerMode = android.media.AudioManager.RINGER_MODE_SILENT
                     } else {
                          try {
                               audioManager.ringerMode = android.media.AudioManager.RINGER_MODE_VIBRATE
                          } catch(_: Exception) {}
                     }
                 }
             } else {
                 // Cancel Notification
                 notificationManager.cancel(1001)

                 // Restore Logic
                 // 1. Turn off DND if access granted
                 if (notificationManager.isNotificationPolicyAccessGranted) {
                      notificationManager.setInterruptionFilter(android.app.NotificationManager.INTERRUPTION_FILTER_ALL)
                 }
                 
                 // 2. Restore Ringer Mode from Persisted State
                 if (currentSettings.preFocusRingerMode != -1) {
                      val target = if (currentSettings.preFocusRingerMode != android.media.AudioManager.RINGER_MODE_SILENT) {
                          currentSettings.preFocusRingerMode
                      } else {
                          android.media.AudioManager.RINGER_MODE_NORMAL
                      }
                      
                      try {
                          audioManager.ringerMode = target
                      } catch (e: Exception) {
                          try { audioManager.ringerMode = android.media.AudioManager.RINGER_MODE_VIBRATE } catch(_: Exception) {}
                      }
                      settingsViewModel.updatePreFocusRingerMode(-1)
                 }
             }
        }

        Scaffold(
            bottomBar = {
                if (currentRoute != "focus_mode" && currentRoute != "vault_auth" && currentRoute != "vault_content") {
                    BottomNavBar(navController = navController, currentRoute = currentRoute)
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = LifeOSDestinations.DASHBOARD.route,
                modifier = Modifier.padding(innerPadding),
                enterTransition = { 
                    slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(400)) + 
                    fadeIn(animationSpec = tween(400)) 
                },
                exitTransition = { 
                    slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(400)) + 
                    fadeOut(animationSpec = tween(400)) 
                },
                popEnterTransition = { 
                    slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(400)) + 
                    fadeIn(animationSpec = tween(400)) 
                },
                popExitTransition = { 
                    slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(400)) + 
                    fadeOut(animationSpec = tween(400)) 
                }
            ) {
                composable(LifeOSDestinations.DASHBOARD.route) {
                    DashboardScreen()
                }
                composable(LifeOSDestinations.TASKS.route) {
                    TasksScreen()
                }
                composable(LifeOSDestinations.FITNESS.route) {
                    FitnessScreen()
                }
                composable(LifeOSDestinations.FINANCE.route) {
                    FinanceScreen()
                }
                composable(LifeOSDestinations.EVENTS.route) {
                    com.example.lifeos.ui.events.EventsScreen()
                }
                composable(LifeOSDestinations.SETTINGS.route) {
                    SettingsScreen(
                        onOpenVault = { navController.navigate("vault_auth") },
                        onNavigateToFocus = { navController.navigate("focus_mode") }
                    )
                }
                composable("vault_auth") {
                    // Check PIN again before showing pure Vault Screen
                    val viewModel: com.example.lifeos.ui.settings.SettingsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = AppViewModelProvider.Factory)
                    val set by viewModel.settings.collectAsState()
                    
                    if (set?.pinCode != null) {
                         com.example.lifeos.ui.auth.PinLockScreen(
                             isSettingUp = false,
                             onPinCorrect = { }, // Unused by PinLockScreen currently
                             onPinSet = { input -> 
                                 if (input == set?.pinCode) {
                                     navController.navigate("vault_content") { 
                                         popUpTo("vault_auth") { inclusive = true } 
                                     } 
                                 }
                             } 
                         )
                    } else {
                        // No PIN set? Warn user or let them in (User said "Selective Access", implies PIN).
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                             Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                 Text("Please set a Security PIN in Settings first!")
                                 androidx.compose.material3.Button(onClick = { navController.popBackStack() }) { Text("Go Back") }
                             }
                        }
                    }
                }
                composable("vault_content") {
                    com.example.lifeos.ui.vault.VaultScreen(onNavigateBack = { navController.popBackStack() })
                }
                composable("focus_mode") {
                    com.example.lifeos.ui.sleep.FocusModeScreen(onExitFocusMode = { navController.popBackStack() })
                }
            }
        }
    }
}
