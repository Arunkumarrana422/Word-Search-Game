package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DifficultyTier
import com.example.ui.theme.glassBorder

@Composable
fun TopGameBar(
    levelNumber: Int,
    isDaily: Boolean = false,
    isTimed: Boolean = false,
    worldName: String,
    coins: Int = 0,
    difficultyTier: DifficultyTier? = null,
    remainingSeconds: Int? = null,
    onBackClick: () -> Unit = {},
    onPauseClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(Color.Black.copy(alpha = 0.28f))
            .glassBorder(RoundedCornerShape(22.dp), borderColor = Color.White.copy(alpha = 0.3f))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (remainingSeconds != null) {
            // Time Attack / Daily Timed Mode: Level text on LEFT, Timer on RIGHT
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = when {
                        isDaily -> "DAILY PUZZLE"
                        isTimed -> "TIME LEVEL $levelNumber/10"
                        else -> "LEVEL $levelNumber"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp,
                        color = Color.White
                    ),
                    maxLines = 1
                )
                Text(
                    text = worldName,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.75f)
                    ),
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Timer Pill on Right
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (remainingSeconds <= 30) Color(0x88FF5252) else Color(0x55000000))
                    .glassBorder(
                        RoundedCornerShape(14.dp),
                        borderColor = if (remainingSeconds <= 30) Color(0xFFFF5252) else Color.White.copy(alpha = 0.3f)
                    )
                    .padding(horizontal = 12.dp, vertical = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = "Timer",
                    tint = if (remainingSeconds <= 30) Color(0xFFFF5252) else Color(0xFFFFD54F),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${remainingSeconds / 60}:${(remainingSeconds % 60).toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = if (remainingSeconds <= 30) Color(0xFFFF5252) else Color(0xFFFFD54F)
                    )
                )
            }
        } else {
            // Standard Level Mode
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = when {
                        isDaily -> "DAILY PUZZLE"
                        isTimed -> "TIME LEVEL $levelNumber/10"
                        else -> "LEVEL $levelNumber"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp,
                        color = Color.White
                    ),
                    maxLines = 1
                )
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
    }
}


