package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui.theme.QuestGreen
import com.example.ui.theme.QuestPurple

/**
 * Beautiful game toggle switch with tactile animated sliding thumb,
 * crisp vector check/cross status indicators, and custom glowing track.
 */
@Composable
fun GameSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val trackWidth = 52.dp
    val trackHeight = 30.dp
    val thumbSize = 24.dp
    val padding = 3.dp

    val primaryColor = MaterialTheme.colorScheme.primary

    val thumbOffset by animateDpAsState(
        targetValue = if (checked) trackWidth - thumbSize - padding else padding,
        animationSpec = tween(durationMillis = 240, easing = FastOutSlowInEasing),
        label = "switch_thumb_offset"
    )

    val trackBgColor by animateColorAsState(
        targetValue = if (checked) primaryColor else Color.White.copy(alpha = 0.15f),
        animationSpec = tween(durationMillis = 240),
        label = "switch_track_color"
    )

    val trackBorderColor by animateColorAsState(
        targetValue = if (checked) primaryColor.copy(alpha = 0.8f) else Color.White.copy(alpha = 0.25f),
        animationSpec = tween(durationMillis = 240),
        label = "switch_border_color"
    )

    Box(
        modifier = modifier
            .width(trackWidth)
            .height(trackHeight)
            .clip(RoundedCornerShape(15.dp))
            .background(trackBgColor)
            .border(1.5.dp, trackBorderColor, RoundedCornerShape(15.dp))
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onCheckedChange(!checked) }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        // Sliding Thumb
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(thumbSize)
                .shadow(elevation = 4.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (checked) Icons.Default.Check else Icons.Default.Close,
                contentDescription = if (checked) "Enabled" else "Disabled",
                tint = if (checked) primaryColor else Color(0xFF78909C),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
