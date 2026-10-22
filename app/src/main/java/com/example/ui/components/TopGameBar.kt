package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DifficultyTier
import com.example.ui.theme.CoinGold

@Composable
fun TopGameBar(
    levelNumber: Int,
    worldName: String,
    coins: Int,
    difficultyTier: DifficultyTier? = null,
    remainingSeconds: Int? = null,
    onBackClick: () -> Unit,
    onPauseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(Color.Black.copy(alpha = 0.28f))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.15f))
                .testTag("top_bar_back_button")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        // Center: Level, Difficulty Badge, and World / Timer
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "LEVEL $levelNumber",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp,
                        color = Color.White
                    ),
                    maxLines = 1
                )

                if (difficultyTier != null) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(difficultyTier.color.copy(alpha = 0.22f))
                            .border(1.dp, difficultyTier.color.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = AppVectorIcons.getIconForTier(difficultyTier),
                                contentDescription = null,
                                tint = difficultyTier.color,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${difficultyTier.tierName} (${difficultyTier.gridSize}×${difficultyTier.gridSize})",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = difficultyTier.color
                                ),
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            if (remainingSeconds != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 1.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Timer",
                        tint = if (remainingSeconds <= 15) Color(0xFFFF5252) else Color(0xFFFFD54F),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${remainingSeconds / 60}:${(remainingSeconds % 60).toString().padStart(2, '0')}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (remainingSeconds <= 15) Color(0xFFFF5252) else Color(0xFFFFD54F)
                        )
                    )
                }
            } else {
                Text(
                    text = worldName,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.75f)
                    ),
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.width(4.dp))

        // Right side: Coin pill and Pause Button
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Coin Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.35f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("top_bar_coin_badge"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = AppVectorIcons.Coin,
                    contentDescription = "Coins",
                    tint = CoinGold,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "$coins",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = CoinGold
                    ),
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Pause Button
            IconButton(
                onClick = onPauseClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f))
                    .testTag("top_bar_pause_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Pause,
                    contentDescription = "Pause",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
