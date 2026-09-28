package com.dicereligion.vagabond.core.designsystem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Reusable Modifier for hard, offset shadows with tactile pressed state
fun Modifier.pixelBrute(
    shadowColor: Color = Color.Black,
    borderColor: Color = Color.Black,
    backgroundColor: Color? = null,
    borderWidth: Dp = 4.dp,
    shadowOffset: Dp = 4.dp,
    shape: Shape = RectangleShape,
    onClick: (() -> Unit)? = null
) = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentShadowOffset = if (isPressed) 0.dp else shadowOffset
    val currentContentOffset = if (isPressed) shadowOffset else 0.dp
    val bgColor = backgroundColor ?: MaterialTheme.colorScheme.surface

    this
        .offset(x = currentShadowOffset, y = currentShadowOffset) // Shadow layer offset
        .background(shadowColor, shape) // Shadow layer background
        .offset(x = -currentShadowOffset + currentContentOffset, y = -currentShadowOffset + currentContentOffset) // Content layer offset relative to shadow
        .border(BorderStroke(borderWidth, borderColor), shape) // Content layer border
        .background(bgColor, shape) // Content layer background
        .then(if (onClick != null) Modifier.clickable(interactionSource = interactionSource, indication = null, onClick = onClick) else Modifier)
}

// Composable for a pixel-brute styled container with shadow and border
@Composable
fun PixelBruteContainer(
    modifier: Modifier = Modifier,
    shadowColor: Color = MaterialTheme.colorScheme.onSurface,
    borderColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    borderWidth: Dp = 4.dp,
    shadowOffset: Dp = 4.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentShadowOffset = if (isPressed) 0.dp else shadowOffset
    val currentContentOffset = if (isPressed) shadowOffset else 0.dp

    Box(
        modifier = modifier
            .offset(x = currentShadowOffset, y = currentShadowOffset) // Shadow layer offset
            .background(shadowColor, RectangleShape)
    ) {
        Box(
            modifier = Modifier
                .offset(x = -currentShadowOffset + currentContentOffset, y = -currentShadowOffset + currentContentOffset) // Content layer offset relative to shadow
                .background(backgroundColor, RectangleShape)
                .border(BorderStroke(borderWidth, borderColor), RectangleShape)
                .then(if (onClick != null) Modifier.clickable(interactionSource = interactionSource, indication = null, onClick = onClick) else Modifier)
                .padding(shadowOffset) // Add padding to content to avoid overlap with shadow
        ) {
            content()
        }
    }
}
