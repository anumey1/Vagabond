# Vagabond Project State: Save File 1

This document provides a comprehensive overview of the Vagabond Android application as of its current development state. It details the project's architecture, implemented features, and core logic.

## 1. Project Overview

**Vagabond** is a lightweight, offline-first "traveling games" container app designed for 1-2 players. The core vision is to provide a collection of simple, engaging, and nostalgic mini-games with a unique "PixelBrute" aesthetic, optimized for on-the-go usage.

The application is built entirely using a CLI-only workflow, emphasizing modern Android development practices suitable for AI-assisted coding and automation.

**Current Version:** 4.1

---

## 2. Core Architecture

The application follows a modern, modular, single-activity architecture.

*   **UI:** Built entirely with **Jetpack Compose** and **Material 3**.
*   **Architecture Pattern:** Model-View-ViewModel (MVVM) with a Unidirectional Data Flow (UDF). Screen-level state is hoisted into ViewModels.
*   **Modularity:** The project is split into distinct Gradle modules to enforce separation of concerns:
    *   `:app`: The main application shell, responsible for navigation, theming, and hosting features.
    *   `:core:designsystem`: Contains the `PixelBrute` theme, `VagabondTheme`, colors, and typography (`PixelFontFamily`).
    *   `:core:utils`: Houses shared, app-wide utilities, most notably the random number generation engine.
    *   `:feature:hangman`: A self-contained feature module for the Hangman game.
*   **Navigation:** Handled by **Navigation Compose**. A single `NavHost` in `MainActivity` manages navigation between all screens.
*   **Dependency Injection:** **Hilt** is used for managing dependencies, primarily for injecting ViewModels and repositories.

---

## 3. Implemented Modules & Features

### App Module (The Shell)

This module serves as the entry point and container for the rest of the app.

*   **`MainActivity.kt`**: The single activity that hosts the `NavHost` and applies the global `VagabondTheme`.
*   **`LandingScreen.kt`**: The main entry screen of the app.
    *   Displays the "Vagabond" title.
    *   Features two primary navigation buttons: "Collections" and "Settings".
    *   Displays the app's dynamic version number (**4.1**) at the bottom-center of the screen.
*   **`CollectionsScreen.kt`**: The game selection screen.
    *   Displays a vertically scrolling list (`LazyColumn`) of full-width game cards.
    *   The first card is for "Hangman", with a title, description, and navigation logic.
    *   The remaining 7 cards are placeholders for future games, styled consistently but disabled.
*   **`SettingsScreen.kt`**:
    *   Allows the user to toggle between the two app themes: **Cosmic Teal** and **Red Star**.
    *   The theme choice is persisted across app launches using **Jetpack DataStore**.

### Core Modules (Shared Logic)

These modules provide foundational logic and styling for the entire application.

*   **`:core:designsystem`**:
    *   **`Theme.kt` & `Type.kt`**: Defines the `VagabondTheme`, the two color schemes (Cosmic Teal, Red Star), and sets up the app-wide `PixelFontFamily` using the "Press Start 2P" font.
    *   **`PixelBrute.kt`**: A custom `Modifier` that provides the signature visual style of the app, featuring thick borders and hard, offset shadows.
*   **`:core:utils`**:
    *   **`RandomMech.kt`**: A high-quality pseudo-random number generator (PRNG) implementing the **Xoshiro128++** algorithm. It includes utility functions for dice rolls, percentage checks, and list shuffling.
    *   **`Chaos.kt`**: A global singleton object that provides a single, lazily-initialized instance of `RandomMech` for use across the entire app, ensuring a consistent and efficient source of randomness.

### Feature: Hangman

A fully implemented, playable mini-game.

*   **`HangmanStartScreen.kt`**: The entry point for the game, featuring buttons to "Start Game", view "Instructions", or go back to "Collections".
*   **`HangmanInstructionsDialog.kt`**: A pop-up dialog with scrollable text explaining the rules of the game.
*   **`HangmanInputScreen.kt`**: Allows the user to select a game mode:
    1.  **2-Player Mode:** A user can type a secret phrase. The input is validated to contain at least one letter/digit and is limited to 120 characters.
    2.  **Computer Mode:** The game automatically selects a random phrase.
