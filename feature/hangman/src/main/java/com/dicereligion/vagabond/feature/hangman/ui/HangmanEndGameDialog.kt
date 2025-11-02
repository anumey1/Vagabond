package com.dicereligion.vagabond.feature.hangman.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dicereligion.vagabond.feature.hangman.domain.HangmanGameState
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dicereligion.vagabond.core.designsystem.pixelBrute

import androidx.compose.foundation.layout.Box

@Composable
fun HangmanEndGameDialog(
    gameState: HangmanGameState,
    onPlayAgain: () -> Unit,
    onBackToMenu: () -> Unit
) {
    if (!gameState.isGameOver) return

    Dialog(
        onDismissRequest = { /* Prevent dismissing by clicking outside */ },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp) // 30dp gap on left and right
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(32.dp)
                    .pixelBrute(
                        shadowColor = MaterialTheme.colorScheme.onSurface,
                        borderColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (gameState.isGameWon) "You Win!" else "You Lose!",
                    style = MaterialTheme.typography.headlineLarge,
                    color = if (gameState.isGameWon) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp) // Move down by 10dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "The phrase was: ${gameState.secretWord}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 8.sp), // Reduce size by 50%
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp) // Move down by 10dp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onPlayAgain,
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp)
                            .pixelBrute(
                                shadowColor = MaterialTheme.colorScheme.onSurface,
                                borderColor = MaterialTheme.colorScheme.surfaceVariant,
                                onClick = onPlayAgain
                            ),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text("Again", style = MaterialTheme.typography.titleLarge, color = Color.White) // Changed to "Again"
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onBackToMenu,
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp)
                            .pixelBrute(
                                shadowColor = MaterialTheme.colorScheme.onSurface,
                                borderColor = MaterialTheme.colorScheme.surfaceVariant,
                                onClick = onBackToMenu
                            ),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text("Menu", style = MaterialTheme.typography.titleLarge, color = Color.White)
                    }
                }
            }
        }
    }
}
