package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AmbientParticlesBackground
import com.example.ui.theme.QuestGreen
import com.example.ui.theme.QuestPurple
import com.example.ui.theme.QuestPurpleDeep
import com.example.ui.theme.SurfaceCardDark
import com.example.ui.theme.glassBorder
import com.example.ui.theme.glassCardColors

data class ThemeOption(
    val key: String,
    val name: String,
    val description: String,
    val previewColors: List<Color>
)

val THEME_OPTIONS = listOf(
    ThemeOption(
        key = "WORLD",
        name = "World Adaptive (Dynamic)",
        description = "Colors shift dynamically based on your current World realm",
        previewColors = listOf(Color(0xFF43D17A), Color(0xFF26D9F2), Color(0xFFFF4FA3), Color(0xFFFFD54F))
    ),
    ThemeOption(
        key = "NEON",
        name = "Neon Cyberpunk",
        description = "High-contrast electric cyan and glowing magenta highlights",
        previewColors = listOf(Color(0xFF00E5FF), Color(0xFFFF007F), Color(0xFF76FF03), Color(0xFF180A30))
    ),
    ThemeOption(
        key = "MIDNIGHT",
        name = "Midnight Nebula",
        description = "Deep dark obsidian with tranquil starlight accents",
        previewColors = listOf(Color(0xFF0A0F1D), Color(0xFF7C4DFF), Color(0xFF448AFF), Color(0xFF00E5FF))
    ),
    ThemeOption(
        key = "GOLD",
        name = "Imperial Gold",
        description = "Lustrous royal gold, amber gemstones, and ruby velvet",
        previewColors = listOf(Color(0xFFFFD700), Color(0xFFFFA000), Color(0xFFD32F2F), Color(0xFF3E2723))
    ),
    ThemeOption(
        key = "PASTEL",
        name = "Pastel Confection",
        description = "Soft, playful cotton-candy pastel aesthetics",
        previewColors = listOf(Color(0xFFFF80AB), Color(0xFF80D8FF), Color(0xFFB9F6CA), Color(0xFFFFE57F))
    )
)

@Composable
fun ThemeSelectionScreen(
    currentThemeKey: String,
    onSelectTheme: (String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(QuestPurpleDeep, Color(0xFF1D0E47), Color(0xFF0F172A))
                )
            )
            .testTag("theme_selection_screen_root")
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
                    .glassBorder(RoundedCornerShape(24.dp), borderColor = QuestPurple.copy(alpha = 0.5f))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                        .testTag("theme_selection_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Board Themes",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(THEME_OPTIONS) { opt ->
                    val isSelected = opt.key == currentThemeKey

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .glassBorder(RoundedCornerShape(20.dp), borderColor = if (isSelected) QuestGreen else QuestPurple)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onSelectTheme(opt.key) }
                            .testTag("theme_option_${opt.key}"),
                        shape = RoundedCornerShape(20.dp),
                        colors = glassCardColors(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = if (isSelected) 2.dp else 0.dp,
                                    color = if (isSelected) QuestGreen else Color.Transparent,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = opt.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                                Text(
                                    text = opt.description,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color.White.copy(alpha = 0.7f)
                                    )
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Color preview pills
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    for (color in opt.previewColors) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(color)
                                        )
                                    }
                                }
                            }

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(QuestGreen),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getThemeBackground(themeKey: String, worldGradient: List<Color>): List<Color> {
    return when (themeKey) {
        "NEON" -> listOf(Color(0xFF0A0314), Color(0xFF180A30), Color(0xFF2C1052))
        "MIDNIGHT" -> listOf(Color(0xFF04060C), Color(0xFF0A0F1D), Color(0xFF131D35))
        "GOLD" -> listOf(Color(0xFF261803), Color(0xFF3E2723), Color(0xFF5D4037))
        "PASTEL" -> listOf(Color(0xFF2D1424), Color(0xFF4A1F3B), Color(0xFF6B2B55))
        else -> worldGradient
    }
}

fun getThemeCardBackground(themeKey: String, worldCardBg: Color): Color {
    return when (themeKey) {
        "NEON" -> Color(0xFF1F0D3D)
        "MIDNIGHT" -> Color(0xFF0F172A)
        "GOLD" -> Color(0xFF3E2723)
        "PASTEL" -> Color(0xFF4A1F3B)
        else -> worldCardBg
    }
}

