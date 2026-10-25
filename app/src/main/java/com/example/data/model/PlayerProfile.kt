package com.example.data.model

/**
 * Player Profile tracking economy, progression, settings, and daily streaks.
 */
data class PlayerProfile(
    val currentLevel: Int = 1,
    val highestUnlockedLevel: Int = 1,
    val coins: Int = 0,
    val totalStars: Int = 0,
    val xp: Int = 0,
    val playerLevel: Int = 1,
    val dailyStreak: Int = 1,
    val lastDailyChallengeDate: String = "",
    val lastRewardClaimDate: String = "",
    val consecutiveLoginDays: Int = 1,
    val soundEnabled: Boolean = true,
    val musicEnabled: Boolean = true,
    val hapticsEnabled: Boolean = false,
    val soundVolume: Float = 0.8f,
    val reducedMotion: Boolean = false,
    val selectedBoardTheme: String = "WORLD", // "WORLD", "DARK", "LIGHT", "NEON", "PASTEL"
    val completedLevelsCount: Int = 0
) {
    /**
     * XP required for next player level: e.g. Level 1 -> 100 XP, Level 2 -> 220 XP, etc.
     */
    val xpForNextLevel: Int get() = playerLevel * 120
    val xpProgressInCurrentLevel: Int get() = xp % xpForNextLevel
    val xpProgressFraction: Float get() = (xpProgressInCurrentLevel.toFloat() / xpForNextLevel.toFloat()).coerceIn(0f, 1f)
}

/**
 * Level Completion Record.
 */
data class LevelRecord(
    val levelNumber: Int,
    val stars: Int, // 1 to 3 stars
    val bestTimeSeconds: Int,
    val completedTimestamp: Long
)
