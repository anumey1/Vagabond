package com.dicereligion.vagabond.feature.hangman.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dicereligion.vagabond.core.designsystem.VagabondTheme
import com.dicereligion.vagabond.core.designsystem.pixelBrute

import com.dicereligion.vagabond.feature.hangman.domain.HangmanGameState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HangmanGameScreen(navController: NavController) {
    val viewModel: HangmanViewModel = hiltViewModel(navController.getBackStackEntry("hangman"))
    val gameState by viewModel.gameState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hangman", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack("hangman_input", inclusive = false) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quit Game")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (gameState.isGameOver) {
                HangmanEndGameDialog(
                    gameState = gameState,
                    onPlayAgain = {
                        viewModel.resetGame()
                        navController.navigate("hangman_input") { 
                            popUpTo("hangman_start")
                        }
                    },
                    onBackToMenu = {
                        viewModel.resetGame()
                        navController.popBackStack("hangman_start", inclusive = false)
                    }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HangmanVisual(
                    incorrectGuesses = gameState.incorrectGuesses,
                    modifier = Modifier.align(Alignment.Top) // Align visual to top within the row
                )

                Spacer(modifier = Modifier.weight(0.5f)) // Spacer to push content to the right

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f) // Take remaining space
                ) {
                    Text(
                        text = "Guesses left",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 25.dp) // Increased padding
                    )
                    Text(
                        text = "${gameState.guessesLeft}",
                        style = MaterialTheme.typography.headlineLarge, // Make the number larger
                        color = MaterialTheme.colorScheme.tertiary, // Changed to tertiary accent color
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Word Display
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp) // Add 10dp vertical spacing between lines
            ) {
                formatMaskedWordIntoLines(gameState.maskedWord).forEach { line ->
                    Text(
                        text = line,
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Custom Keyboard
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Numbers Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    "1234567890".forEach { char ->
                        KeyboardButton(char = char, gameState = gameState) { viewModel.guessLetter(char) }
                    }
                }
                // QWERTY Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    "QWERTYUIOP".forEach { char ->
                        KeyboardButton(char = char, gameState = gameState) { viewModel.guessLetter(char) }
                    }
                }
                // ASDFGHJKL Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    "ASDFGHJKL".forEach { char ->
                        KeyboardButton(char = char, gameState = gameState) { viewModel.guessLetter(char) }
                    }
                }
                // ZXCVBNM Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    "ZXCVBNM".forEach { char ->
                        KeyboardButton(char = char, gameState = gameState) { viewModel.guessLetter(char) }
                    }
                }
            }

            // Quit Button
            Button(
                onClick = { navController.popBackStack("hangman_input", inclusive = false) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .pixelBrute(
                        shadowColor = MaterialTheme.colorScheme.onSurface,
                        borderColor = MaterialTheme.colorScheme.surfaceVariant,
                        onClick = { navController.popBackStack("hangman_input", inclusive = false) }
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent) // Let pixelBrute handle background
            ) {
                Text("Quit", style = MaterialTheme.typography.titleLarge, color = Color.White)
            }
        }
    }
}

@Composable
fun KeyboardButton(char: Char, gameState: HangmanGameState, onClick: (Char) -> Unit) {
    val isGuessed = gameState.guessedLetters.contains(char)
    val isCorrect = gameState.secretWord.contains(char)

    val buttonColor = when {
        isGuessed && isCorrect -> MaterialTheme.colorScheme.secondary // Correct guess
        isGuessed && !isCorrect -> MaterialTheme.colorScheme.onTertiary // Incorrect guess
        else -> MaterialTheme.colorScheme.surfaceVariant // Not guessed yet
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clickable(enabled = !isGuessed && !gameState.isGameOver) { onClick(char) }
            .pixelBrute(
                shadowColor = MaterialTheme.colorScheme.onSurface,
                borderColor = MaterialTheme.colorScheme.onSurface,
                backgroundColor = buttonColor
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(char.toString(), style = MaterialTheme.typography.bodyLarge, color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun HangmanGameScreenPreview() {
    VagabondTheme {
        HangmanGameScreen(rememberNavController())
    }
}

private fun formatMaskedWordIntoLines(maskedWord: String, lineCharLimit: Int = 26): List<String> {
    if (maskedWord.isBlank()) return emptyList()

    val maskedWords = maskedWord.split(" / ")
    val lines = mutableListOf<String>()
    var currentLine = ""

    maskedWords.forEach { word ->
        // If a single word is longer than the limit, it gets its own line
        if (word.length > lineCharLimit) {
            // If there's a line being built, add it first
            if (currentLine.isNotEmpty()) {
                lines.add(currentLine)
            }
            // Add the long word as its own line
            lines.add(word)
            // Reset currentLine
            currentLine = ""
            return@forEach // Continue to next word
        }

        if (currentLine.isEmpty()) {
            currentLine = word
        } else {
            val potentialLine = "$currentLine / $word"
            if (potentialLine.length <= lineCharLimit) {
                currentLine = potentialLine
            } else {
                lines.add(currentLine)
                currentLine = word
            }
        }
    }

    if (currentLine.isNotEmpty()) {
        lines.add(currentLine)
    }

    return lines
}
