package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.random.Random
import kotlinx.coroutines.delay
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
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.theme.dialogCardColors
import com.example.ui.theme.glassBorder
import com.example.ui.theme.glassCardColors

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
    Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .glassBorder(RoundedCornerShape(24.dp), borderColor = MaterialTheme.colorScheme.primary)
                .testTag("pause_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = dialogCardColors(),
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
                        .glassBorder(RoundedCornerShape(12.dp), borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
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
                        .glassBorder(RoundedCornerShape(12.dp), borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
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
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
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
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
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
    isTimedMode: Boolean = false,
    onNextLevel: () -> Unit,
    onReplay: () -> Unit,
    onHome: () -> Unit
) {
    val starScale1 = remember { Animatable(0f) }
    val starScale2 = remember { Animatable(0f) }
    val starScale3 = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        if (starsEarned >= 1) starScale1.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        if (starsEarned >= 2) starScale2.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        if (starsEarned >= 3) starScale3.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
    }

    Dialog(
        onDismissRequest = {},
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .glassBorder(RoundedCornerShape(28.dp), borderColor = QuestGreen)
                .testTag("level_complete_dialog"),
            shape = RoundedCornerShape(28.dp),
            colors = dialogCardColors(),
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
                    text = if (isTimedMode) "TIME ATTACK COMPLETE!" else "LEVEL COMPLETE!",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                )

                Text(
                    text = if (isTimedMode) "Time Level $levelNumber/10 Solved in ${timeTakenSeconds}s" else "Level $levelNumber Solved in ${timeTakenSeconds}s",
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
                        .glassBorder(RoundedCornerShape(16.dp), borderColor = CoinGold.copy(alpha = 0.5f))
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
                        onClick = onHome,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("level_complete_home_button"),
                        shape = CircleShape
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(imageVector = Icons.Default.Home, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Home", color = Color.White, fontSize = 13.sp, maxLines = 1, softWrap = false)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Sunburst rotating glow rays canvas for celebratory background.
 */
@Composable
fun SunburstRaysCanvas(
    modifier: Modifier = Modifier,
    color: Color = CoinGold.copy(alpha = 0.22f)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sunburst_rot")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sunburst_angle"
    )

    Canvas(modifier = modifier) {
        val centerPt = center
        val radius = size.minDimension * 0.95f
        val rayCount = 12
        val rayAngle = 360f / rayCount

        rotate(rotation, pivot = centerPt) {
            for (i in 0 until rayCount step 2) {
                val startAngle = i * rayAngle
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = rayAngle,
                    useCenter = true,
                    topLeft = Offset(centerPt.x - radius, centerPt.y - radius),
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
                )
            }
        }
    }
}

private data class ConfettiParticle(
    val vx: Float,
    val vy: Float,
    val color: Color,
    val size: Float,
    val rotation: Float,
    val isCircle: Boolean
)

/**
 * Animated confetti explosion burst from center.
 */
@Composable
fun ConfettiExplosionCanvas(
    modifier: Modifier = Modifier,
    particleCount: Int = 50
) {
    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
        )
    }

    val particles = remember(particleCount) {
        val rng = Random(42)
        val colors = listOf(
            CoinGold,
            QuestGreen,
            QuestPink,
            StarYellow,
            Color(0xFF00E5FF),
            Color(0xFF9D4EDD),
            Color(0xFFFF9100),
            Color(0xFF00E676)
        )
        List(particleCount) {
            val angle = rng.nextFloat() * 2f * Math.PI.toFloat()
            val speed = rng.nextFloat() * 480f + 180f
            ConfettiParticle(
                vx = kotlin.math.cos(angle) * speed,
                vy = kotlin.math.sin(angle) * speed - 180f,
                color = colors[rng.nextInt(colors.size)],
                size = rng.nextFloat() * 14f + 8f,
                rotation = rng.nextFloat() * 360f,
                isCircle = rng.nextBoolean()
            )
        }
    }

    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val t = animProgress.value
        val gravity = 280f * t * t
        val alpha = (1f - t).coerceIn(0f, 1f)

        if (alpha > 0f) {
            for (p in particles) {
                val px = cx + p.vx * t
                val py = cy + p.vy * t + gravity
                val pColor = p.color.copy(alpha = alpha * 0.95f)

                rotate(p.rotation + t * 720f, pivot = Offset(px, py)) {
                    if (p.isCircle) {
                        drawCircle(color = pColor, radius = p.size / 2f, center = Offset(px, py))
                    } else {
                        drawRect(
                            color = pColor,
                            topLeft = Offset(px - p.size / 2f, py - p.size / 4f),
                            size = androidx.compose.ui.geometry.Size(p.size, p.size / 2f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Celebratory Reward Claim Dialog with animated confetti explosion, sunburst glow, animated coin counter, and pulsing button.
 */
@Composable
fun RewardClaimDialog(
    title: String,
    coins: Int,
    onDismiss: () -> Unit
) {
    val scaleAnim = remember { Animatable(0.2f) }
    val rotateAnim = remember { Animatable(-15f) }
    var displayedCoins by remember { androidx.compose.runtime.mutableIntStateOf(0) }

    val infinitePulse = rememberInfiniteTransition(label = "pulse_button")
    val buttonScale by infinitePulse.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "btn_scale"
    )

    LaunchedEffect(Unit) {
        try {
            val sound = com.example.audio.SoundManager()
            sound.playCoinCollect()
            sound.playAchievementUnlocked()
        } catch (_: Exception) {}
        scaleAnim.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow))
        rotateAnim.animateTo(0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
    }

    LaunchedEffect(coins) {
        displayedCoins = 0
        val steps = coins.coerceIn(1, 20)
        val delayPerStep = (600 / steps).toLong()
        for (i in 1..coins) {
            displayedCoins = i
            delay(delayPerStep)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .scale(scaleAnim.value)
                .rotate(rotateAnim.value),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassBorder(RoundedCornerShape(32.dp), borderColor = CoinGold)
                    .testTag("reward_claim_dialog"),
                shape = RoundedCornerShape(32.dp),
                colors = dialogCardColors(),
                elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    SunburstRaysCanvas(
                        modifier = Modifier
                            .size(280.dp)
                            .align(Alignment.Center)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(QuestGreen.copy(alpha = 0.85f), CoinGold.copy(alpha = 0.9f))
                                    )
                                )
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "🎁 DAILY GIFT UNLOCKED!",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.5.sp,
                                    color = Color.Black
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Box(
                            modifier = Modifier.size(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(96.dp)
                                    .clip(CircleShape)
                                    .background(CoinGold.copy(alpha = 0.25f))
                            )
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(CoinGold, Color(0xFFFF8F00))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = AppVectorIcons.Gift,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(44.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color.Black.copy(alpha = 0.4f))
                                .border(2.dp, CoinGold, RoundedCornerShape(24.dp))
                                .padding(horizontal = 20.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = AppVectorIcons.Coin,
                                contentDescription = null,
                                tint = CoinGold,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "+$displayedCoins COINS",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = CoinGold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = onDismiss,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .scale(buttonScale)
                                .testTag("reward_claim_ok_button"),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = QuestGreen)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "AWESOME!",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        letterSpacing = 1.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            ConfettiExplosionCanvas(
                modifier = Modifier.fillMaxSize()
            )
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
    onClaimReward: () -> Int?,
    onDismiss: () -> Unit
) {
    val rewardAmounts = listOf(2, 4, 6, 8, 10, 12, 14)
    val displayDay = consecutiveDays.coerceIn(1, 7)
    var showCelebration by remember { androidx.compose.runtime.mutableStateOf(false) }
    var claimedCoinsAmount by remember { androidx.compose.runtime.mutableIntStateOf(rewardAmounts[displayDay - 1]) }

    if (showCelebration) {
        RewardClaimDialog(
            title = "Day $displayDay Daily Gift",
            coins = claimedCoinsAmount,
            onDismiss = {
                showCelebration = false
                onDismiss()
            }
        )
    } else {
        Dialog(
            onDismissRequest = onDismiss,
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .glassBorder(RoundedCornerShape(24.dp), borderColor = MaterialTheme.colorScheme.primary)
                    .testTag("daily_reward_dialog"),
                shape = RoundedCornerShape(24.dp),
                colors = dialogCardColors(),
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

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        when {
                                            isToday -> MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                                            isPast -> QuestGreen.copy(alpha = 0.25f)
                                            else -> Color.White.copy(alpha = 0.08f)
                                        }
                                    )
                                    .border(
                                        width = if (isToday) 1.5.dp else 1.dp,
                                        color = when {
                                            isToday -> MaterialTheme.colorScheme.primary
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
                        onClick = {
                            val reward = onClaimReward()
                            if (reward != null) {
                                claimedCoinsAmount = reward
                            }
                            showCelebration = true
                        },
                        enabled = !isClaimedToday,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("claim_daily_reward_button"),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
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
}

/**
 * Confirmation dialog to reset progress safely.
 */
@Composable
fun ResetProgressDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .glassBorder(RoundedCornerShape(24.dp), borderColor = Color(0xFFFF5252))
                .testTag("reset_progress_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = dialogCardColors(),
            elevation = CardDefaults.cardElevation(defaultElevation = 14.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Reset All Progress?",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF5252)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "This will reset all your unlocked levels, stars, coins, and achievements. This cannot be undone.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = CircleShape
                    ) {
                        Text("Cancel", color = Color.White)
                    }

                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("confirm_reset_button"),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252))
                    ) {
                        Text("Reset", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/**
 * Interactive Welcome Scratch Card Dialog for new users (1 to 50 coins).
 * Centered on screen, with sound effects, drag-to-scratch foil, and celebration.
 */
@Composable
fun ScratchCardDialog(
    rewardCoins: Int,
    onClaim: () -> Unit
) {
    var isRevealed by remember { androidx.compose.runtime.mutableStateOf(false) }
    var scratchedPaths by remember { androidx.compose.runtime.mutableStateOf<List<Path>>(emptyList()) }
    var currentPath by remember { androidx.compose.runtime.mutableStateOf<Path?>(null) }
    var totalDragDistance by remember { mutableFloatStateOf(0f) }

    val scaleAnim = remember { Animatable(0.3f) }
    val rotateAnim = remember { Animatable(-10f) }

    val infinitePulse = rememberInfiniteTransition(label = "scratch_btn_pulse")
    val buttonScale by infinitePulse.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "btn_scale"
    )

    LaunchedEffect(Unit) {
        scaleAnim.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow))
        rotateAnim.animateTo(0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
    }

    Dialog(
        onDismissRequest = {},
        properties = androidx.compose.ui.window.DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .scale(scaleAnim.value)
                .rotate(rotateAnim.value),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassBorder(RoundedCornerShape(32.dp), borderColor = CoinGold)
                    .testTag("scratch_card_dialog"),
                shape = RoundedCornerShape(32.dp),
                colors = dialogCardColors(),
                elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(StarYellow, CoinGold, Color(0xFFFF9100))
                                )
                            )
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "✨ WELCOME GIFT SCRATCH CARD ✨",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.2.sp,
                                color = Color.Black
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (isRevealed) "CONGRATULATIONS!" else "Scratch to Win Coins!",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        ),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = if (isRevealed) "You earned Welcome Bonus coins!" else "Rub your finger on the foil below (1 to 50 Coins)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.75f),
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )

                    // Scratch Surface Box
                    Box(
                        modifier = Modifier
                            .size(width = 260.dp, height = 150.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .border(2.5.dp, CoinGold, RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Underlying Revealed Reward Content
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.radialGradient(
                                        listOf(Color(0xFF2E1065), Color(0xFF0F172A))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            SunburstRaysCanvas(
                                modifier = Modifier.fillMaxSize(),
                                color = CoinGold.copy(alpha = 0.35f)
                            )

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = AppVectorIcons.Coin,
                                    contentDescription = null,
                                    tint = CoinGold,
                                    modifier = Modifier.size(42.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "+$rewardCoins COINS",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = CoinGold
                                    )
                                )
                            }
                        }

                        // Overhead Scratchable Metallic Foil
                        if (!isRevealed) {
                            Canvas(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer { alpha = 0.99f }
                                    .pointerInput(Unit) {
                                        detectDragGestures(
                                            onDragStart = { offset ->
                                                val p = Path().apply { moveTo(offset.x, offset.y) }
                                                currentPath = p
                                            },
                                            onDrag = { change, _ ->
                                                change.consume()
                                                currentPath?.lineTo(change.position.x, change.position.y)
                                                totalDragDistance += 15f
                                                if (totalDragDistance > 450f && !isRevealed) {
                                                    isRevealed = true
                                                }
                                            },
                                            onDragEnd = {
                                                currentPath?.let { scratchedPaths = scratchedPaths + it }
                                                currentPath = null
                                            }
                                        )
                                    }
                            ) {
                                // Draw metallic silver/gold foil
                                drawRect(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFFE0E0E0),
                                            Color(0xFFB0BEC5),
                                            Color(0xFFCFD8DC),
                                            Color(0xFF90A4AE)
                                        )
                                    )
                                )

                                // Scratch path eraser strokes
                                drawIntoCanvas { canvas ->
                                    val paint = Paint().apply {
                                        blendMode = BlendMode.Clear
                                        style = PaintingStyle.Stroke
                                        strokeWidth = 54f
                                        strokeCap = StrokeCap.Round
                                        strokeJoin = StrokeJoin.Round
                                    }
                                    for (path in scratchedPaths) {
                                        canvas.drawPath(path, paint)
                                    }
                                    currentPath?.let { canvas.drawPath(it, paint) }
                                }
                            }

                            // Foil text prompt overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Transparent),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "SCRATCH HERE ✍️",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF263238),
                                        letterSpacing = 1.sp
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    if (isRevealed) {
                        // Claim Button
                        Button(
                            onClick = onClaim,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .scale(buttonScale)
                                .testTag("claim_scratch_card_button"),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = QuestGreen)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "CLAIM MY COINS!",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        letterSpacing = 1.sp
                                    )
                                )
                            }
                        }
                    } else {
                        // Quick Reveal Button
                        TextButton(
                            onClick = { isRevealed = true },
                            modifier = Modifier.testTag("quick_reveal_scratch_button")
                        ) {
                            Text(
                                text = "⚡ Quick Reveal",
                                color = CoinGold,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            if (isRevealed) {
                ConfettiExplosionCanvas(
                    modifier = Modifier.size(340.dp)
                )
            }
        }
    }
}

/**
 * Time Out Dialog shown when the 3-minute Daily Challenge timer runs out.
 */
@Composable
fun TimeoutDialog(
    onRetry: () -> Unit,
    onHome: () -> Unit
) {
    Dialog(
        onDismissRequest = {},
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .glassBorder(RoundedCornerShape(26.dp), borderColor = Color(0xFFFF5252))
                .testTag("timeout_dialog"),
            shape = RoundedCornerShape(26.dp),
            colors = dialogCardColors(),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Timer icon
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(Color(0x33FF5252)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Time Over",
                        tint = Color(0xFFFF5252),
                        modifier = Modifier.size(38.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "TIME OVER!",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.5.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Time ran out! Try again with a new reshuffled board layout!",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Button(
                    onClick = onRetry,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("timeout_retry_button"),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = QuestGreen)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "TRY AGAIN",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onHome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("timeout_home_button"),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "QUIT TO MENU",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

