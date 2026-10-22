package com.example.data.generator

import com.example.data.model.BoardData
import com.example.data.model.DifficultyTier
import com.example.data.model.DirectionType
import com.example.data.model.GridPos
import com.example.data.model.PlacedWord
import com.example.data.model.WorldsRepository
import com.example.data.vocabulary.ThemedWordBank
import kotlin.random.Random

object WordSearchGenerator {

    // English letter frequency weighting for realistic board fill
    private val FREQUENT_LETTERS = "EEEEEEEAAAAAAIIIIIIOOOOOOUUUUURRRRRSTTTTTTNNNNLLLLDDCMPPBFGHVKWYXZQJ"

    /**
     * Generate a board with progressive difficulty based on level number (1 to 500).
     */
    fun generateLevel(levelNumber: Int): BoardData {
        val world = WorldsRepository.getWorldForLevel(levelNumber)
        val tier = DifficultyTier.fromLevel(levelNumber)
        val seed = (levelNumber.toLong() * 9301L + world.id.toLong() * 49297L)

        // Smooth gradual progression of word count inside each tier
        val tierSpan = (tier.endLevel - tier.startLevel).coerceAtLeast(1)
        val progressInTier = ((levelNumber - tier.startLevel).toFloat() / tierSpan).coerceIn(0f, 1f)
        val targetWordCount = (tier.minWords + progressInTier * (tier.maxWords - tier.minWords)).toInt()
            .coerceIn(tier.minWords, tier.maxWords)

        return generateBoardInternal(
            levelNumber = levelNumber,
            worldId = world.id,
            tier = tier,
            gridSize = tier.gridSize,
            targetCount = targetWordCount,
            allowedDirections = tier.directionsAllowed,
            minWordLen = tier.minWordLength,
            maxWordLen = minOf(tier.maxWordLength, tier.gridSize),
            decoyRatio = tier.decoyRatio,
            seed = seed
        )
    }

    /**
     * Generate a board for Daily Challenge or Timed Challenge.
     */
    fun generateCustomBoard(
        levelNumber: Int,
        worldId: Int,
        gridSize: Int,
        wordCount: Int,
        seed: Long
    ): BoardData {
        val tier = DifficultyTier.fromLevel(levelNumber.coerceIn(1, 500))
        return generateBoardInternal(
            levelNumber = levelNumber,
            worldId = worldId,
            tier = tier,
            gridSize = gridSize,
            targetCount = wordCount,
            allowedDirections = tier.directionsAllowed,
            minWordLen = tier.minWordLength,
            maxWordLen = minOf(tier.maxWordLength, gridSize),
            decoyRatio = tier.decoyRatio,
            seed = seed
        )
    }

