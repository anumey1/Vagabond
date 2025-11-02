package com.dicereligion.vagabond.feature.hangman.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dicereligion.vagabond.core.designsystem.pixelBrute
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp

@Composable
fun HangmanVisual(
    incorrectGuesses: Int,
    modifier: Modifier = Modifier
) {
    val hangmanStages = listOf(
        " +---+\n" +
        " |   |\n" +
        " |    \n" +
        " |    \n" +
        " |    \n" +
        " |    \n" +
        "=====",
        " +---+\n" +
        " |   |\n" +
        " |   O\n" +
        " |    \n" +
        " |    \n" +
        " |    \n" +
        "=====",
        " +---+\n" +
        " |   |\n" +
        " |   O\n" +
        " |   |\n" +
        " |    \n" +
        " |    \n" +
        "=====",
        " +---+\n" +
        " |   |\n" +
        " |   O\n" +
        " |  /|\n" +
        " |    \n" +
        " |    \n" +
        "=====",
        " +---+\n" +
        " |   |\n" +
        " |   O\n" +
        " |  /|\\\n" +
        " |    \n" +
        " |    \n" +
        "=====",
        " +---+\n" +
        " |   |\n" +
        " |   O\n" +
        " |  /|\\\n" +
        " |  / \n" +
        " |    \n" +
        "=====",
        " +---+\n" +
        " |   |\n" +
        " |   O\n" +
        " |  /|\\\n" +
        " |  / \\\n" +
        " |    \n" +
        "====="
    )

    Box(
        modifier = modifier
            .size(150.dp) // Fixed size for the container
            .background(Color.Gray)
            .pixelBrute(
                shadowColor = MaterialTheme.colorScheme.onSurface,
                borderColor = MaterialTheme.colorScheme.surfaceVariant
            ),
        contentAlignment = Alignment.Center // Center the Text within the Box
    ) {
        Text(
            text = hangmanStages[incorrectGuesses.coerceIn(hangmanStages.indices)],
            modifier = Modifier.padding(25.dp), // 25dp gap between border and drawing
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp,
            lineHeight = 14.sp,
            color = Color.White,
            textAlign = TextAlign.Start
        )
    }
}
