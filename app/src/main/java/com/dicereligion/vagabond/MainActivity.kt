package com.dicereligion.vagabond

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dicereligion.vagabond.data.SettingsRepository
import com.dicereligion.vagabond.ui.screens.LandingScreen
import com.dicereligion.vagabond.ui.screens.CollectionsScreen
import com.dicereligion.vagabond.ui.screens.SettingsScreen
import com.dicereligion.vagabond.core.designsystem.VagabondTheme
import com.dicereligion.vagabond.core.designsystem.AppTheme
import com.dicereligion.vagabond.feature.hangman.ui.HangmanStartScreen
import com.dicereligion.vagabond.feature.hangman.ui.HangmanInputScreen
import com.dicereligion.vagabond.feature.hangman.ui.HangmanGameScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

import androidx.navigation.navigation

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appTheme by settingsRepository.appTheme.collectAsState(initial = com.dicereligion.vagabond.core.designsystem.AppTheme.COSMIC_TEAL)

            VagabondTheme(appTheme = appTheme) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "landing") {
                    composable("landing") {
                        LandingScreen(navController = navController)
                    }
                    composable("collections") {
                        CollectionsScreen(navController = navController)
                    }
                    composable("settings") {
                        SettingsScreen(navController = navController)
                    }
                    navigation(startDestination = "hangman_start", route = "hangman") {
                        composable("hangman_start") {
                            HangmanStartScreen(navController = navController)
                        }
                        composable("hangman_input") {
                            HangmanInputScreen(navController = navController)
                        }
                        composable("hangman_game") {
                            HangmanGameScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}