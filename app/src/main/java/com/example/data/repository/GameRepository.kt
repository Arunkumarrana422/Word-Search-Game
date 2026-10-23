package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.Achievement
import com.example.data.model.DefaultAchievements
import com.example.data.model.GameStatistics
import com.example.data.model.PlayerProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GameRepository(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("word_quest_game_prefs", Context.MODE_PRIVATE)

    private val _playerProfile = MutableStateFlow(loadPlayerProfile())
    val playerProfile: StateFlow<PlayerProfile> = _playerProfile.asStateFlow()

    private val _achievements = MutableStateFlow(loadAchievements())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

    private val _statistics = MutableStateFlow(loadStatistics())
    val statistics: StateFlow<GameStatistics> = _statistics.asStateFlow()

    private fun loadPlayerProfile(): PlayerProfile {
        return PlayerProfile(
            currentLevel = prefs.getInt("current_level", 1),
            highestUnlockedLevel = prefs.getInt("highest_unlocked_level", 1),
            coins = prefs.getInt("coins", 250),
            totalStars = prefs.getInt("total_stars", 0),
            xp = prefs.getInt("xp", 0),
            playerLevel = prefs.getInt("player_level", 1),
            dailyStreak = prefs.getInt("daily_streak", 1),
            lastDailyChallengeDate = prefs.getString("last_daily_challenge_date", "") ?: "",
            lastRewardClaimDate = prefs.getString("last_reward_claim_date", "") ?: "",
            consecutiveLoginDays = prefs.getInt("consecutive_login_days", 1),
            soundEnabled = prefs.getBoolean("sound_enabled", true),
            musicEnabled = prefs.getBoolean("music_enabled", true),
            hapticsEnabled = prefs.getBoolean("haptics_enabled", true),
            soundVolume = prefs.getFloat("sound_volume", 0.8f),
            reducedMotion = prefs.getBoolean("reduced_motion", false),
            selectedBoardTheme = prefs.getString("selected_board_theme", "WORLD") ?: "WORLD",
            completedLevelsCount = prefs.getInt("completed_levels_count", 0)
        )
    }

    private fun savePlayerProfile(profile: PlayerProfile) {
        prefs.edit()
            .putInt("current_level", profile.currentLevel)
            .putInt("highest_unlocked_level", profile.highestUnlockedLevel)
            .putInt("coins", profile.coins)
            .putInt("total_stars", profile.totalStars)
            .putInt("xp", profile.xp)
            .putInt("player_level", profile.playerLevel)
            .putInt("daily_streak", profile.dailyStreak)
            .putString("last_daily_challenge_date", profile.lastDailyChallengeDate)
            .putString("last_reward_claim_date", profile.lastRewardClaimDate)
            .putInt("consecutive_login_days", profile.consecutiveLoginDays)
            .putBoolean("sound_enabled", profile.soundEnabled)
            .putBoolean("music_enabled", profile.musicEnabled)
            .putBoolean("hapticsEnabled", profile.hapticsEnabled)
            .putFloat("sound_volume", profile.soundVolume)
            .putBoolean("reduced_motion", profile.reducedMotion)
            .putString("selected_board_theme", profile.selectedBoardTheme)
            .putInt("completed_levels_count", profile.completedLevelsCount)
            .apply()
        _playerProfile.value = profile
    }

    fun getLevelStars(levelNumber: Int): Int {
        return prefs.getInt("level_stars_$levelNumber", 0)
    }

    fun saveLevelCompletion(levelNumber: Int, starsEarned: Int, timeTakenSeconds: Int, usedHints: Boolean) {
        val currentStars = getLevelStars(levelNumber)
        val bestStars = maxOf(currentStars, starsEarned)
        val isFirstCompletion = currentStars == 0

        prefs.edit().putInt("level_stars_$levelNumber", bestStars).apply()

        val oldProfile = _playerProfile.value
        val newlyEarnedStars = if (isFirstCompletion) starsEarned else maxOf(0, starsEarned - currentStars)
        val localLevel = ((levelNumber - 1) % 50) + 1
        val coinsReward = 30 + ((200 - 30) * (localLevel - 1).coerceAtMost(49) / 49)
        val xpReward = 50 + (starsEarned * 15)

        val newTotalStars = oldProfile.totalStars + newlyEarnedStars
        val newCoins = oldProfile.coins + coinsReward
        val newXp = oldProfile.xp + xpReward
        val newPlayerLevel = 1 + (newXp / 120)
        val nextLevel = maxOf(oldProfile.highestUnlockedLevel, levelNumber + 1).coerceAtMost(500)
        val completedCount = if (isFirstCompletion) oldProfile.completedLevelsCount + 1 else oldProfile.completedLevelsCount

        val updatedProfile = oldProfile.copy(
            currentLevel = levelNumber,
            highestUnlockedLevel = nextLevel,
            totalStars = newTotalStars,
            coins = newCoins,
            xp = newXp,
            playerLevel = newPlayerLevel,
            completedLevelsCount = completedCount
        )
        savePlayerProfile(updatedProfile)

        // Update Stats
        val oldStats = _statistics.value
        val fastest = if (oldStats.fastestSolveSeconds == 0) timeTakenSeconds else minOf(oldStats.fastestSolveSeconds, timeTakenSeconds)
        val perfectInc = if (!usedHints) 1 else 0

        val newStats = oldStats.copy(
            totalGamesPlayed = oldStats.totalGamesPlayed + 1,
            totalGamesWon = oldStats.totalGamesWon + 1,
            totalTimeSpentSeconds = oldStats.totalTimeSpentSeconds + timeTakenSeconds,
            fastestSolveSeconds = fastest,
            perfectNoHintGames = oldStats.perfectNoHintGames + perfectInc
        )
        saveStatistics(newStats)

        // Update Achievements
        updateAchievementProgress("level_5", completedCount)
        updateAchievementProgress("level_20", completedCount)
        updateAchievementProgress("level_50", completedCount)
        updateAchievementProgress("level_100", completedCount)
        updateAchievementProgress("stars_50", newTotalStars)
        updateAchievementProgress("stars_150", newTotalStars)
        if (timeTakenSeconds <= 30) {
            updateAchievementProgress("speedy", 1)
        }
        if (!usedHints) {
            updateAchievementProgress("hintless_10", newStats.perfectNoHintGames)
        }
    }

    fun recordWordFound(isDiagonal: Boolean) {
        val oldStats = _statistics.value
        val newWords = oldStats.totalWordsFound + 1
        val newDiag = if (isDiagonal) oldStats.diagonalWordsFound + 1 else oldStats.diagonalWordsFound
        val newStats = oldStats.copy(
            totalWordsFound = newWords,
            diagonalWordsFound = newDiag
        )
        saveStatistics(newStats)

        updateAchievementProgress("first_step", 1)
        updateAchievementProgress("words_50", newWords)
        updateAchievementProgress("words_200", newWords)
        updateAchievementProgress("words_500", newWords)
        if (isDiagonal) {
            updateAchievementProgress("diagonal_pro", newDiag)
        }
    }

    fun recordHintUsed(cost: Int): Boolean {
        val profile = _playerProfile.value
        if (profile.coins < cost) return false

        savePlayerProfile(profile.copy(coins = profile.coins - cost))
        val oldStats = _statistics.value
        saveStatistics(oldStats.copy(totalHintsUsed = oldStats.totalHintsUsed + 1))
        return true
    }

    fun addCoins(amount: Int) {
        val p = _playerProfile.value
        savePlayerProfile(p.copy(coins = p.coins + amount))
    }

    fun updateSettings(
        sound: Boolean = _playerProfile.value.soundEnabled,
        music: Boolean = _playerProfile.value.musicEnabled,
        haptics: Boolean = _playerProfile.value.hapticsEnabled,
        volume: Float = _playerProfile.value.soundVolume,
        theme: String = _playerProfile.value.selectedBoardTheme,
        reducedMotion: Boolean = _playerProfile.value.reducedMotion
    ) {
        val p = _playerProfile.value.copy(
            soundEnabled = sound,
            musicEnabled = music,
            hapticsEnabled = haptics,
            soundVolume = volume,
            selectedBoardTheme = theme,
            reducedMotion = reducedMotion
        )
        savePlayerProfile(p)
    }

    fun setCurrentLevel(level: Int) {
        val p = _playerProfile.value.copy(currentLevel = level)
        savePlayerProfile(p)
    }

    fun checkAndClaimDailyLoginReward(): Int? {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val profile = _playerProfile.value
        if (profile.lastRewardClaimDate == today) {
            return null // Already claimed today
        }

        val days = (profile.consecutiveLoginDays % 7) + 1
        val rewardAmount = days * 50
        val updated = profile.copy(
            coins = profile.coins + rewardAmount,
            lastRewardClaimDate = today,
            consecutiveLoginDays = days
        )
        savePlayerProfile(updated)
        return rewardAmount
    }

    fun recordDailyChallengeCompleted(): Int {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val profile = _playerProfile.value
        val reward = 100
        val newStreak = profile.dailyStreak + 1
        val updated = profile.copy(
            coins = profile.coins + reward,
            dailyStreak = newStreak,
            lastDailyChallengeDate = today
        )
        savePlayerProfile(updated)

        val oldStats = _statistics.value
        val newDailyCount = oldStats.totalDailyCompleted + 1
        saveStatistics(oldStats.copy(
            totalDailyCompleted = newDailyCount,
            highestDailyStreak = maxOf(oldStats.highestDailyStreak, newStreak)
        ))
        updateAchievementProgress("streak_3", newDailyCount)
        return reward
    }

    fun resetAllProgress() {
        prefs.edit().clear().apply()
        _playerProfile.value = loadPlayerProfile()
        _achievements.value = DefaultAchievements.createInitial()
        _statistics.value = GameStatistics()
    }

    private fun loadAchievements(): List<Achievement> {
        val initial = DefaultAchievements.createInitial()
        return initial.map { ach ->
            val prog = prefs.getInt("ach_prog_${ach.id}", 0)
            val claimed = prefs.getBoolean("ach_claimed_${ach.id}", false)
            ach.copy(currentProgress = prog, isClaimed = claimed)
        }
    }

    private fun updateAchievementProgress(id: String, progress: Int) {
        val list = _achievements.value.map { ach ->
            if (ach.id == id) {
                val newProg = maxOf(ach.currentProgress, progress)
                prefs.edit().putInt("ach_prog_${ach.id}", newProg).apply()
                ach.copy(currentProgress = newProg)
            } else {
                ach
            }
        }
        _achievements.value = list
    }

    fun claimAchievement(id: String): Int {
        var reward = 0
        val list = _achievements.value.map { ach ->
            if (ach.id == id && ach.isCompleted && !ach.isClaimed) {
                reward = ach.rewardCoins
                prefs.edit().putBoolean("ach_claimed_${ach.id}", true).apply()
                ach.copy(isClaimed = true)
            } else {
                ach
            }
        }
        if (reward > 0) {
            _achievements.value = list
            addCoins(reward)
        }
        return reward
    }

    private fun loadStatistics(): GameStatistics {
        return GameStatistics(
            totalGamesPlayed = prefs.getInt("stat_games_played", 0),
            totalGamesWon = prefs.getInt("stat_games_won", 0),
            totalWordsFound = prefs.getInt("stat_words_found", 0),
            totalTimeSpentSeconds = prefs.getLong("stat_time_spent", 0L),
            fastestSolveSeconds = prefs.getInt("stat_fastest_solve", 0),
            totalHintsUsed = prefs.getInt("stat_hints_used", 0),
            totalDailyCompleted = prefs.getInt("stat_daily_completed", 0),
            highestDailyStreak = prefs.getInt("stat_highest_streak", 0),
            diagonalWordsFound = prefs.getInt("stat_diagonal_words", 0),
            perfectNoHintGames = prefs.getInt("stat_no_hint_games", 0)
        )
    }

    private fun saveStatistics(stats: GameStatistics) {
        prefs.edit()
            .putInt("stat_games_played", stats.totalGamesPlayed)
            .putInt("stat_games_won", stats.totalGamesWon)
            .putInt("stat_words_found", stats.totalWordsFound)
            .putLong("stat_time_spent", stats.totalTimeSpentSeconds)
            .putInt("stat_fastest_solve", stats.fastestSolveSeconds)
            .putInt("stat_hints_used", stats.totalHintsUsed)
            .putInt("stat_daily_completed", stats.totalDailyCompleted)
            .putInt("stat_highest_streak", stats.highestDailyStreak)
            .putInt("stat_diagonal_words", stats.diagonalWordsFound)
            .putInt("stat_no_hint_games", stats.perfectNoHintGames)
            .apply()
        _statistics.value = stats
    }
}
