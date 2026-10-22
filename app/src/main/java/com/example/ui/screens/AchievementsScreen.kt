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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Achievement
import com.example.ui.components.AmbientParticlesBackground
import com.example.ui.components.AppVectorIcons
import com.example.ui.components.RewardClaimDialog
import com.example.ui.theme.CoinGold
import com.example.ui.theme.QuestGreen
import com.example.ui.theme.QuestPurple
import com.example.ui.theme.QuestPurpleDeep
import com.example.ui.theme.SurfaceCardDark
import com.example.ui.theme.glassBorder
import com.example.ui.theme.glassCardColors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun AchievementsScreen(
    achievements: List<Achievement>,
    onClaimReward: (String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var claimedTitle by remember { mutableStateOf<String?>(null) }
    var claimedCoins by remember { mutableIntStateOf(0) }

    if (claimedTitle != null) {
        RewardClaimDialog(
            title = claimedTitle!!,
            coins = claimedCoins,
            onDismiss = { claimedTitle = null }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(QuestPurpleDeep, Color(0xFF1D0E47), Color(0xFF0F172A))
                )
            )
            .testTag("achievements_screen_root")
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
                    .glassBorder(RoundedCornerShape(24.dp), borderColor = QuestGreen.copy(alpha = 0.5f))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                        .testTag("achievements_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Achievements",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(achievements) { ach ->
                    AchievementCardItem(
                        achievement = ach,
                        onClaim = {
                            onClaimReward(ach.id)
                            claimedTitle = ach.title
                            claimedCoins = ach.rewardCoins
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
private fun AchievementCardItem(
    achievement: Achievement,
    onClaim: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .glassBorder(RoundedCornerShape(20.dp), borderColor = if (achievement.isCompleted) QuestGreen else MaterialTheme.colorScheme.primary)
            .testTag("achievement_card_${achievement.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = glassCardColors(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (achievement.isCompleted) QuestGreen.copy(alpha = 0.25f)
                            else Color.White.copy(alpha = 0.1f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = AppVectorIcons.getIconForAchievement(achievement.id),
                        contentDescription = achievement.title,
                        tint = if (achievement.isCompleted) QuestGreen else Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = achievement.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = achievement.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Progress Bar
                    LinearProgressIndicator(
                        progress = { achievement.progressFraction },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (achievement.isCompleted) QuestGreen else QuestPurple,
                        trackColor = Color.White.copy(alpha = 0.15f)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${achievement.currentProgress} / ${achievement.target}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    )
                }
            }

            // Reward / Claim Button
            if (achievement.isClaimed) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = QuestGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "CLAIMED",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = QuestGreen
                        )
                    }
                }
            } else if (achievement.isCompleted) {
                Button(
                    onClick = onClaim,
                    colors = ButtonDefaults.buttonColors(containerColor = QuestGreen),
                    shape = CircleShape,
                    modifier = Modifier.testTag("claim_${achievement.id}")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = AppVectorIcons.Coin,
                            contentDescription = null,
                            tint = CoinGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+${achievement.rewardCoins}", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.35f))
                        .glassBorder(RoundedCornerShape(12.dp), borderColor = CoinGold.copy(alpha = 0.5f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = AppVectorIcons.Coin,
                        contentDescription = null,
                        tint = CoinGold,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "+${achievement.rewardCoins}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoinGold
                    )
                }
            }
        }
    }
}
