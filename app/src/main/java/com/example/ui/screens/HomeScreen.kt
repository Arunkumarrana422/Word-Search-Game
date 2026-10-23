package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import com.example.data.model.Achievement
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PlayerProfile
import com.example.data.model.WorldsRepository
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
fun HomeScreen(
    playerProfile: PlayerProfile,
    achievements: List<Achievement> = emptyList(),
    onContinuePlaying: (Int) -> Unit,
    onSelectWorlds: () -> Unit,
    onDailyChallenge: () -> Unit,
    onTimeAttack: () -> Unit,
    onRelaxMode: () -> Unit,
    onDailyRewards: () -> Unit,
    onAchievements: () -> Unit,
    onStatistics: () -> Unit,
    onSettings: () -> Unit
) {
    val currentWorld = WorldsRepository.getWorldForLevel(playerProfile.currentLevel)
    val logoScale = remember { Animatable(0.9f) }

    val todayStr = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()) }
    val hasClaimableDaily = playerProfile.lastRewardClaimDate != todayStr
    val hasClaimableBadges = achievements.any { it.isCompleted && !it.isClaimed }

    LaunchedEffect(Unit) {
        logoScale.animateTo(1f, tween(400, easing = FastOutSlowInEasing))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(QuestPurpleDeep, Color(0xFF1D0E47), Color(0xFF0F172A))
                )
            )
            .testTag("home_screen_root")
    ) {
        AmbientParticlesBackground()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top HUD: Player Profile, Coins, Stars, and Settings
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Coins & Stars Pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.12f))
                            .glassBorder(RoundedCornerShape(20.dp), borderColor = CoinGold.copy(alpha = 0.6f))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = AppVectorIcons.Coin,
                            contentDescription = null,
                            tint = CoinGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${playerProfile.coins}",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = CoinGold
                            )
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            imageVector = AppVectorIcons.Star,
                            contentDescription = null,
                            tint = StarYellow,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${playerProfile.totalStars}",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = StarYellow
                            )
                        )
                    }

                    // Settings Button
                    IconButton(
                        onClick = onSettings,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                            .testTag("home_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                }
            }

            // Animated Hero Game Title Banner
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .scale(logoScale.value),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val title = "WORD QUEST"
                        val colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            CoinGold,
                            MaterialTheme.colorScheme.secondary,
                            QuestPink,
                            MaterialTheme.colorScheme.tertiary,
                            QuestCyan,
                            QuestGreen,
                            Color(0xFFFF7043),
                            Color(0xFFCE93D8)
                        )
                        var colorIndex = 0
                        title.forEach { char ->
                            if (char == ' ') {
                                Spacer(modifier = Modifier.width(10.dp))
                            } else {
                                val color = colors[colorIndex % colors.size]
                                colorIndex++
                                Text(
                                    text = char.toString(),
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp,
                                        color = color
                                    )
                                )
                            }
                        }
                    }
                    Text(
                        text = "WORD SEARCH PUZZLE",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    )
                }
            }

            // Big "CONTINUE PLAYING" Primary Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .glassBorder(RoundedCornerShape(24.dp), borderColor = QuestPurple)
                        .clip(RoundedCornerShape(24.dp))
                        .testTag("home_continue_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = glassCardColors(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "CONTINUE QUEST",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp,
                                        color = Color.White.copy(alpha = 0.85f)
                                    )
                                )
                                Text(
                                    text = "Level ${playerProfile.currentLevel}",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = AppVectorIcons.getIconForWorld(currentWorld.id),
                                        contentDescription = null,
                                        tint = Color.White.copy(alpha = 0.9f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = currentWorld.name,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color.White.copy(alpha = 0.9f)
                                        )
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .clickable { onContinuePlaying(playerProfile.currentLevel) }
                                    .testTag("home_continue_play_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play",
                                    tint = QuestPurple,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // XP Progress
                        LinearProgressIndicator(
                            progress = { playerProfile.xpProgressFraction },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = CoinGold,
                            trackColor = Color.White.copy(alpha = 0.3f)
                        )
                    }
                }
            }

            // Quick Play Modes Row: Daily Challenge & World Selection
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Daily Challenge Card
                    HomeActionCard(
                        modifier = Modifier.weight(1f),
                        title = "Daily Puzzle",
                        subtitle = "Streak: ${playerProfile.dailyStreak} Days",
                        icon = Icons.Default.CalendarMonth,
                        accentColor = QuestPink,
                        testTag = "home_daily_challenge_button",
                        onClick = onDailyChallenge
                    )

                    // World Selection Card
                    HomeActionCard(
                        modifier = Modifier.weight(1f),
                        title = "Worlds",
                        subtitle = "10 Themed Realms",
                        icon = Icons.Default.Public,
                        accentColor = QuestCyan,
                        testTag = "home_worlds_button",
                        onClick = onSelectWorlds
                    )
                }
            }

            // Secondary Game Modes: Time Attack & Zen Relax
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HomeActionCard(
                        modifier = Modifier.weight(1f),
                        title = "Time Attack",
                        subtitle = "Beat the Clock",
                        icon = Icons.Default.Timer,
                        accentColor = Color(0xFFFF7043),
                        testTag = "home_time_attack_button",
                        onClick = onTimeAttack
                    )

                    HomeActionCard(
                        modifier = Modifier.weight(1f),
                        title = "Zen Relax",
                        subtitle = "No Timers",
                        icon = Icons.Default.Spa,
                        accentColor = QuestGreen,
                        testTag = "home_relax_button",
                        onClick = onRelaxMode
                    )
                }
            }

            // Bottom Hub: Daily Gifts, Achievements, Statistics
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SmallHubButton(
                        text = "Daily",
                        icon = AppVectorIcons.Gift,
                        accentColor = CoinGold,
                        onClick = onDailyRewards,
                        testTag = "home_daily_rewards_button",
                        isGlowing = hasClaimableDaily,
                        modifier = Modifier.weight(1f)
                    )

                    SmallHubButton(
                        text = "Badges",
                        icon = AppVectorIcons.Trophy,
                        accentColor = StarYellow,
                        onClick = onAchievements,
                        testTag = "home_achievements_button",
                        isGlowing = hasClaimableBadges,
                        modifier = Modifier.weight(1f)
                    )

                    SmallHubButton(
                        text = "Stats",
                        icon = AppVectorIcons.BarChart,
                        accentColor = QuestCyan,
                        onClick = onStatistics,
                        testTag = "home_statistics_button",
                        isGlowing = false,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun HomeActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .glassBorder(RoundedCornerShape(20.dp), borderColor = accentColor)
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .testTag(testTag),
        shape = RoundedCornerShape(20.dp),
        colors = glassCardColors(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White.copy(alpha = 0.7f)
                )
            )
        }
    }
}

@Composable
private fun SmallHubButton(
    text: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier,
    isGlowing: Boolean = false
) {
    val infinitePulse = rememberInfiniteTransition(label = "hub_glow_$text")
    val pulseAlpha by infinitePulse.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )
    val pulseScale by infinitePulse.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_scale"
    )

    Box(
        modifier = modifier
            .scale(if (isGlowing) pulseScale else 1.0f)
            .clip(RoundedCornerShape(24.dp))
            .background(
                if (isGlowing) accentColor.copy(alpha = 0.25f * pulseAlpha)
                else Color.Black.copy(alpha = 0.28f)
            )
            .border(
                width = if (isGlowing) 2.dp else 1.dp,
                color = if (isGlowing) accentColor.copy(alpha = pulseAlpha) else accentColor.copy(alpha = 0.6f),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 12.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                maxLines = 1
            )
        }

        if (isGlowing) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF1744))
                    .border(1.5.dp, Color.White, CircleShape)
                    .align(Alignment.TopEnd)
            )
        }
    }
}
