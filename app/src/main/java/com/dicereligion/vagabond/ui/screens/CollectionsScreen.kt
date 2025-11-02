package com.dicereligion.vagabond.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Collections", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack("landing", inclusive = false) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to Landing")
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
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(8) {
                if (it == 0) { // First tile is Hangman
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(128.dp)
                            .pixelBrute(
                                shadowColor = MaterialTheme.colorScheme.onSurface,
                                borderColor = MaterialTheme.colorScheme.surfaceVariant,
                                onClick = { navController.navigate("hangman") }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Hangman",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                } else { // Other tiles are Coming Soon placeholders
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(128.dp)
                            .pixelBrute(
                                shadowColor = MaterialTheme.colorScheme.onSurface,
                                borderColor = MaterialTheme.colorScheme.surfaceVariant,
                                onClick = null // Inert tile
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Coming Soon",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CollectionsScreenPreview() {
    VagabondTheme {
        CollectionsScreen(rememberNavController())
    }
}
