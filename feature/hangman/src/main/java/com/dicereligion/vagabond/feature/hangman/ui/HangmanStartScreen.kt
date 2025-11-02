package com.dicereligion.vagabond.feature.hangman.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dicereligion.vagabond.core.designsystem.VagabondTheme
import com.dicereligion.vagabond.core.designsystem.pixelBrute

@Composable
fun HangmanStartScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hangman",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 64.dp)
        )

        Button(
            onClick = { navController.navigate("hangman_input") },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .pixelBrute(
                    shadowColor = MaterialTheme.colorScheme.onSurface,
                    borderColor = MaterialTheme.colorScheme.surfaceVariant,
                    onClick = { navController.navigate("hangman_input") }
                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent) // Let pixelBrute handle background
        ) {
            Text("Start Game", style = MaterialTheme.typography.titleLarge, color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HangmanStartScreenPreview() {
    VagabondTheme {
        HangmanStartScreen(rememberNavController())
    }
}