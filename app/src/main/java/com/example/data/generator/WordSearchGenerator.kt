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

        // Retrieve 100% globally unique words allocated for this specific level (never used in any other level)
        val candidateWords = getUniqueWordsForLevel(
            levelNumber = levelNumber,
            worldId = worldId,
            minWordLen = minWordLen,
            maxWordLen = maxWordLen,
            targetCount = targetCount
        )

        val grid = Array(gridSize) { Array(gridSize) { ' ' } }
        val placedWords = mutableListOf<PlacedWord>()
        var colorCounter = 0
        val preferOverlap = levelNumber >= 2 || tier != DifficultyTier.NOVICE

        for (wordCandidate in candidateWords) {
            if (placedWords.size >= targetCount) break

            val word = wordCandidate.uppercase()
            var placement = tryPlaceWord(grid, word, gridSize, allowedDirections, random, preferOverlap)
            if (placement == null && preferOverlap) {
                // Fallback attempt without overlap preference if grid is crowded
                placement = tryPlaceWord(grid, word, gridSize, allowedDirections, random, preferOverlap = false)
            }
            if (placement != null) {
                placedWords.add(placement.copy(colorIndex = colorCounter++))
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

    private val levelAllocatedWordsCache = mutableMapOf<Int, List<String>>()
    private val globalUsedWordsSet = mutableSetOf<String>()
    private var highestCachedLevel = 0

    @Synchronized
    private fun getUniqueWordsForLevel(
        levelNumber: Int,
        worldId: Int,
        minWordLen: Int,
        maxWordLen: Int,
        targetCount: Int
    ): List<String> {
        val cached = levelAllocatedWordsCache[levelNumber]
        if (cached != null) return cached

        // Progressively populate up to levelNumber
        for (lvl in (highestCachedLevel + 1)..levelNumber) {
            val lvlWorld = WorldsRepository.getWorldForLevel(lvl)
            val lvlTier = DifficultyTier.fromLevel(lvl)
            val lvlMinLen = lvlTier.minWordLength
            val lvlMaxLen = minOf(lvlTier.maxWordLength, lvlTier.gridSize)
            
            val tierSpan = (lvlTier.endLevel - lvlTier.startLevel).coerceAtLeast(1)
            val progressInTier = ((lvl - lvlTier.startLevel).toFloat() / tierSpan).coerceIn(0f, 1f)
            val lvlTargetCount = (lvlTier.minWords + progressInTier * (lvlTier.maxWords - lvlTier.minWords)).toInt()
                .coerceIn(lvlTier.minWords, lvlTier.maxWords)

            val worldPool = ThemedWordBank.getWordsForWorld(lvlWorld.id)
            val lvlSeed = (lvl.toLong() * 9301L + lvlWorld.id.toLong() * 49297L)
            val poolRandom = Random(lvlWorld.id * 7919L + lvl.toLong() * 31L + lvlSeed)

            val candidates = worldPool.filter {
                it.length in lvlMinLen..lvlMaxLen && it !in globalUsedWordsSet
            }

            val countToPick = lvlTargetCount + 3
            val chosen = mutableListOf<String>()
            
            if (candidates.size >= countToPick) {
                chosen.addAll(candidates.shuffled(poolRandom).take(countToPick))
            } else {
                chosen.addAll(candidates)
                val masterFallback = ThemedWordBank.getWordsForWorld(lvlWorld.id).filter {
                    it !in globalUsedWordsSet && it !in chosen
                }
                val needed = countToPick - chosen.size
                chosen.addAll(masterFallback.shuffled(poolRandom).take(needed))
            }

            globalUsedWordsSet.addAll(chosen)
            levelAllocatedWordsCache[lvl] = chosen
        }

        highestCachedLevel = maxOf(highestCachedLevel, levelNumber)
        return levelAllocatedWordsCache[levelNumber] ?: emptyList()
    }

    private data class PlacementCandidate(
        val start: GridPos,
        val dir: DirectionType,
        val endRow: Int,
        val endCol: Int,
        val path: List<GridPos>,
        val overlapCount: Int
    )

    private fun tryPlaceWord(
        grid: Array<Array<Char>>,
        word: String,
        gridSize: Int,
        allowedDirections: List<DirectionType>,
        random: Random,
        preferOverlap: Boolean = false
    ): PlacedWord? {
        val directions = allowedDirections.shuffled(random)
        val allPositions = mutableListOf<GridPos>()
        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                allPositions.add(GridPos(r, c))
            }
        }
        val shuffledPositions = allPositions.shuffled(random)
        val validCandidates = mutableListOf<PlacementCandidate>()

        for (start in shuffledPositions) {
            for (dir in directions) {
                val endR = start.row + dir.dRow * (word.length - 1)
                val endC = start.col + dir.dCol * (word.length - 1)

                if (endR in 0 until gridSize && endC in 0 until gridSize) {
                    var canPlace = true
                    var overlapCount = 0
                    val path = mutableListOf<GridPos>()

                    for (i in word.indices) {
                        val currR = start.row + dir.dRow * i
                        val currC = start.col + dir.dCol * i
                        val cellChar = grid[currR][currC]
                        if (cellChar != ' ') {
                            if (cellChar == word[i]) {
                                overlapCount++
                            } else {
                                canPlace = false
                                break
                            }
                        }
                        path.add(GridPos(currR, currC))
                    }

                    if (canPlace) {
                        validCandidates.add(
                            PlacementCandidate(start, dir, endR, endC, path, overlapCount)
                        )
                    }
                }
            }
        }

        if (validCandidates.isEmpty()) return null

        val selected = if (preferOverlap) {
            val overlapping = validCandidates.filter { it.overlapCount > 0 }
            if (overlapping.isNotEmpty()) {
                val maxOverlap = overlapping.maxOf { it.overlapCount }
                overlapping.filter { it.overlapCount == maxOverlap }.random(random)
            } else {
                validCandidates.random(random)
            }
        } else {
            validCandidates.random(random)
        }

        // Apply word to grid
        for (i in word.indices) {
            grid[selected.path[i].row][selected.path[i].col] = word[i]
        }

        return PlacedWord(
            word = word,
            startRow = selected.start.row,
            startCol = selected.start.col,
            endRow = selected.endRow,
            endCol = selected.endCol,
            path = selected.path
        )
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
