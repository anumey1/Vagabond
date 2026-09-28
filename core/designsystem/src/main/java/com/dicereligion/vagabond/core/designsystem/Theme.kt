package com.dicereligion.vagabond.core.designsystem

import android.app.Activity
import android.graphics.Paint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun VagabondTheme(
    appTheme: AppTheme = AppTheme.COSMIC_TEAL, // Default to Cosmic Teal
    darkTheme: Boolean = isSystemInDarkTheme(), // Not used directly for theme selection, but kept for MaterialTheme compatibility
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when (appTheme) {
        AppTheme.COSMIC_TEAL -> CosmicTealDarkColorScheme
        AppTheme.RED_STAR -> RedStarDarkColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Extension function to disable anti-aliasing for Paint objects
fun Paint.disableAntiAlias() {
    isAntiAlias = false
}
