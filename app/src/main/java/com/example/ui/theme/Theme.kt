package com.example.ui.theme

import androidx.compose.ui.Modifier
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = QuestPurple,
    secondary = QuestCyan,
    tertiary = QuestPink,
    background = SurfaceDarkNavy,
    surface = SurfaceCardDark,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = QuestPurple,
    secondary = QuestBlue,
    tertiary = QuestPink,
    background = SurfaceLight,
    surface = SurfaceCardLight,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A)
)

fun getThemePrimaryColor(themeKey: String): Color {
    return when (themeKey) {
        "NEON" -> Color(0xFF00E5FF)
        "MIDNIGHT" -> Color(0xFF7C4DFF)
        "GOLD" -> Color(0xFFFFD700)
        "PASTEL" -> Color(0xFFFF80AB)
        else -> QuestPurple
    }
}

fun getThemeSecondaryColor(themeKey: String): Color {
    return when (themeKey) {
        "NEON" -> Color(0xFFFF007F)
        "MIDNIGHT" -> Color(0xFF448AFF)
        "GOLD" -> Color(0xFFFFA000)
        "PASTEL" -> Color(0xFF80D8FF)
        else -> QuestCyan
    }
}

fun getThemeTertiaryColor(themeKey: String): Color {
    return when (themeKey) {
        "NEON" -> Color(0xFF76FF03)
        "MIDNIGHT" -> Color(0xFF00E5FF)
        "GOLD" -> Color(0xFFD32F2F)
        "PASTEL" -> Color(0xFFB9F6CA)
        else -> QuestPink
    }
}

fun getThemeBackground(themeKey: String, worldGradient: List<Color> = listOf(QuestPurpleDeep, Color(0xFF1D0E47), Color(0xFF0F172A))): List<Color> {
    return when (themeKey) {
        "NEON" -> listOf(Color(0xFF0A0314), Color(0xFF180A30), Color(0xFF2C1052))
        "MIDNIGHT" -> listOf(Color(0xFF04060C), Color(0xFF0A0F1D), Color(0xFF131D35))
        "GOLD" -> listOf(Color(0xFF261803), Color(0xFF3E2723), Color(0xFF5D4037))
        "PASTEL" -> listOf(Color(0xFF2D1424), Color(0xFF4A1F3B), Color(0xFF6B2B55))
        else -> worldGradient
    }
}

fun getThemeCardBackground(themeKey: String, worldCardBg: Color = SurfaceCardDark): Color {
    return when (themeKey) {
        "NEON" -> Color(0xFF1F0D3D)
        "MIDNIGHT" -> Color(0xFF0F172A)
        "GOLD" -> Color(0xFF3E2723)
        "PASTEL" -> Color(0xFF4A1F3B)
        else -> worldCardBg
    }
}

@Composable
fun WordQuestTheme(
    themeKey: String = "WORLD",
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val primaryColor = getThemePrimaryColor(themeKey)
    val secondaryColor = getThemeSecondaryColor(themeKey)
    val tertiaryColor = getThemeTertiaryColor(themeKey)
    val backgroundColors = getThemeBackground(themeKey)
    val surfaceColor = getThemeCardBackground(themeKey)

    val customColorScheme = darkColorScheme(
        primary = primaryColor,
        secondary = secondaryColor,
        tertiary = tertiaryColor,
        background = backgroundColors.firstOrNull() ?: SurfaceDarkNavy,
        surface = surfaceColor,
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onTertiary = Color.White,
        onBackground = Color.White,
        onSurface = Color.White
    )

    MaterialTheme(
        colorScheme = customColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun Modifier.glassBorder(
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(16.dp),
    borderColor: Color = androidx.compose.material3.MaterialTheme.colorScheme.primary
): Modifier = this.border(
    width = 1.dp,
    brush = Brush.linearGradient(
        colors = listOf(
            borderColor.copy(alpha = 0.7f),
            androidx.compose.material3.MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
            borderColor.copy(alpha = 0.15f)
        )
    ),
    shape = shape
)

@Composable
fun glassCardColors(): androidx.compose.material3.CardColors = androidx.compose.material3.CardDefaults.cardColors(
    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface.copy(alpha = 0.75f)
)

@Composable
fun dialogCardColors(): androidx.compose.material3.CardColors = androidx.compose.material3.CardDefaults.cardColors(
    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)
)