    private fun generateBoardInternal(
        levelNumber: Int,
        worldId: Int,
        tier: DifficultyTier,
        gridSize: Int,
        targetCount: Int,
        allowedDirections: List<DirectionType>,
        minWordLen: Int,
        maxWordLen: Int,
        decoyRatio: Float,
        seed: Long
    ): BoardData {
        val random = Random(seed)

        // Sort & filter words by appropriate length for this level's difficulty, rotating by levelNumber to prevent repetition in consecutive levels
        val rawCandidates = ThemedWordBank.getWordsForWorld(worldId)
            .filter { it.length in minWordLen..maxWordLen }
        val rotationOffset = if (rawCandidates.isNotEmpty()) ((levelNumber - 1) * 5) % rawCandidates.size else 0
        val rotatedCandidates = if (rawCandidates.isNotEmpty()) {
            rawCandidates.drop(rotationOffset) + rawCandidates.take(rotationOffset)
        } else {
            rawCandidates
        }
        val candidateWords = rotatedCandidates.shuffled(random)

        val grid = Array(gridSize) { Array(gridSize) { ' ' } }
        val placedWords = mutableListOf<PlacedWord>()
        var colorCounter = 0

        for (wordCandidate in candidateWords) {
            if (placedWords.size >= targetCount) break

            val word = wordCandidate.uppercase()
            val placement = tryPlaceWord(grid, word, gridSize, allowedDirections, random)
            if (placement != null) {
                placedWords.add(placement.copy(colorIndex = colorCounter++))
            }
        }

        // If candidates within strict range didn't meet targetCount, try fallback length range
        if (placedWords.size < tier.minWords) {
            val fallbackCandidates = ThemedWordBank.getWordsForWorld(worldId)
                .filter { it.length in 3..gridSize && it !in placedWords.map { p -> p.word } }
                .shuffled(random)

            for (wordCandidate in fallbackCandidates) {
                if (placedWords.size >= targetCount) break
                val word = wordCandidate.uppercase()
                val placement = tryPlaceWord(grid, word, gridSize, allowedDirections, random)
                if (placement != null) {
                    placedWords.add(placement.copy(colorIndex = colorCounter++))
                }
            }
        }

        // Collect all distinct letters appearing in placed words for deceptive decoy camouflage
        val targetLettersPool = placedWords.flatMap { it.word.toList() }.distinct()

        // Fill remaining spaces with weighted letters or deceptive decoys
        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                if (grid[r][c] == ' ') {
                    if (targetLettersPool.isNotEmpty() && random.nextFloat() < decoyRatio) {
                        // Deceptive decoy letter from target words!
                        grid[r][c] = targetLettersPool[random.nextInt(targetLettersPool.size)]
                    } else {
                        grid[r][c] = FREQUENT_LETTERS[random.nextInt(FREQUENT_LETTERS.length)]
                    }
                }
            }
        }

        // Final verification pass: ensure all placed words are completely present in grid
        val verifiedPlacedWords = placedWords.filter { placed ->
            val reconstructed = placed.path.map { grid[it.row][it.col] }.joinToString("")
            reconstructed == placed.word
        }

        val immutableGrid = grid.map { it.toList() }
        val targetWordList = verifiedPlacedWords.map { it.word }

        return BoardData(
            levelNumber = levelNumber,
            worldId = worldId,
            gridSize = gridSize,
            grid = immutableGrid,
            targetWords = targetWordList,
            placedWords = verifiedPlacedWords,
            difficultyTier = tier
        )
    }

    private fun tryPlaceWord(
        grid: Array<Array<Char>>,
        word: String,
        gridSize: Int,
        allowedDirections: List<DirectionType>,
        random: Random
    ): PlacedWord? {
        val directions = allowedDirections.shuffled(random)
        val allPositions = mutableListOf<GridPos>()
        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                allPositions.add(GridPos(r, c))
            }
        }
        val shuffledPositions = allPositions.shuffled(random)

        for (start in shuffledPositions) {
            for (dir in directions) {
                val endR = start.row + dir.dRow * (word.length - 1)
                val endC = start.col + dir.dCol * (word.length - 1)

                if (endR in 0 until gridSize && endC in 0 until gridSize) {
                    var canPlace = true
                    val path = mutableListOf<GridPos>()

                    for (i in word.indices) {
                        val currR = start.row + dir.dRow * i
                        val currC = start.col + dir.dCol * i
                        val cellChar = grid[currR][currC]
                        if (cellChar != ' ' && cellChar != word[i]) {
                            canPlace = false
                            break
                        }
                        path.add(GridPos(currR, currC))
                    }

                    if (canPlace) {
                        // Apply word to grid
                        for (i in word.indices) {
                            grid[path[i].row][path[i].col] = word[i]
                        }
                        return PlacedWord(
                            word = word,
                            startRow = start.row,
                            startCol = start.col,
                            endRow = endR,
                            endCol = endC,
                            path = path
                        )
                    }
                }
            }
        }
        return null
    }

    /**
     * Compute a straight line of GridPos between two points if they align
     * along one of the 8 directions. If not an exact alignment, snaps to nearest valid direction.
     */
    fun calculateSelectionPath(
        start: GridPos,
        current: GridPos,
        gridSize: Int
    ): List<GridPos> {
        val dR = current.row - start.row
        val dC = current.col - start.col

        if (dR == 0 && dC == 0) {
            return listOf(start)
        }

        val absR = kotlin.math.abs(dR)
        val absC = kotlin.math.abs(dC)

        val stepR: Int
        val stepC: Int
        val steps: Int

        if (absR == 0) { // Horizontal
            stepR = 0
            stepC = if (dC > 0) 1 else -1
            steps = absC
        } else if (absC == 0) { // Vertical
            stepR = if (dR > 0) 1 else -1
            stepC = 0
            steps = absR
        } else {
            val ratio = absR.toFloat() / absC.toFloat()
            if (ratio in 0.5f..2.0f) {
                // Diagonal
                stepR = if (dR > 0) 1 else -1
                stepC = if (dC > 0) 1 else -1
                steps = maxOf(absR, absC)
            } else if (absR > absC) {
                // Vertical
                stepR = if (dR > 0) 1 else -1
                stepC = 0
                steps = absR
            } else {
                // Horizontal
                stepR = 0
                stepC = if (dC > 0) 1 else -1
                steps = absC
            }
        }

        val path = mutableListOf<GridPos>()
        for (i in 0..steps) {
            val r = start.row + stepR * i
            val c = start.col + stepC * i
            if (r in 0 until gridSize && c in 0 until gridSize) {
                path.add(GridPos(r, c))
            } else {
                break
            }
        }
        return path
    }
}
