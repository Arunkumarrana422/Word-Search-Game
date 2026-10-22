package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

private data class FloatingParticle(
    val initialX: Float,
    val initialY: Float,
    val radius: Float,
    val speedY: Float,
    val color: Color,
    val alpha: Float
)

/**
 * Gentle floating ambient particles for the game background.
 */
@Composable
fun AmbientParticlesBackground(
    modifier: Modifier = Modifier,
    particleCount: Int = 24,
    colors: List<Color> = listOf(
        Color(0x3326D9F2),
        Color(0x33FF4FA3),
        Color(0x33FFD54F),
        Color(0x337C4DFF),
        Color(0x3343D17A)
    )
) {
    val particles = remember(particleCount) {
        val random = Random(42)
        List(particleCount) {
            FloatingParticle(
                initialX = random.nextFloat(),
                initialY = random.nextFloat(),
                radius = random.nextFloat() * 6f + 3f,
                speedY = random.nextFloat() * 0.15f + 0.05f,
                color = colors[random.nextInt(colors.size)],
                alpha = random.nextFloat() * 0.4f + 0.2f
            )
        }
    }

    val transition = rememberInfiniteTransition(label = "ambient_particles")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particle_motion"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        for (p in particles) {
            val currentY = ((p.initialY - progress * p.speedY) % 1.0f + 1.0f) % 1.0f
            val currentX = (p.initialX + kotlin.math.sin((progress * 2 * Math.PI + p.initialY * 5).toFloat()) * 0.03f).coerceIn(0f, 1f)

            drawCircle(
                color = p.color.copy(alpha = p.alpha),
                radius = p.radius,
                center = Offset(currentX * w, currentY * h)
            )
        }
    }
}
