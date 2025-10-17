package com.zontechx.servidex.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.ceil

@Composable
fun StarRating(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    starCount: Int = 5,
    starSize: Dp = 30.dp,
    spacing: Dp = 6.dp,
    readOnly: Boolean = false,
    activeColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    inactiveColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
) {
    var measuredSize by remember { mutableStateOf(Size.Zero) }

    Row(
        modifier = modifier
            .then(
                if (!readOnly) {
                    Modifier
                        .onSizeChanged { measuredSize = Size(it.width.toFloat(), it.height.toFloat()) }
                        .pointerInput(starCount) {
                            detectTapGestures { offset ->
                                if (measuredSize.width > 0f) {
                                    // Map tap position to star index (1–starCount)
                                    val starWidth = measuredSize.width / starCount
                                    val newRating = ceil(offset.x / starWidth).toInt().coerceIn(1, starCount)
                                    onRatingChanged(newRating)
                                }
                            }
                        }
                } else Modifier
            )
            .semantics { contentDescription = "Rating: $rating out of $starCount" },
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..starCount) {
            val icon = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star
            Icon(
                imageVector = icon,
                contentDescription = if (i <= rating) "Filled star" else "Empty star",
                tint = if (i <= rating) activeColor else inactiveColor,
                modifier = Modifier.size(starSize).weight(1f)
            )
        }
    }
}
