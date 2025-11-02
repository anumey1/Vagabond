package com.dicereligion.vagabond.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
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
fun LandingScreen(navController: NavController) {
    val systemBarPadding = WindowInsets.systemBars.asPaddingValues()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Band for Vagabond title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp) // Increased height to accommodate title and padding
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(top = systemBarPadding.calculateTopPadding(), start = 16.dp, end = 16.dp), // Use systemBars padding
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Vagabond",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        // Dark teal border
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(MaterialTheme.colorScheme.primary))
        // Darker teal border
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Take remaining space
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { navController.navigate("collections") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .pixelBrute(
                        shadowColor = MaterialTheme.colorScheme.onSurface,
                        borderColor = MaterialTheme.colorScheme.surfaceVariant,
                        onClick = { navController.navigate("collections") }
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent) // Let pixelBrute handle background
            ) {
                Text("Collections", style = MaterialTheme.typography.titleLarge, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("settings") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .pixelBrute(
                        shadowColor = MaterialTheme.colorScheme.onSurface,
                        borderColor = MaterialTheme.colorScheme.surfaceVariant,
                        onClick = { navController.navigate("settings") }
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent) // Let pixelBrute handle background
            ) {
                Text("Settings", style = MaterialTheme.typography.titleLarge, color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LandingScreenPreview() {
    VagabondTheme {
        LandingScreen(rememberNavController())
    }
}