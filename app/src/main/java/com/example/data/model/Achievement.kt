package com.example.data.model

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val target: Int,
    val currentProgress: Int,
    val rewardCoins: Int,
    val isClaimed: Boolean
) {
    val isCompleted: Boolean get() = currentProgress >= target
    val progressFraction: Float get() = (currentProgress.toFloat() / target.toFloat()).coerceIn(0f, 1f)
}

object DefaultAchievements {
    fun createInitial(): List<Achievement> = listOf(
        Achievement("first_step", "First Word", "Find your first hidden word", 1, 0, 5, false),
        Achievement("level_5", "Curious Seeker", "Complete 5 levels", 5, 0, 10, false),
        Achievement("level_20", "Pathfinder", "Complete 20 levels", 20, 0, 15, false),
        Achievement("level_50", "World Traveler", "Complete World 1 (Level 50)", 50, 0, 20, false),
        Achievement("level_100", "Deep Explorer", "Complete World 2 (Level 100)", 100, 0, 20, false),
        Achievement("words_50", "Word Collector", "Find 50 words total", 50, 0, 10, false),
        Achievement("words_200", "Lexicon Master", "Find 200 words total", 200, 0, 15, false),
        Achievement("words_500", "Dictionary Devourer", "Find 500 words total", 500, 0, 20, false),
        Achievement("stars_50", "Starlight Seeker", "Earn 50 stars", 50, 0, 10, false),
        Achievement("stars_150", "Constellation", "Earn 150 stars", 150, 0, 20, false),
        Achievement("speedy", "Speedy Finder", "Solve any puzzle under 30 seconds", 1, 0, 10, false),
        Achievement("hintless_10", "Pure Intuition", "Complete 10 levels without using hints", 10, 0, 15, false),
        Achievement("streak_3", "Committed Quester", "Complete 3 Daily Challenges", 3, 0, 15, false),
        Achievement("diagonal_pro", "Diagonal Whisperer", "Find 30 diagonal words", 30, 0, 10, false)
    )
}

data class GameStatistics(
    val totalGamesPlayed: Int = 0,
    val totalGamesWon: Int = 0,
    val totalWordsFound: Int = 0,
    val totalTimeSpentSeconds: Long = 0,
    val fastestSolveSeconds: Int = 0,
    val totalHintsUsed: Int = 0,
    val totalDailyCompleted: Int = 0,
    val highestDailyStreak: Int = 0,
    val diagonalWordsFound: Int = 0,
    val perfectNoHintGames: Int = 0
) {
    val winRatePercent: Int get() = if (totalGamesPlayed > 0) (totalGamesWon * 100 / totalGamesPlayed) else 100
}
