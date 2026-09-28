package com.dicereligion.vagabond.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dicereligion.vagabond.core.designsystem.PixelFontFamily
import com.dicereligion.vagabond.core.designsystem.VagabondTheme
import com.dicereligion.vagabond.core.designsystem.pixelBrute

private data class GameCardInfo(
    val title: String,
    val description: String,
    val route: String?,
    val enabled: Boolean
)

private val gameList = listOf(
    GameCardInfo(
        title = "Hangman",
        description = "The old reliable game for travellers and the In General Bored.",
        route = "hangman",
        enabled = true
    ),
    GameCardInfo(
        title = "Title in the Works",
        description = "Don't worry, things are coming very soon here too.",
        route = null,
        enabled = false
    ),
    GameCardInfo(
        title = "Title in the Works",
        description = "Don't worry, things are coming very soon here too.",
        route = null,
        enabled = false
    ),
    GameCardInfo(
        title = "Title in the Works",
        description = "Don't worry, things are coming very soon here too.",
        route = null,
        enabled = false
    ),
    GameCardInfo(
        title = "Title in the Works",
        description = "Don't worry, things are coming very soon here too.",
        route = null,
        enabled = false
    ),
    GameCardInfo(
        title = "Title in the Works",
        description = "Don't worry, things are coming very soon here too.",
        route = null,
        enabled = false
    ),
    GameCardInfo(
        title = "Title in the Works",
        description = "Don't worry, things are coming very soon here too.",
        route = null,
        enabled = false
    ),
    GameCardInfo(
        title = "Title in the Works",
        description = "Don't worry, things are coming very soon here too.",
        route = null,
        enabled = false
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Collections", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(gameList) { game ->
                GameCard(
                    info = game,
                    onClick = {
                        if (game.enabled && game.route != null) {
                            navController.navigate(game.route)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun GameCard(info: GameCardInfo, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth() // Card takes full width
            .height(150.dp)
            .pixelBrute(
                shadowColor = MaterialTheme.colorScheme.onSurface,
                borderColor = MaterialTheme.colorScheme.surfaceVariant,
                onClick = if (info.enabled) onClick else null
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = info.title,
            style = MaterialTheme.typography.titleMedium,
            fontFamily = PixelFontFamily, // Explicitly set font
            textDecoration = TextDecoration.Underline,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = info.description,
            style = MaterialTheme.typography.bodySmall,
            fontFamily = PixelFontFamily, // Explicitly set font
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CollectionsScreenPreview() {
    VagabondTheme {
        CollectionsScreen(rememberNavController())
    }
}