*   **`HangmanGameScreen.kt`**: The main gameplay screen.
    *   **Word Display:** Intelligently wraps the masked secret phrase into multiple lines, ensuring no single word is split. Lines have vertical padding for readability.
    *   **Hangman Visual:** An ASCII-art hangman drawing that updates with each incorrect guess.
    *   **Guess Counter:** Displays the remaining guesses to the right of the hangman visual.
    *   **Keyboard:** A custom on-screen keyboard for guessing letters and numbers. Buttons change color based on whether the guess is correct (`secondary` theme color) or incorrect (`onTertiary` theme color).
*   **`HangmanEndGameDialog.kt`**: A full-width dialog that appears at the end of the game, announcing whether the player won or lost and revealing the secret phrase. It provides options to play "Again" or return to the "Menu".
*   **`HangmanData.kt`**: The data source for Computer mode. It contains extensive lists of movie and TV show titles.
*   **`HangmanGame.kt`**: The domain logic for the game, handling state management, guess validation (case-insensitive), and win/loss conditions.

---

## 4. How It All Works: Key Logic

### Random Number Generation (Xoshiro128++)

The `Chaos.kt` object provides a single point of access to the powerful `RandomMech.kt` PRNG.

```kotlin
// In Chaos.kt
object Chaos {
    val mech: RandomMech by lazy {
        val seed = System.currentTimeMillis() xor System.nanoTime() xor Random.nextLong()
        RandomMech(seed)
    }
}

// In RandomMech.kt
class RandomMech(seed: Long) {
    // Implements the Xoshiro128++ algorithm
    fun nextInt(): Int { /* ... */ }
    fun <T> shuffleList(list: MutableList<T>) { /* ... */ }
    // ... other utilities
}
```

### Hangman Word Selection (Shuffle Bag)

`HangmanData.kt` uses the `Chaos` object to implement an efficient "draw without replacement" system, ensuring no word is repeated until the entire list has been used.

```kotlin
// In HangmanData.kt
private val availableCombined: MutableList<String> = mutableListOf()

fun ReturnMovieOrShow(): String {
    if (availableCombined.isEmpty()) {
        availableCombined.addAll(movie_list)
        availableCombined.addAll(tv_show_list)
        Chaos.mech.shuffleList(availableCombined) // Uses the new PRNG
    }
    return availableCombined.removeLast()
}

// In HangmanGame.kt
private fun selectRandomWord(): String {
    return ReturnMovieOrShow() // This is called for "Computer" mode
}
```

### Smart Word Wrapping Logic

The `HangmanGameScreen` uses a helper function to format the masked word into lines that respect a character limit, preventing words from being split.

```kotlin
// In HangmanGameScreen.kt
private fun formatMaskedWordIntoLines(maskedWord: String, lineCharLimit: Int = 26): List<String> {
    // 1. Splits the masked phrase into words
    val maskedWords = maskedWord.split(" / ")
    val lines = mutableListOf<String>()
    var currentLine = ""

    // 2. Greedily builds lines, adding words until the character limit is reached
    maskedWords.forEach { word ->
        val potentialLine = if (currentLine.isEmpty()) word else "$currentLine / $word"
        if (potentialLine.length <= lineCharLimit) {
            currentLine = potentialLine
        } else {
            lines.add(currentLine)
            currentLine = word
        }
    }
    lines.add(currentLine)
    return lines
}

// The UI then renders this list in a Column
Column {
    formatMaskedWordIntoLines(gameState.maskedWord).forEach { line ->
        Text(text = line)
    }
}
```

---

## 5. Current State Summary

The Vagabond application is in a solid state. The core app shell is functional, the design system is established, and a robust, reusable random number generation utility is in place. The first feature, Hangman, is complete and polished, incorporating several rounds of feedback and quality-of-life improvements. The project is well-structured and ready for the development of future mini-games.
