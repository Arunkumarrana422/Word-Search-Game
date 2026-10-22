package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import com.example.ui.components.GameSwitch
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
import com.example.data.model.PlayerProfile
import com.example.ui.components.AmbientParticlesBackground
import com.example.ui.theme.QuestPurple
import com.example.ui.theme.QuestPurpleDeep
import com.example.ui.theme.SurfaceCardDark
import com.example.ui.theme.glassBorder
import com.example.ui.theme.glassCardColors

@Composable
fun SettingsScreen(
    profile: PlayerProfile,
    onSoundToggle: (Boolean) -> Unit,
    onMusicToggle: (Boolean) -> Unit,
    onHapticsToggle: (Boolean) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onReducedMotionToggle: (Boolean) -> Unit,
    onOpenThemeSelection: () -> Unit,
    onRequestReset: () -> Unit,
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
            .testTag("settings_screen_root")
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
                        .testTag("settings_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Settings",
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
                // Audio Section
                item {
                    SettingsSectionTitle("AUDIO & HAPTICS")
                }

                item {
                    SettingsToggleCard(
                        title = "Sound Effects",
                        subtitle = "Chimes, clicks, and fanfare",
                        icon = Icons.Default.VolumeUp,
                        isChecked = profile.soundEnabled,
                        onCheckedChange = onSoundToggle
                    )
                }

                item {
                    SettingsToggleCard(
                        title = "Background Music",
                        subtitle = "Gentle ambient puzzle melody",
                        icon = Icons.Default.MusicNote,
                        isChecked = profile.musicEnabled,
                        onCheckedChange = onMusicToggle
                    )
                }

                item {
                    SettingsToggleCard(
                        title = "Haptic Vibration",
                        subtitle = "Tactile feedback on tile touch",
                        icon = Icons.Default.Vibration,
                        isChecked = profile.hapticsEnabled,
                        onCheckedChange = onHapticsToggle
                    )
                }

                // Volume Slider
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .glassBorder(RoundedCornerShape(18.dp), borderColor = QuestPurple),
                        shape = RoundedCornerShape(18.dp),
                        colors = glassCardColors()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Master Volume",
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${(profile.soundVolume * 100).toInt()}%",
                                    color = QuestPurple,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Slider(
                                value = profile.soundVolume,
                                onValueChange = onVolumeChange,
                                colors = SliderDefaults.colors(
                                    thumbColor = QuestPurple,
                                    activeTrackColor = QuestPurple
                                )
                            )
                        }
                    }
                }

                // Customization Section
                item {
                    SettingsSectionTitle("VISUALS & ACCESSIBILITY")
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .glassBorder(RoundedCornerShape(18.dp), borderColor = QuestPurple)
                            .clip(RoundedCornerShape(18.dp))
                            .clickable(onClick = onOpenThemeSelection)
                            .testTag("settings_theme_button"),
                        shape = RoundedCornerShape(18.dp),
                        colors = glassCardColors()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Palette, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text("Board Theme Style", fontWeight = FontWeight.SemiBold, color = Color.White)
                                    Text("Current: ${profile.selectedBoardTheme}", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.7f))
                                }
                            }
                            Text("Change >", color = QuestPurple, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                item {
                    SettingsToggleCard(
                        title = "Reduced Motion",
                        subtitle = "Minimize floating particles and animations",
                        icon = Icons.Default.Animation,
                        isChecked = profile.reducedMotion,
                        onCheckedChange = onReducedMotionToggle
                    )
                }

                // Danger zone: Reset
                item {
                    SettingsSectionTitle("DATA & PROGRESS")
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .glassBorder(RoundedCornerShape(18.dp), borderColor = Color(0xFFFF5252))
                            .clip(RoundedCornerShape(18.dp))
                            .clickable(onClick = onRequestReset)
                            .testTag("settings_reset_progress_button"),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0x33FF5252))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteForever,
                                contentDescription = null,
                                tint = Color(0xFFFF5252)
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Reset All Game Progress",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF5252)
                                )
                                Text(
                                    text = "Clear levels, stars, coins, and achievements",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.65f)
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp,
            color = Color.White.copy(alpha = 0.6f)
        ),
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}

@Composable
private fun SettingsToggleCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .glassBorder(RoundedCornerShape(18.dp), borderColor = QuestPurple),
        shape = RoundedCornerShape(18.dp),
        colors = glassCardColors()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(imageVector = icon, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(text = title, fontWeight = FontWeight.SemiBold, color = Color.White)
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.65f)
                    )
                }
            }
            GameSwitch(
                checked = isChecked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}
