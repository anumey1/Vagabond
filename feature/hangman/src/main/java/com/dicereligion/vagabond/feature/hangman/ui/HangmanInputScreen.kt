package com.dicereligion.vagabond.feature.hangman.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dicereligion.vagabond.core.designsystem.VagabondTheme
import com.dicereligion.vagabond.core.designsystem.pixelBrute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HangmanInputScreen(navController: NavController) {
    val viewModel: HangmanViewModel = hiltViewModel(navController.getBackStackEntry("hangman"))
    var phraseInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Enter a Word or Phrase", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack("hangman_start", inclusive = false) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to Hangman Start")
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
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = phraseInput,
                onValueChange = { phraseInput = it },
                label = { Text("Word or Phrase") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (phraseInput.isNotBlank()) {
                        viewModel.startGame(phraseInput)
                        navController.navigate("hangman_game")
                    }
                },
                enabled = phraseInput.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .pixelBrute(
                        shadowColor = MaterialTheme.colorScheme.onSurface,
                        borderColor = MaterialTheme.colorScheme.surfaceVariant,
                        onClick = {
                            if (phraseInput.isNotBlank()) {
                                viewModel.startGame(phraseInput)
                                navController.navigate("hangman_game")
                            }
                        }
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent) // Let pixelBrute handle background
            ) {
                Text("Play", style = MaterialTheme.typography.titleLarge, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.startGame()
                    navController.navigate("hangman_game")
                },
                enabled = true, // Always enabled for computer mode
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .pixelBrute(
                        shadowColor = MaterialTheme.colorScheme.onSurface,
                        borderColor = MaterialTheme.colorScheme.surfaceVariant,
                        onClick = {
                            viewModel.startGame()
                            navController.navigate("hangman_game")
                        }
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent) // Let pixelBrute handle background
            ) {
                Text("Computer", style = MaterialTheme.typography.titleLarge, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.popBackStack("hangman_start", inclusive = false) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .pixelBrute(
                        shadowColor = MaterialTheme.colorScheme.onSurface,
                        borderColor = MaterialTheme.colorScheme.surfaceVariant,
                        onClick = { navController.popBackStack("hangman_start", inclusive = false) }
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent) // Let pixelBrute handle background
            ) {
                Text("Back", style = MaterialTheme.typography.titleLarge, color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HangmanInputScreenPreview() {
    VagabondTheme {
        HangmanInputScreen(rememberNavController())
    }
}