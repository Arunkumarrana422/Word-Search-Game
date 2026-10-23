package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.HapticsManager
import com.example.audio.SoundManager
import com.example.data.generator.WordSearchGenerator
import com.example.data.model.BoardData
import com.example.data.model.GameMode
import com.example.data.model.GridPos
import com.example.data.model.PlacedWord
import com.example.data.model.PlayerProfile
import com.example.data.model.WordWorld
import com.example.data.model.WorldsRepository
import com.example.data.repository.GameRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class GameScreen {
    SPLASH,
    ONBOARDING,
    HOME,
    WORLD_SELECTION,
    LEVEL_MAP,
    GAMEPLAY,
    DAILY_CHALLENGE,
    ACHIEVEMENTS,
    STATISTICS,
    SETTINGS,
    THEME_SELECTION
}

class GameViewModel(application: Application) : AndroidViewModel(application) {

    val repository = GameRepository(application)
    val soundManager = SoundManager()
    val hapticsManager = HapticsManager(application)

    val playerProfile = repository.playerProfile
    val achievements = repository.achievements
    val statistics = repository.statistics

    private val _currentScreen = MutableStateFlow(GameScreen.SPLASH)
    val currentScreen: StateFlow<GameScreen> = _currentScreen.asStateFlow()

    private val screenBackStack = mutableListOf<GameScreen>()

    // Current Game Board State
    private val _boardData = MutableStateFlow<BoardData?>(null)
    val boardData: StateFlow<BoardData?> = _boardData.asStateFlow()

    private val _currentMode = MutableStateFlow(GameMode.CLASSIC)
    val currentMode: StateFlow<GameMode> = _currentMode.asStateFlow()

    private val _selectedWorld = MutableStateFlow(WorldsRepository.ALL_WORLDS.first())
    val selectedWorld: StateFlow<WordWorld> = _selectedWorld.asStateFlow()

    private val _foundWords = MutableStateFlow<List<PlacedWord>>(emptyList())
    val foundWords: StateFlow<List<PlacedWord>> = _foundWords.asStateFlow()

    private val _revealedLetters = MutableStateFlow<Set<GridPos>>(emptySet())
    val revealedLetters: StateFlow<Set<GridPos>> = _revealedLetters.asStateFlow()

    private val _firstLetterHints = MutableStateFlow<Set<GridPos>>(emptySet())
    val firstLetterHints: StateFlow<Set<GridPos>> = _firstLetterHints.asStateFlow()

    private val _temporaryPathHint = MutableStateFlow<List<GridPos>?>(null)
    val temporaryPathHint: StateFlow<List<GridPos>?> = _temporaryPathHint.asStateFlow()

    // Timer for Timed Challenge
    private val _timedRemainingSeconds = MutableStateFlow<Int?>(null)
    val timedRemainingSeconds: StateFlow<Int?> = _timedRemainingSeconds.asStateFlow()
    private var timerJob: Job? = null

    // Dialogs
    val showPauseDialog = MutableStateFlow(false)
    val showLevelCompleteDialog = MutableStateFlow(false)
    val showDailyRewardDialog = MutableStateFlow(false)
    val showResetConfirmDialog = MutableStateFlow(false)

    // Completion stats
    val starsEarned = MutableStateFlow(3)
    val coinsWon = MutableStateFlow(50)
    val xpWon = MutableStateFlow(80)
    val timeTakenSeconds = MutableStateFlow(0)
    private var levelStartTime = 0L
    private var hintsUsedInCurrentGame = 0

    init {
        // Sync sound and haptics settings with repository
        viewModelScope.launch {
            playerProfile.collect { profile ->
                soundManager.isSoundEnabled = profile.soundEnabled
                soundManager.masterVolume = profile.soundVolume
                hapticsManager.isHapticsEnabled = profile.hapticsEnabled
            }
        }
    }

