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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AmbientParticlesBackground
import com.example.ui.components.AppVectorIcons
import com.example.ui.theme.QuestGreen
import com.example.ui.theme.QuestPurple
import com.example.ui.theme.QuestPurpleDeep
import com.example.ui.theme.SurfaceCardDark
import com.example.ui.theme.glassBorder

@Composable
fun OnboardingScreen(
    onGotIt: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(QuestPurpleDeep, Color(0xFF1E1045), Color(0xFF0F172A))
                )
            )
            .testTag("onboarding_screen_root")
    ) {
        AmbientParticlesBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Header Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.Black.copy(alpha = 0.28f))
                    .glassBorder(RoundedCornerShape(24.dp), borderColor = QuestGreen.copy(alpha = 0.5f))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "How to Play",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    TutorialStepCard(
                        icon = AppVectorIcons.Touch,
                        title = "1. Drag to Select Words",
                        description = "Touch the first letter of a target word and drag across the letters to select it. Release your finger to confirm."
                    )
                }
                item {
                    TutorialStepCard(
                        icon = AppVectorIcons.Compass,
                        title = "2. Search in All 8 Directions",
                        description = "Words can hide horizontally, vertically, diagonally, and even in reverse! Keep your eyes sharp for tricky paths."
                    )
                }
                item {
                    TutorialStepCard(
                        icon = AppVectorIcons.Palette,
                        title = "3. Colorful Highlights",
                        description = "Found words are struck off the list and highlighted on the board in vivid glowing colors."
                    )
                }
                item {
                    TutorialStepCard(
                        icon = AppVectorIcons.LightbulbIcon,
                        title = "4. Helpful Hints",
                        description = "Stuck? Use in-game coins to reveal a single letter, pinpoint the first letter, or highlight an entire word trail."
                    )
                }
                item {
                    TutorialStepCard(
                        icon = AppVectorIcons.StarsSet,
                        title = "5. 500 Progressive Levels",
                        description = "Advance across 10 worlds with increasing board sizes and richer vocabularies. Earn stars, coins, and trophies!"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onGotIt,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("onboarding_got_it_button"),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = QuestGreen)
            ) {
                Text(
                    text = "READY TO PLAY!",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                )
            }
        }
    }
}

@Composable
private fun TutorialStepCard(icon: ImageVector, title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCardDark.copy(alpha = 0.85f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(QuestPurple.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White.copy(alpha = 0.8f),
                        lineHeight = 18.sp
                    )
                )
            }
        }
    }
}
