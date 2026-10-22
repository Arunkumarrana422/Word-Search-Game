package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WordWorld
import com.example.data.repository.GameRepository
import com.example.ui.components.AmbientParticlesBackground
import com.example.ui.components.AppVectorIcons
import com.example.ui.theme.CoinGold
import com.example.ui.theme.QuestGreen
import com.example.ui.theme.QuestPurple
import com.example.ui.theme.StarYellow

@Composable
fun LevelMapScreen(
    world: WordWorld,
    highestUnlockedLevel: Int,
    repository: GameRepository,
    onLevelSelected: (Int) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val levels = (world.startLevel..world.endLevel).toList()

    // Bouncing animation for current active level
    val transition = rememberInfiniteTransition(label = "level_pulse")
    val pulseScale by transition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(world.backgroundGradient))
            .testTag("level_map_screen_root")
    ) {
        AmbientParticlesBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                        .testTag("level_map_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    val sampleTier = com.example.data.model.DifficultyTier.fromLevel(world.startLevel)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = AppVectorIcons.getIconForWorld(world.id),
                            contentDescription = world.name,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = world.name,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(sampleTier.color.copy(alpha = 0.25f))
                                .border(1.dp, sampleTier.color.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = AppVectorIcons.getIconForTier(sampleTier),
                                    contentDescription = sampleTier.tierName,
                                    tint = sampleTier.color,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = sampleTier.tierName,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = sampleTier.color
                                    )
                                )
                            }
                        }
                    }
                    Text(
                        text = "${world.subtitle} • ${sampleTier.gridSize}×${sampleTier.gridSize} Grids",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = world.accentColor
                        )
                    )
                }
            }

            // Grid of Level Nodes
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(levels) { levelNum ->
                    val isUnlocked = levelNum <= highestUnlockedLevel
                    val isCurrent = levelNum == highestUnlockedLevel
                    val stars = repository.getLevelStars(levelNum)

                    LevelNodeItem(
                        levelNumber = levelNum,
                        isUnlocked = isUnlocked,
                        isCurrent = isCurrent,
                        stars = stars,
                        accentColor = world.accentColor,
                        pulseScale = if (isCurrent) pulseScale else 1f,
                        onClick = {
                            if (isUnlocked) onLevelSelected(levelNum)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LevelNodeItem(
    levelNumber: Int,
    isUnlocked: Boolean,
    isCurrent: Boolean,
    stars: Int,
    accentColor: Color,
    pulseScale: Float,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .scale(pulseScale)
            .clickable(enabled = isUnlocked, onClick = onClick)
            .testTag("level_node_$levelNumber")
    ) {
        Box(
            modifier = Modifier
                .size(62.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isCurrent -> QuestPurple
                        isUnlocked -> QuestGreen.copy(alpha = 0.85f)
                        else -> Color.White.copy(alpha = 0.12f)
                    }
                )
                .border(
                    width = if (isCurrent) 2.5.dp else 1.dp,
                    color = if (isCurrent) accentColor else Color.White.copy(alpha = 0.25f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isUnlocked) {
                Text(
                    text = "$levelNumber",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = Color.White.copy(alpha = 0.4f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Stars underneath
        Row(horizontalArrangement = Arrangement.Center) {
            for (i in 1..3) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = if (i <= stars) StarYellow else Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}