    fun navigateTo(screen: GameScreen) {
        soundManager.playButtonClick()
        hapticsManager.vibrateButton()
        if (_currentScreen.value != screen) {
            screenBackStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        soundManager.playButtonClick()
        hapticsManager.vibrateButton()
        return if (screenBackStack.isNotEmpty()) {
            _currentScreen.value = screenBackStack.removeAt(screenBackStack.size - 1)
            true
        } else {
            if (_currentScreen.value != GameScreen.HOME) {
                _currentScreen.value = GameScreen.HOME
                true
            } else {
                false
            }
        }
    }

    fun selectWorld(world: WordWorld) {
        _selectedWorld.value = world
        navigateTo(GameScreen.LEVEL_MAP)
    }

    fun startLevel(levelNumber: Int, mode: GameMode = GameMode.CLASSIC) {
        soundManager.playButtonClick()
        hapticsManager.vibrateButton()
        _currentMode.value = mode
        _selectedWorld.value = WorldsRepository.getWorldForLevel(levelNumber)
        repository.setCurrentLevel(levelNumber)

        val newBoard = WordSearchGenerator.generateLevel(levelNumber)
        _boardData.value = newBoard
        _foundWords.value = emptyList()
        _revealedLetters.value = emptySet()
        _firstLetterHints.value = emptySet()
        _temporaryPathHint.value = null
        hintsUsedInCurrentGame = 0
        levelStartTime = System.currentTimeMillis()

        showPauseDialog.value = false
        showLevelCompleteDialog.value = false

        if (mode == GameMode.TIMED) {
            startTimer(90)
        } else {
            stopTimer()
        }

        _currentScreen.value = GameScreen.GAMEPLAY
    }

    fun startDailyChallenge() {
        soundManager.playButtonClick()
        hapticsManager.vibrateButton()
        _currentMode.value = GameMode.DAILY
        val todayStr = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
        val seed = todayStr.toLongOrNull() ?: 20261022L
        val world = WorldsRepository.ALL_WORLDS[2] // Cosmic Space for Daily
        _selectedWorld.value = world

        val board = WordSearchGenerator.generateCustomBoard(
            levelNumber = 999,
            worldId = world.id,
            gridSize = 7,
            wordCount = 7,
            seed = seed
        )
        _boardData.value = board
        _foundWords.value = emptyList()
        _revealedLetters.value = emptySet()
        _firstLetterHints.value = emptySet()
        _temporaryPathHint.value = null
        hintsUsedInCurrentGame = 0
        levelStartTime = System.currentTimeMillis()
        stopTimer()

        _currentScreen.value = GameScreen.GAMEPLAY
    }

    fun startRelaxMode() {
        startLevel(playerProfile.value.currentLevel, GameMode.RELAX)
    }

    fun startChallengeMode() {
        startLevel(1, GameMode.CHALLENGE)
    }

    private fun startTimer(seconds: Int) {
        timerJob?.cancel()
        _timedRemainingSeconds.value = seconds
        timerJob = viewModelScope.launch {
            while ((_timedRemainingSeconds.value ?: 0) > 0) {
                delay(1000)
                if (!showPauseDialog.value && !showLevelCompleteDialog.value) {
                    val remaining = (_timedRemainingSeconds.value ?: 1) - 1
                    _timedRemainingSeconds.value = remaining
                    if (remaining == 0) {
                        // Time over sound
                        soundManager.playWordIncorrect()
                        hapticsManager.vibrateIncorrect()
                        showPauseDialog.value = true
                    }
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        _timedRemainingSeconds.value = null
    }

    fun onSelectionLetterPassed() {
        soundManager.playLetterSelect()
        hapticsManager.vibrateLetterSelect()
    }

    fun onWordSelected(letters: String, path: List<GridPos>) {
        val board = _boardData.value ?: return
        val currentFound = _foundWords.value
        val alreadyFoundWords = currentFound.map { it.word }.toSet()

        val upperLetters = letters.uppercase()
        val reversedLetters = upperLetters.reversed()

        // Match against uncollected placed words
        val matchedPlaced = board.placedWords.firstOrNull { placed ->
            !alreadyFoundWords.contains(placed.word) &&
                    (placed.word == upperLetters || placed.word == reversedLetters)
        }

        if (matchedPlaced != null) {
            // Correct word!
            val updatedFound = currentFound + matchedPlaced
            _foundWords.value = updatedFound

            soundManager.playWordFound()
            hapticsManager.vibrateWordFound()

            val isDiagonal = matchedPlaced.startRow != matchedPlaced.endRow &&
                    matchedPlaced.startCol != matchedPlaced.endCol
            repository.recordWordFound(isDiagonal)

            // If in Timed mode, award +10 seconds bonus
            if (_currentMode.value == GameMode.TIMED) {
                _timedRemainingSeconds.value = (_timedRemainingSeconds.value ?: 0) + 10
            }

            // Check if level completed!
            if (updatedFound.size >= board.targetWords.size) {
                triggerLevelComplete()
            }
        } else {
            // Missed / Invalid selection
            soundManager.playWordIncorrect()
            hapticsManager.vibrateIncorrect()
        }
    }

    private fun triggerLevelComplete() {
        val board = _boardData.value ?: return
        val duration = ((System.currentTimeMillis() - levelStartTime) / 1000).toInt().coerceAtLeast(5)
        timeTakenSeconds.value = duration
        stopTimer()

        // Calculate stars with time thresholds scaling dynamically by level number
        val levelFactor = board.levelNumber.coerceIn(1, 500)
        val threshold3Stars = 45 + (levelFactor * 2)
        val threshold2Stars = 90 + (levelFactor * 3)

        val calculatedStars = when {
            hintsUsedInCurrentGame == 0 && duration <= threshold3Stars -> 3
            hintsUsedInCurrentGame <= 1 && duration <= threshold2Stars -> 2
            else -> 1
        }
        starsEarned.value = calculatedStars

        val localLevel = ((board.levelNumber - 1) % 50) + 1
        val earnedCoins = 30 + ((200 - 30) * (localLevel - 1).coerceAtMost(49) / 49)
        val earnedXp = 50 + (calculatedStars * 20)
        coinsWon.value = earnedCoins
        xpWon.value = earnedXp

        if (_currentMode.value == GameMode.DAILY) {
            val dailyReward = repository.recordDailyChallengeCompleted()
            coinsWon.value = dailyReward
        } else {
            repository.saveLevelCompletion(
                levelNumber = board.levelNumber,
                starsEarned = calculatedStars,
                timeTakenSeconds = duration,
                usedHints = hintsUsedInCurrentGame > 0
            )
        }

        soundManager.playLevelComplete()
        hapticsManager.vibrateLevelComplete()
        showLevelCompleteDialog.value = true
    }

    fun nextLevel() {
        showLevelCompleteDialog.value = false
        val current = _boardData.value?.levelNumber ?: 1
        val next = (current + 1).coerceAtMost(500)
        startLevel(next, _currentMode.value)
    }

    fun restartCurrentLevel() {
        showPauseDialog.value = false
        showLevelCompleteDialog.value = false
        val current = _boardData.value?.levelNumber ?: 1
        startLevel(current, _currentMode.value)
    }

    // Hint 1: Reveal 1 Letter (25 coins)
    fun useRevealLetterHint() {
        val board = _boardData.value ?: return
        val alreadyFound = _foundWords.value.map { it.word }.toSet()
        val unfound = board.placedWords.filter { !alreadyFound.contains(it.word) }
        if (unfound.isEmpty()) return

        if (!repository.recordHintUsed(25)) return

        hintsUsedInCurrentGame++
        soundManager.playHintUsed()
        hapticsManager.vibrateButton()

        // Pick an unfound cell not yet revealed
        val candidateCells = unfound.flatMap { it.path }
            .filter { !_revealedLetters.value.contains(it) }

        if (candidateCells.isNotEmpty()) {
            val picked = candidateCells.random()
            _revealedLetters.value = _revealedLetters.value + picked
        }
    }

    // Hint 2: First Letter of an unfound word (40 coins)
    fun useFirstLetterHint() {
        val board = _boardData.value ?: return
        val alreadyFound = _foundWords.value.map { it.word }.toSet()
        val unfound = board.placedWords.filter { !alreadyFound.contains(it.word) }
        if (unfound.isEmpty()) return

        if (!repository.recordHintUsed(40)) return

        hintsUsedInCurrentGame++
        soundManager.playHintUsed()
        hapticsManager.vibrateButton()

        val pickedWord = unfound.random()
        val firstCell = pickedWord.path.first()
        _firstLetterHints.value = _firstLetterHints.value + firstCell
    }

    // Hint 3: Path Reveal for 3 seconds (60 coins)
    fun usePathRevealHint() {
        val board = _boardData.value ?: return
        val alreadyFound = _foundWords.value.map { it.word }.toSet()
        val unfound = board.placedWords.filter { !alreadyFound.contains(it.word) }
        if (unfound.isEmpty()) return

        if (!repository.recordHintUsed(60)) return

        hintsUsedInCurrentGame++
        soundManager.playHintUsed()
        hapticsManager.vibrateButton()

        val pickedWord = unfound.random()
        _temporaryPathHint.value = pickedWord.path
        viewModelScope.launch {
            delay(3200)
            _temporaryPathHint.value = null
        }
    }

    fun claimDailyReward() {
        val amount = repository.checkAndClaimDailyLoginReward()
        if (amount != null) {
            soundManager.playCoinCollect()
            hapticsManager.vibrateButton()
        }
        showDailyRewardDialog.value = false
    }

    fun claimAchievement(id: String) {
        val reward = repository.claimAchievement(id)
        if (reward > 0) {
            soundManager.playAchievementUnlocked()
            hapticsManager.vibrateButton()
        }
    }

    fun resetProgress() {
        repository.resetAllProgress()
        showResetConfirmDialog.value = false
        soundManager.playButtonClick()
        _currentScreen.value = GameScreen.HOME
    }

    fun updateSettings(
        sound: Boolean,
        music: Boolean,
        haptics: Boolean,
        volume: Float,
        theme: String,
        reducedMotion: Boolean
    ) {
        soundManager.isSoundEnabled = sound
        soundManager.masterVolume = volume
        hapticsManager.isHapticsEnabled = haptics
        repository.updateSettings(sound, music, haptics, volume, theme, reducedMotion)
    }

    fun playVolumePreview() {
        soundManager.playButtonClick()
    }
}
