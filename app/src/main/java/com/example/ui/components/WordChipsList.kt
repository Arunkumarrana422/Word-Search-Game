package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PlacedWord
import com.example.data.model.WordHighlightColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WordChipsList(
    targetWords: List<String>,
    foundWords: List<PlacedWord>,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val foundWordMap = foundWords.associateBy { it.word }
    val progress = if (targetWords.isNotEmpty()) foundWords.size.toFloat() / targetWords.size else 0f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("word_chips_list_container")
    ) {
        // Progress header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "WORDS TO FIND",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${foundWords.size}/${targetWords.size}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = accentColor
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Progress bar
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = accentColor,
            trackColor = Color.White.copy(alpha = 0.2f)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Chips flow
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (word in targetWords) {
                val placed = foundWordMap[word]
                val isFound = placed != null
                val highlightColor = if (placed != null) {
                    WordHighlightColors[placed.colorIndex % WordHighlightColors.size]
                } else {
                    Color.Transparent
                }

                WordChipItem(
                    word = word,
                    isFound = isFound,
                    highlightColor = highlightColor
                )
            }
        }
    }
}

@Composable
private fun WordChipItem(
    word: String,
    isFound: Boolean,
    highlightColor: Color
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isFound) highlightColor.copy(alpha = 0.35f) else Color.White.copy(alpha = 0.12f),
        animationSpec = tween(300),
        label = "chip_bg"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isFound) highlightColor else Color.White.copy(alpha = 0.25f),
        animationSpec = tween(300),
        label = "chip_border"
    )

    val textColor by animateColorAsState(
        targetValue = if (isFound) Color.White.copy(alpha = 0.65f) else Color.White,
        animationSpec = tween(300),
        label = "chip_text"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(1.2.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("word_chip_$word"),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isFound) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(highlightColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Found",
                        tint = Color.Black,
                        modifier = Modifier.size(12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
            }

            Text(
                text = word,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isFound) FontWeight.Normal else FontWeight.Bold,
                    textDecoration = if (isFound) TextDecoration.LineThrough else TextDecoration.None,
                    letterSpacing = 1.sp,
                    color = textColor
                )
            )
        }
    }
}
