package com.dicereligion.vagabond.core.designsystem

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

enum class AppTheme {
    COSMIC_TEAL,
    RED_STAR
}

// Cosmic Teal Palette
val CosmicTealBackground = Color(0xFF071212)
val CosmicTealContainerBg = Color(0xFF162929)
val CosmicTealContainerBorder = Color(0xFF008080)
val CosmicTealTextLight = Color(0xFFE0FFFF)
val CosmicTealTextDark = Color(0xFF012B2B)
val CosmicTealAccentOne = Color(0xFF2ECC71)
val CosmicTealAccentOneDark = Color(0xFF27AE60)
val CosmicTealAccentTwo = Color(0xFF7FDBFF)
val CosmicTealAccentTwoDark = Color(0xFF0074D9)
val CosmicTealAccentThree = Color(0xFF3DF2E0)
val CosmicTealAccentThreeDark = Color(0xFF0B544D)
val CosmicTealPriorityHigh = Color(0xFF7FDBFF)
val CosmicTealPriorityMedium = Color(0xFF2ECC71)
val CosmicTealPriorityLow = Color(0xFFA2D2FF)

val CosmicTealDarkColorScheme = darkColorScheme(
    primary = CosmicTealAccentOne,
    onPrimary = CosmicTealTextDark,
    primaryContainer = CosmicTealAccentOneDark,
    onPrimaryContainer = CosmicTealTextLight,
    inversePrimary = CosmicTealAccentTwo,
    secondary = CosmicTealAccentTwo,
    onSecondary = CosmicTealTextDark,
    secondaryContainer = CosmicTealAccentTwoDark,
    onSecondaryContainer = CosmicTealTextLight,
    tertiary = CosmicTealAccentThree,
    onTertiary = CosmicTealTextDark,
    tertiaryContainer = CosmicTealAccentThreeDark,
    onTertiaryContainer = CosmicTealTextLight,
    background = CosmicTealBackground,
    onBackground = CosmicTealTextLight,
    surface = CosmicTealContainerBg,
    onSurface = CosmicTealTextLight,
    surfaceVariant = CosmicTealContainerBorder,
    onSurfaceVariant = CosmicTealTextLight,
    inverseSurface = CosmicTealTextLight,
    inverseOnSurface = CosmicTealBackground,
    error = CosmicTealPriorityHigh,
    onError = CosmicTealTextLight,
    errorContainer = CosmicTealAccentThreeDark,
    onErrorContainer = CosmicTealTextLight,
    outline = CosmicTealContainerBorder,
    outlineVariant = CosmicTealContainerBorder,
    scrim = Color.Black.copy(alpha = 0.8f),
    surfaceBright = CosmicTealBackground,
    surfaceContainer = CosmicTealContainerBg,
    surfaceContainerHigh = CosmicTealContainerBg,
    surfaceContainerHighest = CosmicTealContainerBg,
    surfaceContainerLow = CosmicTealContainerBg,
    surfaceContainerLowest = CosmicTealBackground,
    surfaceDim = CosmicTealBackground
)

// Red Star Palette
val RedStarBackground = Color(0xFF1A0A0A)
val RedStarContainerBg = Color(0xFF331A1A)
val RedStarContainerBorder = Color(0xFFF78D98)
val RedStarTextLight = Color(0xFFFFF8E1)
val RedStarTextDark = Color(0xFF3D0000)
val RedStarAccentOne = Color(0xFFF44336)
val RedStarAccentOneDark = Color(0xFFC62828)
val RedStarAccentTwo = Color(0xFFFF737A)
val RedStarAccentTwoDark = Color(0xFF8F3539)
val RedStarAccentThree = Color(0xFFFFEB3B)
val RedStarAccentThreeDark = Color(0xFFFBC02D)
val RedStarPriorityHigh = Color(0xFFF44336)
val RedStarPriorityMedium = Color(0xFFFFEB3B)
val RedStarPriorityLow = Color(0xFFFFCC80)

val RedStarDarkColorScheme = darkColorScheme(
    primary = RedStarAccentOne,
    onPrimary = RedStarTextDark,
    primaryContainer = RedStarAccentOneDark,
    onPrimaryContainer = RedStarTextLight,
    inversePrimary = RedStarAccentTwo,
    secondary = RedStarAccentTwo,
    onSecondary = RedStarTextDark,
    secondaryContainer = RedStarAccentTwoDark,
    onSecondaryContainer = RedStarTextLight,
    tertiary = RedStarAccentThree,
    onTertiary = RedStarTextDark,
    tertiaryContainer = RedStarAccentThreeDark,
    onTertiaryContainer = RedStarTextLight,
    background = RedStarBackground,
    onBackground = RedStarTextLight,
    surface = RedStarContainerBg,
    onSurface = RedStarTextLight,
    surfaceVariant = RedStarContainerBorder,
    onSurfaceVariant = RedStarTextLight,
    inverseSurface = RedStarTextLight,
    inverseOnSurface = RedStarBackground,
    error = RedStarPriorityHigh,
    onError = RedStarTextLight,
    errorContainer = RedStarAccentThreeDark,
    onErrorContainer = RedStarTextLight,
    outline = RedStarContainerBorder,
    outlineVariant = RedStarContainerBorder,
    scrim = Color.Black.copy(alpha = 0.8f),
    surfaceBright = RedStarBackground,
    surfaceContainer = RedStarContainerBg,
    surfaceContainerHigh = RedStarContainerBg,
    surfaceContainerHighest = RedStarContainerBg,
    surfaceContainerLow = RedStarContainerBg,
    surfaceContainerLowest = RedStarBackground,
    surfaceDim = RedStarBackground
)
