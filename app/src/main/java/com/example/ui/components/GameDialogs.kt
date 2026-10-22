package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.CoinGold
import com.example.ui.theme.QuestGreen
import com.example.ui.theme.QuestPink
import com.example.ui.theme.QuestPurple
import com.example.ui.theme.StarYellow
import com.example.ui.theme.SurfaceDarkNavy

/**
 * Pause Menu Dialog with instant audio/haptic toggles, resume, restart, and home navigation.
 */
@Composable
fun PauseDialog(
    soundEnabled: Boolean,
    hapticsEnabled: Boolean,
    onSoundToggle: (Boolean) -> Unit,
    onHapticsToggle: (Boolean) -> Unit,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onHome: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("pause_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDarkNavy),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "PAUSED",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp,
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Sound Toggle Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Sound",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Sound Effects", color = Color.White)
                    }
                    GameSwitch(
                        checked = soundEnabled,
                        onCheckedChange = onSoundToggle
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Haptics Toggle Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Vibration,
                            contentDescription = "Haptics",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Vibration", color = Color.White)
                    }
                    GameSwitch(
                        checked = hapticsEnabled,
                        onCheckedChange = onHapticsToggle
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Resume Button
                Button(
                    onClick = onResume,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("pause_resume_button"),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = QuestPurple)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("RESUME", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Restart Button
                OutlinedButton(
                    onClick = onRestart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("pause_restart_button"),
                    shape = CircleShape
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("RESTART LEVEL", color = Color.White, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Home Button
                TextButton(
                    onClick = onHome,
                    modifier = Modifier.testTag("pause_home_button")
                ) {
                    Icon(imageVector = Icons.Default.Home, contentDescription = null, tint = Color.White.copy(alpha = 0.8f))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Quit to Menu", color = Color.White.copy(alpha = 0.8f))
                }
            }
        }
    }
}

/**
 * Celebratory Level Complete screen overlay with animated stars, coin rewards, and next level action.
 */
@Composable
fun LevelCompleteDialog(
    levelNumber: Int,
    starsEarned: Int,
    coinsWon: Int,
    xpWon: Int,
    timeTakenSeconds: Int,
    hasNextLevel: Boolean,
    onNextLevel: () -> Unit,
    onReplay: () -> Unit,
    onMap: () -> Unit
) {
    val starScale1 = remember { Animatable(0f) }
    val starScale2 = remember { Animatable(0f) }
    val starScale3 = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        if (starsEarned >= 1) starScale1.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        if (starsEarned >= 2) starScale2.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        if (starsEarned >= 3) starScale3.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
    }

    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("level_complete_dialog"),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDarkNavy),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Crown / Celebration Icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0x33FFD54F)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Celebration,
                        contentDescription = "Success",
                        tint = StarYellow,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "LEVEL COMPLETE!",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                )

                Text(
                    text = "Level $levelNumber Solved in ${timeTakenSeconds}s",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White.copy(alpha = 0.75f)
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Stars Row
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Star 1
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star 1",
                        tint = if (starsEarned >= 1) StarYellow else Color.White.copy(alpha = 0.2f),
                        modifier = Modifier
                            .size(46.dp)
                            .scale(if (starsEarned >= 1) starScale1.value else 1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // Star 2 (Middle, slightly larger)
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star 2",
                        tint = if (starsEarned >= 2) StarYellow else Color.White.copy(alpha = 0.2f),
                        modifier = Modifier
                            .size(56.dp)
                            .scale(if (starsEarned >= 2) starScale2.value else 1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // Star 3
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star 3",
                        tint = if (starsEarned >= 3) StarYellow else Color.White.copy(alpha = 0.2f),
                        modifier = Modifier
                            .size(46.dp)
                            .scale(if (starsEarned >= 3) starScale3.value else 1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Rewards Card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Coins reward
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = AppVectorIcons.Coin,
                            contentDescription = "Coins",
                            tint = CoinGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "+$coinsWon",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = CoinGold
                            )
                        )
                    }

                    // XP reward
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = AppVectorIcons.Energy,
                            contentDescription = "XP",
                            tint = Color(0xFF00E5FF),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "+$xpWon XP",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00E5FF)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Next Level Button
                if (hasNextLevel) {
                    Button(
                        onClick = onNextLevel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("level_complete_next_button"),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = QuestGreen)
                    ) {
                        Text(
                            text = "NEXT LEVEL",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Replay & Map Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onReplay,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("level_complete_replay_button"),
                        shape = CircleShape
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Replay", color = Color.White, fontSize = 13.sp, maxLines = 1, softWrap = false)
                        }
                    }

                    OutlinedButton(
                        onClick = onMap,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("level_complete_map_button"),
                        shape = CircleShape
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(imageVector = Icons.Default.Home, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Levels", color = Color.White, fontSize = 13.sp, maxLines = 1, softWrap = false)
                        }
                    }
                }
            }
        }
    }
}

/**
 * 7-Day Daily Login Rewards Calendar dialog.
 */
@Composable
fun DailyRewardDialog(
    consecutiveDays: Int,
    isClaimedToday: Boolean,
    onClaimReward: () -> Unit,
    onDismiss: () -> Unit
) {
    val rewardAmounts = listOf(50, 100, 150, 200, 250, 300, 500)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("daily_reward_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDarkNavy),
            elevation = CardDefaults.cardElevation(defaultElevation = 14.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = AppVectorIcons.Gift,
                        contentDescription = null,
                        tint = StarYellow,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DAILY REWARDS",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    )
                }

                Text(
                    text = "Log in every day to claim bonus coins!",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                // 7 days grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    for (day in 1..7) {
                        val isPast = day < consecutiveDays
                        val isToday = day == consecutiveDays
                        val isFuture = day > consecutiveDays

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    when {
                                        isToday -> QuestPurple.copy(alpha = 0.4f)
                                        isPast -> QuestGreen.copy(alpha = 0.25f)
                                        else -> Color.White.copy(alpha = 0.08f)
                                    }
                                )
                                .border(
                                    width = if (isToday) 1.5.dp else 1.dp,
                                    color = when {
                                        isToday -> QuestPurple
                                        isPast -> QuestGreen
                                        else -> Color.White.copy(alpha = 0.15f)
                                    },
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(vertical = 8.dp, horizontal = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "D$day",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Icon(
                                    imageVector = if (day == 7) AppVectorIcons.Trophy else AppVectorIcons.Coin,
                                    contentDescription = null,
                                    tint = if (day == 7) StarYellow else CoinGold,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${rewardAmounts[day - 1]}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = CoinGold
                                )
                                if (isPast) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Claimed",
                                        tint = QuestGreen,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onClaimReward,
                    enabled = !isClaimedToday,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("claim_daily_reward_button"),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = QuestPurple)
                ) {
                    Text(
                        text = if (isClaimedToday) "CLAIMED TODAY" else "CLAIM TODAY'S GIFT",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Confirmation dialog to reset progress safely.
 */
@Composable
fun ResetProgressDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Reset All Progress?", fontWeight = FontWeight.Bold)
        },
        text = {
            Text(text = "This will reset all your unlocked levels, stars, coins, and achievements. This cannot be undone.")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252))
            ) {
                Text("Reset Everything")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
