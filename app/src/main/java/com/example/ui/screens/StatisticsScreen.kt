package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GameStatistics
import com.example.ui.components.AmbientParticlesBackground
import com.example.ui.components.AppVectorIcons
import com.example.ui.theme.CoinGold
import com.example.ui.theme.QuestCyan
import com.example.ui.theme.QuestGreen
import com.example.ui.theme.QuestPink
import com.example.ui.theme.QuestPurple
import com.example.ui.theme.QuestPurpleDeep
import com.example.ui.theme.StarYellow
import com.example.ui.theme.SurfaceCardDark
import com.example.ui.theme.glassBorder
import com.example.ui.theme.glassCardColors

@Composable
fun StatisticsScreen(
    statistics: GameStatistics,
    totalStars: Int,
    completedLevels: Int,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(QuestPurpleDeep, Color(0xFF130930), Color(0xFF0F172A))
                )
            )
            .testTag("statistics_screen_root")
    ) {
        AmbientParticlesBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Header Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.Black.copy(alpha = 0.28f))
                    .glassBorder(RoundedCornerShape(24.dp), borderColor = QuestCyan.copy(alpha = 0.5f))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Player Statistics",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                item {
                    StatCardItem("Levels Beaten", "$completedLevels / 500", AppVectorIcons.Trophy, QuestGreen)
                }
                item {
                    StatCardItem("Total Stars", "$totalStars Stars", AppVectorIcons.StarsSet, StarYellow)
                }
                item {
                    StatCardItem("Words Found", "${statistics.totalWordsFound}", AppVectorIcons.Books, QuestCyan)
                }
                item {
                    StatCardItem("Puzzles Played", "${statistics.totalGamesPlayed}", AppVectorIcons.Gamepad, QuestPurple)
                }
                item {
                    StatCardItem("Win Rate", "${statistics.winRatePercent}%", AppVectorIcons.Target, QuestPink)
                }
                item {
                    StatCardItem(
                        "Fastest Solve",
                        if (statistics.fastestSolveSeconds > 0) "${statistics.fastestSolveSeconds}s" else "—",
                        AppVectorIcons.Energy,
                        Color(0xFFFF9800)
                    )
                }
                item {
                    StatCardItem("Diagonal Words", "${statistics.diagonalWordsFound}", AppVectorIcons.Diagonal, Color(0xFF00E5FF))
                }
                item {
                    StatCardItem("Best Streak", "${statistics.highestDailyStreak} Days", AppVectorIcons.Flame, Color(0xFFFF5252))
                }
                item {
                    StatCardItem("Hints Used", "${statistics.totalHintsUsed}", AppVectorIcons.LightbulbIcon, CoinGold)
                }
                item {
                    StatCardItem("No-Hint Clears", "${statistics.perfectNoHintGames}", AppVectorIcons.Sparkle, Color(0xFFAEEA00))
                }
            }
        }
    }
}

@Composable
private fun StatCardItem(title: String, value: String, icon: ImageVector, accentColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .glassBorder(RoundedCornerShape(18.dp), borderColor = accentColor),
        shape = RoundedCornerShape(18.dp),
        colors = glassCardColors(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White.copy(alpha = 0.65f)
                )
            )
        }
    }
}
