package com.example.data.model

import androidx.compose.ui.graphics.Color

/**
 * Coordinate position on the word search grid (0-indexed).
 */
data class GridPos(
    val row: Int,
    val col: Int
)

/**
 * Represents a word placed inside the grid.
 */
data class PlacedWord(
    val word: String,
    val startRow: Int,
    val startCol: Int,
    val endRow: Int,
    val endCol: Int,
    val path: List<GridPos>,
    val colorIndex: Int = 0
)

/**
 * Currently active or completed drag selection.
 */
data class WordSelection(
    val startPos: GridPos,
    val currentPos: GridPos,
    val path: List<GridPos>,
    val formedWord: String
)

/**
 * Board state for a playable level.
 */
data class BoardData(
    val levelNumber: Int,
    val worldId: Int,
    val gridSize: Int,
    val grid: List<List<Char>>,
    val targetWords: List<String>,
    val placedWords: List<PlacedWord>,
    val difficultyTier: DifficultyTier = DifficultyTier.fromLevel(levelNumber)
)

/**
 * Game Modes available.
 */
enum class GameMode(val displayName: String, val description: String) {
    CLASSIC("Classic Quest", "500 progressive levels across 10 worlds"),
    TIMED("Time Attack", "Beat the clock! Find words to gain extra time"),
    DAILY("Daily Challenge", "Unique puzzle every day with bonus streak rewards"),
    RELAX("Zen Relax", "No timer, peaceful vibes, and tranquil search"),
    CHALLENGE("Master Streak", "Consecutive puzzles with increasing difficulty")
}

/**
 * Progressive Difficulty Tiers.
 * As levels increase, grid size, word length, word count, directions,
 * and deceptive decoy letter frequencies scale up systematically.
 */
enum class DifficultyTier(
    val tierName: String,
    val gridSize: Int,
    val minWords: Int,
    val maxWords: Int,
    val minWordLength: Int,
    val maxWordLength: Int,
    val directionsAllowed: List<DirectionType>,
    val decoyRatio: Float,
    val startLevel: Int,
    val endLevel: Int,
    val color: Color
) {
    NOVICE(
        tierName = "Novice",
        gridSize = 5,
        minWords = 3,
        maxWords = 4,
        minWordLength = 3,
        maxWordLength = 4,
        directionsAllowed = listOf(DirectionType.HORIZONTAL_FORWARD, DirectionType.VERTICAL_DOWN),
        decoyRatio = 0.0f,
        startLevel = 1,
        endLevel = 10,
        color = Color(0xFF43D17A)
    ),
    EASY(
        tierName = "Easy",
        gridSize = 5,
        minWords = 4,
        maxWords = 6,
        minWordLength = 3,
        maxWordLength = 5,
        directionsAllowed = listOf(DirectionType.HORIZONTAL_FORWARD, DirectionType.VERTICAL_DOWN, DirectionType.DIAGONAL_DOWN_RIGHT),
        decoyRatio = 0.08f,
        startLevel = 11,
        endLevel = 20,
        color = Color(0xFF81C784)
    ),
    NORMAL(
        tierName = "Normal",
        gridSize = 6,
        minWords = 5,
        maxWords = 8,
        minWordLength = 4,
        maxWordLength = 6,
        directionsAllowed = listOf(
            DirectionType.HORIZONTAL_FORWARD,
            DirectionType.VERTICAL_DOWN,
            DirectionType.DIAGONAL_DOWN_RIGHT,
            DirectionType.HORIZONTAL_BACKWARD
        ),
        decoyRatio = 0.18f,
        startLevel = 21,
        endLevel = 50,
        color = Color(0xFF26D9F2)
    ),
    MEDIUM(
        tierName = "Medium",
        gridSize = 7,
        minWords = 7,
        maxWords = 10,
        minWordLength = 4,
        maxWordLength = 7,
        directionsAllowed = listOf(
            DirectionType.HORIZONTAL_FORWARD,
            DirectionType.VERTICAL_DOWN,
            DirectionType.DIAGONAL_DOWN_RIGHT,
            DirectionType.DIAGONAL_UP_RIGHT,
            DirectionType.HORIZONTAL_BACKWARD,
            DirectionType.VERTICAL_UP
        ),
        decoyRatio = 0.28f,
        startLevel = 51,
        endLevel = 100,
        color = Color(0xFFFFD54F)
    ),
    HARD(
        tierName = "Hard",
        gridSize = 8,
        minWords = 8,
        maxWords = 10,
        minWordLength = 5,
        maxWordLength = 8,
        directionsAllowed = DirectionType.values().toList(),
        decoyRatio = 0.38f,
        startLevel = 101,
        endLevel = 200,
        color = Color(0xFFFF7043)
    ),
    EXPERT(
        tierName = "Expert",
        gridSize = 9,
        minWords = 8,
        maxWords = 10,
        minWordLength = 5,
        maxWordLength = 9,
        directionsAllowed = DirectionType.values().toList(),
        decoyRatio = 0.48f,
        startLevel = 201,
        endLevel = 350,
        color = Color(0xFFE040FB)
    ),
    MASTER(
        tierName = "Master",
        gridSize = 10,
        minWords = 8,
        maxWords = 10,
        minWordLength = 5,
        maxWordLength = 10,
        directionsAllowed = DirectionType.values().toList(),
        decoyRatio = 0.58f,
        startLevel = 351,
        endLevel = 500,
        color = Color(0xFFFFD700)
    );

    companion object {
        fun fromLevel(level: Int): DifficultyTier = when (level) {
            in 1..10 -> NOVICE
            in 11..20 -> EASY
            in 21..50 -> NORMAL
            in 51..100 -> MEDIUM
            in 101..200 -> HARD
            in 201..350 -> EXPERT
            else -> MASTER
        }
    }
}

/**
 * 8 Word Search Placement Directions.
 */
enum class DirectionType(val dRow: Int, val dCol: Int, val label: String) {
    HORIZONTAL_FORWARD(0, 1, "Right"),
    HORIZONTAL_BACKWARD(0, -1, "Left"),
    VERTICAL_DOWN(1, 0, "Down"),
    VERTICAL_UP(-1, 0, "Up"),
    DIAGONAL_DOWN_RIGHT(1, 1, "Down-Right"),
    DIAGONAL_DOWN_LEFT(1, -1, "Down-Left"),
    DIAGONAL_UP_RIGHT(-1, 1, "Up-Right"),
    DIAGONAL_UP_LEFT(-1, -1, "Up-Left")
}

/**
 * Distinct vibrant highlight colors for found words.
 */
val WordHighlightColors = listOf(
    Color(0xFF26D9F2), // Electric Cyan
    Color(0xFFFF4FA3), // Hot Pink
    Color(0xFF43D17A), // Vibrant Emerald
    Color(0xFFFFD54F), // Sunburst Gold
    Color(0xFF7C4DFF), // Royal Purple
    Color(0xFFFF7043), // Tangerine Coral
    Color(0xFF42A5F5), // Sky Blue
    Color(0xFFE040FB), // Neon Magenta
    Color(0xFF76FF03), // Lime Flash
    Color(0xFFFF5252), // Crimson
    Color(0xFF00E5FF), // Aqua
    Color(0xFFFFAB40), // Amber
    Color(0xFFB388FF), // Lavender
    Color(0xFF69F0AE), // Mint Green
    Color(0xFFFF80AB), // Rose Pink
    Color(0xFFFFD740)  // Goldenrod
)
