package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Highlight
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CoinGold

@Composable
fun HintActionBar(
    coins: Int,
    onUseRevealLetter: () -> Unit,
    onUseFirstLetter: () -> Unit,
    onUsePathReveal: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HintButtonItem(
            title = "Reveal Letter",
            icon = Icons.Default.Lightbulb,
            cost = 3,
            canAfford = coins >= 3,
            iconTint = Color(0xFFFFD54F),
            testTag = "hint_reveal_letter",
            onClick = onUseRevealLetter
        )

        HintButtonItem(
            title = "First Letter",
            icon = Icons.Default.Visibility,
            cost = 5,
            canAfford = coins >= 5,
            iconTint = Color(0xFFFF4FA3),
            testTag = "hint_first_letter",
            onClick = onUseFirstLetter
        )

        HintButtonItem(
            title = "Word Trail",
            icon = Icons.Default.Highlight,
            cost = 8,
            canAfford = coins >= 8,
            iconTint = Color(0xFF26D9F2),
            testTag = "hint_word_trail",
            onClick = onUsePathReveal
        )
    }
}

@Composable
private fun HintButtonItem(
    title: String,
    icon: ImageVector,
    cost: Int,
    canAfford: Boolean,
    iconTint: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = canAfford, onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag(testTag)
    ) {
        // Icon Circle
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    if (canAfford) Color.White.copy(alpha = 0.18f)
                    else Color.White.copy(alpha = 0.06f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (canAfford) iconTint else Color.White.copy(alpha = 0.3f),
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Cost pill
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(
                    if (canAfford) Color.Black.copy(alpha = 0.4f)
                    else Color.Red.copy(alpha = 0.25f)
                )
                .padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = AppVectorIcons.Coin,
                contentDescription = null,
                tint = CoinGold,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = "$cost",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (canAfford) CoinGold else Color(0xFFFF8A80)
                )
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                color = Color.White.copy(alpha = if (canAfford) 0.85f else 0.4f)
            ),
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
