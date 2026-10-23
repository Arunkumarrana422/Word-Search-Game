package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.example.ui.components.DailyRewardDialog
import com.example.ui.components.ResetProgressDialog
import com.example.ui.screens.AchievementsScreen
import com.example.ui.screens.DailyChallengeScreen
import com.example.ui.screens.GameplayScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LevelMapScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.StatisticsScreen
import com.example.ui.screens.ThemeSelectionScreen
import com.example.ui.screens.WorldSelectionScreen
import com.example.ui.theme.WordQuestTheme
import androidx.activity.SystemBarStyle
import com.example.ui.viewmodel.GameScreen
import com.example.ui.viewmodel.GameViewModel

import android.widget.Toast
import android.app.Activity
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        setContent {
            val playerProfile by viewModel.playerProfile.collectAsState()
            WordQuestTheme(themeKey = playerProfile.selectedBoardTheme) {
                WordQuestApp(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun WordQuestApp(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val playerProfile by viewModel.playerProfile.collectAsState()
    val achievements by viewModel.achievements.collectAsState()
    val statistics by viewModel.statistics.collectAsState()
    val selectedWorld by viewModel.selectedWorld.collectAsState()

    val showDailyRewards by viewModel.showDailyRewardDialog.collectAsState()
    val showResetDialog by viewModel.showResetConfirmDialog.collectAsState()

    val context = LocalContext.current
    var backPressedTime by remember { mutableStateOf(0L) }

    // Exit on double back press in Home screen
    BackHandler(enabled = currentScreen == GameScreen.HOME) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime < 2000) {
            (context as? Activity)?.finish()
        } else {
            backPressedTime = currentTime
            Toast.makeText(context, "Press again to exit", Toast.LENGTH_SHORT).show()
        }
    }

    // Global back handling for other screens
    BackHandler(enabled = currentScreen != GameScreen.SPLASH && currentScreen != GameScreen.HOME) {
        viewModel.navigateBack()
    }

    when (currentScreen) {
        GameScreen.SPLASH -> {
            SplashScreen(
                onStartGame = {
                    viewModel.navigateTo(GameScreen.HOME)
                },
                onViewTutorial = {
                    viewModel.navigateTo(GameScreen.ONBOARDING)
                }
            )
        }

        GameScreen.ONBOARDING -> {
            OnboardingScreen(
                onGotIt = {
                    viewModel.navigateTo(GameScreen.HOME)
                },
                onBack = {
                    viewModel.navigateBack()
                }
            )
        }

        GameScreen.HOME -> {
            HomeScreen(
                playerProfile = playerProfile,
                achievements = achievements,
                onContinuePlaying = { level ->
                    viewModel.startLevel(level)
                },
                onSelectWorlds = {
                    viewModel.navigateTo(GameScreen.WORLD_SELECTION)
                },
                onDailyChallenge = {
                    viewModel.navigateTo(GameScreen.DAILY_CHALLENGE)
                },
                onTimeAttack = {
                    viewModel.startLevel(playerProfile.currentLevel, com.example.data.model.GameMode.TIMED)
                },
                onRelaxMode = {
                    viewModel.startRelaxMode()
                },
                onDailyRewards = {
                    viewModel.showDailyRewardDialog.value = true
                },
                onAchievements = {
                    viewModel.navigateTo(GameScreen.ACHIEVEMENTS)
                },
                onStatistics = {
                    viewModel.navigateTo(GameScreen.STATISTICS)
                },
                onSettings = {
                    viewModel.navigateTo(GameScreen.SETTINGS)
                }
            )
        }

        GameScreen.WORLD_SELECTION -> {
            WorldSelectionScreen(
                totalStars = playerProfile.totalStars,
                highestUnlockedLevel = playerProfile.highestUnlockedLevel,
                onWorldSelected = { world ->
                    viewModel.selectWorld(world)
                },
                onBack = {
                    viewModel.navigateBack()
                }
            )
        }

        GameScreen.LEVEL_MAP -> {
            LevelMapScreen(
                world = selectedWorld,
                highestUnlockedLevel = playerProfile.highestUnlockedLevel,
                repository = viewModel.repository,
                onLevelSelected = { level ->
                    viewModel.startLevel(level)
                },
                onBack = {
                    viewModel.navigateBack()
                }
            )
        }

        GameScreen.GAMEPLAY -> {
            GameplayScreen(
                viewModel = viewModel,
                onBack = {
                    viewModel.showPauseDialog.value = true
                }
            )
        }

        GameScreen.DAILY_CHALLENGE -> {
            DailyChallengeScreen(
                profile = playerProfile,
                onStartDaily = {
                    viewModel.startDailyChallenge()
                },
                onBack = {
                    viewModel.navigateBack()
                }
            )
        }

        GameScreen.ACHIEVEMENTS -> {
            AchievementsScreen(
                achievements = achievements,
                onClaimReward = { id ->
                    viewModel.claimAchievement(id)
                },
                onBack = {
                    viewModel.navigateBack()
                }
            )
        }

        GameScreen.STATISTICS -> {
            StatisticsScreen(
                statistics = statistics,
                totalStars = playerProfile.totalStars,
                completedLevels = playerProfile.completedLevelsCount,
                onBack = {
                    viewModel.navigateBack()
                }
            )
        }

        GameScreen.SETTINGS -> {
            SettingsScreen(
                profile = playerProfile,
                onSoundToggle = { enabled ->
                    viewModel.updateSettings(
                        sound = enabled,
                        music = playerProfile.musicEnabled,
                        haptics = playerProfile.hapticsEnabled,
                        volume = playerProfile.soundVolume,
                        theme = playerProfile.selectedBoardTheme,
                        reducedMotion = playerProfile.reducedMotion
                    )
                },
                onMusicToggle = { enabled ->
                    viewModel.updateSettings(
                        sound = playerProfile.soundEnabled,
                        music = enabled,
                        haptics = playerProfile.hapticsEnabled,
                        volume = playerProfile.soundVolume,
                        theme = playerProfile.selectedBoardTheme,
                        reducedMotion = playerProfile.reducedMotion
                    )
                },
                onHapticsToggle = { enabled ->
                    viewModel.updateSettings(
                        sound = playerProfile.soundEnabled,
                        music = playerProfile.musicEnabled,
                        haptics = enabled,
                        volume = playerProfile.soundVolume,
                        theme = playerProfile.selectedBoardTheme,
                        reducedMotion = playerProfile.reducedMotion
                    )
                },
                onVolumeChange = { volume ->
                    viewModel.updateSettings(
                        sound = playerProfile.soundEnabled,
                        music = playerProfile.musicEnabled,
                        haptics = playerProfile.hapticsEnabled,
                        volume = volume,
                        theme = playerProfile.selectedBoardTheme,
                        reducedMotion = playerProfile.reducedMotion
                    )
                    viewModel.playVolumePreview()
                },
                onReducedMotionToggle = { reduced ->
                    viewModel.updateSettings(
                        sound = playerProfile.soundEnabled,
                        music = playerProfile.musicEnabled,
                        haptics = playerProfile.hapticsEnabled,
                        volume = playerProfile.soundVolume,
                        theme = playerProfile.selectedBoardTheme,
                        reducedMotion = reduced
                    )
                },
                onOpenThemeSelection = {
                    viewModel.navigateTo(GameScreen.THEME_SELECTION)
                },
                onRequestReset = {
                    viewModel.showResetConfirmDialog.value = true
                },
                onBack = {
                    viewModel.navigateBack()
                }
            )
        }

        GameScreen.THEME_SELECTION -> {
            ThemeSelectionScreen(
                currentThemeKey = playerProfile.selectedBoardTheme,
                onSelectTheme = { themeKey ->
                    viewModel.updateSettings(
                        sound = playerProfile.soundEnabled,
                        music = playerProfile.musicEnabled,
                        haptics = playerProfile.hapticsEnabled,
                        volume = playerProfile.soundVolume,
                        theme = themeKey,
                        reducedMotion = playerProfile.reducedMotion
                    )
                },
                onBack = {
                    viewModel.navigateBack()
                }
            )
        }
    }

    // Daily Reward Dialog
    if (showDailyRewards) {
        val todayStr = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date())
        DailyRewardDialog(
            consecutiveDays = playerProfile.consecutiveLoginDays,
            isClaimedToday = playerProfile.lastRewardClaimDate == todayStr,
            onClaimReward = {
                viewModel.claimDailyReward()
            },
            onDismiss = {
                viewModel.showDailyRewardDialog.value = false
            }
        )
    }

    // Reset Progress Confirm Dialog
    if (showResetDialog) {
        ResetProgressDialog(
            onConfirm = {
                viewModel.resetProgress()
            },
            onDismiss = {
                viewModel.showResetConfirmDialog.value = false
            }
        )
    }
}
