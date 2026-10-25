package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.GameMode
import com.example.ui.components.AmbientParticlesBackground
import com.example.ui.components.HintActionBar
import com.example.ui.components.LevelCompleteDialog
import com.example.ui.components.PauseDialog
import com.example.ui.components.TimeoutDialog
import com.example.ui.components.TopGameBar
import com.example.ui.components.WordChipsList
import com.example.ui.components.WordGridBoard
import com.example.ui.viewmodel.GameScreen
import com.example.ui.viewmodel.GameViewModel

import com.example.ui.components.PraisePopupOverlay

@Composable
fun GameplayScreen(
    viewModel: GameViewModel,
    onBack: () -> Unit
) {
    BackHandler {
        viewModel.showPauseDialog.value = true
    }

    val boardData by viewModel.boardData.collectAsState()
    val world by viewModel.selectedWorld.collectAsState()
    val foundWords by viewModel.foundWords.collectAsState()
    val revealedLetters by viewModel.revealedLetters.collectAsState()
    val firstLetterHints by viewModel.firstLetterHints.collectAsState()
    val temporaryPathHint by viewModel.temporaryPathHint.collectAsState()
    val timedSeconds by viewModel.timedRemainingSeconds.collectAsState()
    val playerProfile by viewModel.playerProfile.collectAsState()
    val praiseMessage by viewModel.praiseMessage.collectAsState()

    val currentMode by viewModel.currentMode.collectAsState()
    val showPause by viewModel.showPauseDialog.collectAsState()
    val showComplete by viewModel.showLevelCompleteDialog.collectAsState()
    val showTimeout by viewModel.showTimeoutDialog.collectAsState()
    val starsEarned by viewModel.starsEarned.collectAsState()
    val coinsWon by viewModel.coinsWon.collectAsState()
    val xpWon by viewModel.xpWon.collectAsState()
    val timeTaken by viewModel.timeTakenSeconds.collectAsState()

    val currentBoard = boardData ?: return

    val themeKey = playerProfile.selectedBoardTheme
    val activeGradient = getThemeBackground(themeKey, world.backgroundGradient)
    val activeCardBg = getThemeCardBackground(themeKey, world.cardBackground)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(activeGradient))
            .testTag("gameplay_screen_root")
    ) {
        AmbientParticlesBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val isTimedMode = currentMode == GameMode.TIMED
            val timeAttackLvl by viewModel.currentTimeAttackLevel.collectAsState()

            // Header Bar
            TopGameBar(
                levelNumber = if (isTimedMode) timeAttackLvl else currentBoard.levelNumber,
                isDaily = currentMode == GameMode.DAILY,
                isTimed = isTimedMode,
                worldName = world.name,
                coins = playerProfile.coins,
                difficultyTier = currentBoard.difficultyTier,
                remainingSeconds = timedSeconds,
                onBackClick = { viewModel.showPauseDialog.value = true },
                onPauseClick = { viewModel.showPauseDialog.value = true }
            )

            // Letter Grid Board
            WordGridBoard(
                boardData = currentBoard,
                foundWords = foundWords,
                revealedLetters = revealedLetters,
                firstLetterHints = firstLetterHints,
                temporaryPathHint = temporaryPathHint,
                boardBackgroundColor = activeCardBg,
                borderColor = world.accentColor,
                onWordSelected = { letters, path ->
                    viewModel.onWordSelected(letters, path)
                },
                onDragLetterPassed = { pathSize ->
                    viewModel.onSelectionLetterPassed(pathSize)
                },
                modifier = Modifier.weight(1f, fill = false)
            )

            // Target Words Chips List
            WordChipsList(
                targetWords = currentBoard.targetWords,
                foundWords = foundWords,
                accentColor = world.accentColor
            )

            // Bottom Hints Bar
            HintActionBar(
                coins = playerProfile.coins,
                onUseRevealLetter = { viewModel.useRevealLetterHint() },
                onUseFirstLetter = { viewModel.useFirstLetterHint() },
                onUsePathReveal = { viewModel.usePathRevealHint() }
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Pause Menu Dialog
        if (showPause) {
            PauseDialog(
                soundEnabled = playerProfile.soundEnabled,
                hapticsEnabled = playerProfile.hapticsEnabled,
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
                onResume = { viewModel.showPauseDialog.value = false },
                onRestart = { viewModel.restartCurrentLevel() },
                onHome = {
                    viewModel.showPauseDialog.value = false
                    viewModel.navigateTo(GameScreen.HOME)
                },
                onDismiss = { viewModel.showPauseDialog.value = false }
            )
        }

        // Level Complete Celebration Dialog
        if (showComplete) {
            val isTimedMode = currentMode == GameMode.TIMED
            val timeAttackLvl by viewModel.currentTimeAttackLevel.collectAsState()
            LevelCompleteDialog(
                levelNumber = if (isTimedMode) timeAttackLvl else currentBoard.levelNumber,
                starsEarned = starsEarned,
                coinsWon = coinsWon,
                xpWon = xpWon,
                timeTakenSeconds = timeTaken,
                hasNextLevel = if (isTimedMode) timeAttackLvl < 10 else currentBoard.levelNumber < 1000,
                isTimedMode = isTimedMode,
                onNextLevel = { viewModel.nextLevel() },
                onReplay = { viewModel.restartCurrentLevel() },
                onHome = {
                    viewModel.showLevelCompleteDialog.value = false
                    viewModel.navigateTo(GameScreen.HOME)
                }
            )
        }

        // Time Out Dialog
        if (showTimeout) {
            TimeoutDialog(
                onRetry = {
                    viewModel.restartCurrentLevel()
                },
                onHome = {
                    viewModel.showTimeoutDialog.value = false
                    viewModel.navigateTo(GameScreen.HOME)
                }
            )
        }

        // Bouncy Praise Message Overlay (Awesome!, Perfect!, Genius!)
        praiseMessage?.let { msg ->
            PraisePopupOverlay(message = msg)
        }
    }
}
