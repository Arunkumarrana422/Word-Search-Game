package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CoinGold
import com.example.ui.theme.QuestGreen
import com.example.ui.theme.QuestPink
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Bouncy, animated floating praise message banner (Awesome!, Perfect!, Brilliant!)
 * displayed on top of the board when a word is found.
 */
@Composable
fun PraisePopupOverlay(
    message: String,
    modifier: Modifier = Modifier
) {
    val scaleAnim = remember { Animatable(0.2f) }
    val alphaAnim = remember { Animatable(0f) }
    val offsetYAnim = remember { Animatable(25f) }

    LaunchedEffect(message) {
        scaleAnim.snapTo(0.2f)
        alphaAnim.snapTo(0f)
        offsetYAnim.snapTo(25f)

        // Smooth bouncy entrance animation
        launch {
            scaleAnim.animateTo(
                targetValue = 1.15f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
            scaleAnim.animateTo(1.0f, tween(120))
        }
        launch {
            alphaAnim.animateTo(1.0f, tween(150))
        }
        launch {
            offsetYAnim.animateTo(0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        }

        delay(850)

        // Smooth exit upward floating fade
        launch {
            alphaAnim.animateTo(0f, tween(300))
        }
        launch {
            offsetYAnim.animateTo(-35f, tween(300))
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("praise_popup_overlay"),
        contentAlignment = Alignment.Center
    ) {
        if (alphaAnim.value > 0f) {
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scaleAnim.value
                        scaleY = scaleAnim.value
                        alpha = alphaAnim.value
                        translationY = offsetYAnim.value.dp.toPx()
                    }
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                CoinGold,
                                QuestGreen,
                                QuestPink
                            )
                        )
                    )
                    .border(2.5.dp, Color.White, RoundedCornerShape(24.dp))
                    .padding(horizontal = 24.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.Black,
                        letterSpacing = 1.5.sp
                    )
                )
            }
        }
    }
}
