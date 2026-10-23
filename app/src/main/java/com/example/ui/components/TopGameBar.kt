package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
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
import com.example.ui.theme.glassBorder

@Composable
fun TopGameBar(
    levelNumber: Int,
    isDaily: Boolean = false,
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
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(Color.Black.copy(alpha = 0.28f))
            .glassBorder(RoundedCornerShape(22.dp), borderColor = Color.White.copy(alpha = 0.3f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Center: Level or Daily Puzzle, and World / Timer
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = if (isDaily) "DAILY PUZZLE" else "LEVEL $levelNumber",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp,
                    color = Color.White
                ),
                maxLines = 1
            )

            if (remainingSeconds != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (remainingSeconds <= 30) Color(0x66FF5252) else Color(0x44000000))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Timer",
                        tint = if (remainingSeconds <= 30) Color(0xFFFF5252) else Color(0xFFFFD54F),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${remainingSeconds / 60}:${(remainingSeconds % 60).toString().padStart(2, '0')}",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = if (remainingSeconds <= 30) Color(0xFFFF5252) else Color(0xFFFFD54F)
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

        Spacer(modifier = Modifier.width(8.dp))

        // Right side: Coin pill
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Coin Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.35f))
                    .glassBorder(RoundedCornerShape(16.dp), borderColor = CoinGold.copy(alpha = 0.6f))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .testTag("top_bar_coin_badge"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = AppVectorIcons.Coin,
                    contentDescription = "Coins",
                    tint = CoinGold,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$coins",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = CoinGold
                    ),
                    maxLines = 1
                )
            }
        }
    }
}
