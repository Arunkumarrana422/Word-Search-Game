package com.example

import com.example.data.generator.WordSearchGenerator
import com.example.data.model.DifficultyTier
import com.example.data.model.DirectionType
import com.example.data.model.GridPos
import com.example.data.model.WorldsRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun verify_all10WorldsSpan500LevelsContinuously() {
        val worlds = WorldsRepository.ALL_WORLDS
        assertEquals(10, worlds.size)
        assertEquals(1, worlds.first().startLevel)
        assertEquals(500, worlds.last().endLevel)

        for (i in 0 until worlds.size - 1) {
            assertEquals(worlds[i].endLevel + 1, worlds[i + 1].startLevel)
        }
    }

    @Test
    fun verify_progressiveDifficultyEscalationAcrossLevels() {
        val level1 = WordSearchGenerator.generateLevel(1)
        val level15 = WordSearchGenerator.generateLevel(15)
        val level35 = WordSearchGenerator.generateLevel(35)
        val level75 = WordSearchGenerator.generateLevel(75)
        val level150 = WordSearchGenerator.generateLevel(150)
        val level275 = WordSearchGenerator.generateLevel(275)
        val level450 = WordSearchGenerator.generateLevel(450)

        // Tiers progress
        assertEquals(DifficultyTier.NOVICE, level1.difficultyTier)
        assertEquals(DifficultyTier.EASY, level15.difficultyTier)
        assertEquals(DifficultyTier.NORMAL, level35.difficultyTier)
        assertEquals(DifficultyTier.MEDIUM, level75.difficultyTier)
        assertEquals(DifficultyTier.HARD, level150.difficultyTier)
        assertEquals(DifficultyTier.EXPERT, level275.difficultyTier)
        assertEquals(DifficultyTier.MASTER, level450.difficultyTier)

        // Grid size scales up
        assertTrue(level1.gridSize < level75.gridSize)
        assertTrue(level75.gridSize < level150.gridSize)
        assertTrue(level150.gridSize < level450.gridSize)

        // Target words count scales up
        assertTrue(level1.targetWords.size < level450.targetWords.size)

        // Directions scale up: Level 1 only has 2 directions (Right & Down), Level 450 has all 8 directions
        assertEquals(2, level1.difficultyTier.directionsAllowed.size)
        assertEquals(8, level450.difficultyTier.directionsAllowed.size)
    }

    @Test
    fun verify_level1GenerationAndWordIntegrity() {
        val board = WordSearchGenerator.generateLevel(1)
        assertEquals(1, board.levelNumber)
        assertEquals(1, board.worldId)
        assertEquals(5, board.gridSize)
        assertTrue(board.targetWords.isNotEmpty())

        // Every placed word must match exact grid letters
        for (placed in board.placedWords) {
            val wordFromGrid = placed.path.map { board.grid[it.row][it.col] }.joinToString("")
            assertEquals("Word ${placed.word} must match grid letters", placed.word, wordFromGrid)
        }
    }

    @Test
    fun verify_level100Generation() {
        val board = WordSearchGenerator.generateLevel(100)
        assertEquals(100, board.levelNumber)
        assertEquals(2, board.worldId) // Ocean World
        assertEquals(7, board.gridSize)
        assertTrue(board.targetWords.size >= 5)

        for (placed in board.placedWords) {
            val wordFromGrid = placed.path.map { board.grid[it.row][it.col] }.joinToString("")
            assertEquals(placed.word, wordFromGrid)
        }
    }

    @Test
    fun verify_level500MasterGeneration() {
        val board = WordSearchGenerator.generateLevel(500)
        assertEquals(500, board.levelNumber)
        assertEquals(10, board.worldId) // Master World
        assertEquals(10, board.gridSize)
        assertTrue(board.targetWords.size >= 8)

        for (placed in board.placedWords) {
            val wordFromGrid = placed.path.map { board.grid[it.row][it.col] }.joinToString("")
            assertEquals(placed.word, wordFromGrid)
        }
    }

    @Test
    fun verify_pathCalculationSnapping() {
        // Horizontal right
        val pathRight = WordSearchGenerator.calculateSelectionPath(GridPos(2, 1), GridPos(2, 4), 6)
        assertEquals(listOf(GridPos(2, 1), GridPos(2, 2), GridPos(2, 3), GridPos(2, 4)), pathRight)

        // Vertical down
        val pathDown = WordSearchGenerator.calculateSelectionPath(GridPos(1, 3), GridPos(3, 3), 6)
        assertEquals(listOf(GridPos(1, 3), GridPos(2, 3), GridPos(3, 3)), pathDown)

        // Diagonal down-right
        val pathDiag = WordSearchGenerator.calculateSelectionPath(GridPos(0, 0), GridPos(3, 3), 6)
        assertEquals(listOf(GridPos(0, 0), GridPos(1, 1), GridPos(2, 2), GridPos(3, 3)), pathDiag)

        // Horizontal left (reverse)
        val pathLeft = WordSearchGenerator.calculateSelectionPath(GridPos(1, 4), GridPos(1, 2), 6)
        assertEquals(listOf(GridPos(1, 4), GridPos(1, 3), GridPos(1, 2)), pathLeft)
    }

    @Test
    fun verify_zeroWordRepetitionAcrossLevels() {
        val seenWords = mutableSetOf<String>()
        // Check levels 1 through 20 for zero word overlap
        for (lvl in 1..20) {
            val board = WordSearchGenerator.generateLevel(lvl)
            for (word in board.targetWords) {
                assertTrue("Word '$word' in level $lvl was already used in a previous level!", word !in seenWords)
                seenWords.add(word)
            }
        }
    }

    @Test
    fun verify_starRatingThresholdsAndScaling() {
        val boardLvl1 = WordSearchGenerator.generateLevel(1)
        val levelIndex1 = (boardLvl1.levelNumber - 1).coerceAtLeast(0)
        val extraWords1 = (boardLvl1.targetWords.size - 3).coerceAtLeast(0)
        val extraGrid1 = (boardLvl1.gridSize - 6).coerceAtLeast(0)

        val threshold3StarsLvl1 = 20 + (levelIndex1 * 0.35f).toInt() + (extraWords1 * 3) + (extraGrid1 * 2)
        val threshold2StarsLvl1 = 50 + (levelIndex1 * 0.70f).toInt() + (extraWords1 * 6) + (extraGrid1 * 4)

        // For level 1, base 3-star threshold is <= 20s and 2-star is <= 50s
        assertTrue("Level 1 3-star threshold should be around 20s", threshold3StarsLvl1 in 18..25)
        assertTrue("Level 1 2-star threshold should be around 50s", threshold2StarsLvl1 in 48..55)

        // For higher level 50, thresholds scale up
        val boardLvl50 = WordSearchGenerator.generateLevel(50)
        val levelIndex50 = (boardLvl50.levelNumber - 1).coerceAtLeast(0)
        val extraWords50 = (boardLvl50.targetWords.size - 3).coerceAtLeast(0)
        val extraGrid50 = (boardLvl50.gridSize - 6).coerceAtLeast(0)

        val threshold3StarsLvl50 = 20 + (levelIndex50 * 0.35f).toInt() + (extraWords50 * 3) + (extraGrid50 * 2)
        val threshold2StarsLvl50 = 50 + (levelIndex50 * 0.70f).toInt() + (extraWords50 * 6) + (extraGrid50 * 4)

        assertTrue("Higher level 3-star threshold should scale up beyond 20s", threshold3StarsLvl50 > threshold3StarsLvl1)
        assertTrue("Higher level 2-star threshold should scale up beyond 50s", threshold2StarsLvl50 > threshold2StarsLvl1)
    }

    @Test
    fun verify_dailyBoardGenerationWith10WordsAndTargetWordsNotEmpty() {
        val dailyBoard = WordSearchGenerator.generateDailyBoard(worldId = 1, seed = 20261023L)
        assertEquals(10, dailyBoard.gridSize)
        assertTrue("Daily board must have target words placed", dailyBoard.targetWords.isNotEmpty())
        assertEquals(10, dailyBoard.targetWords.size)
        assertEquals(10, dailyBoard.placedWords.size)

        for (placed in dailyBoard.placedWords) {
            val wordFromGrid = placed.path.map { dailyBoard.grid[it.row][it.col] }.joinToString("")
            assertEquals("Placed word ${placed.word} must match grid letters", placed.word, wordFromGrid)
        }
    }
}
