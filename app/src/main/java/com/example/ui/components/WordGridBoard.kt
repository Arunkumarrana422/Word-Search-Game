package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import com.example.data.generator.WordSearchGenerator
import com.example.data.model.BoardData
import com.example.data.model.GridPos
import com.example.data.model.PlacedWord
import com.example.data.model.WordHighlightColors
import com.example.ui.theme.glassBorder

/**
 * Interactive Word Search Letter Grid with 8-direction drag detection,
 * smooth snap trails, found word highlight capsules, and hint rings.
 */
@Composable
fun WordGridBoard(
    boardData: BoardData,
    foundWords: List<PlacedWord>,
    revealedLetters: Set<GridPos>,
    firstLetterHints: Set<GridPos>,
    temporaryPathHint: List<GridPos>?,
    boardBackgroundColor: Color,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    onWordSelected: (String, List<GridPos>) -> Unit,
    onDragLetterPassed: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridSize = boardData.gridSize
    var startCell by remember(boardData) { mutableStateOf<GridPos?>(null) }
    var currentPath by remember(boardData) { mutableStateOf<List<GridPos>>(emptyList()) }
    var lastHoverCell by remember(boardData) { mutableStateOf<GridPos?>(null) }

    // Pulsing animation for hints
    val pulseTransition = rememberInfiniteTransition(label = "hint_pulse")
    val pulseScale by pulseTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(12.dp)
            .glassBorder(shape = RoundedCornerShape(20.dp), borderColor = borderColor)
            .testTag("word_search_grid_board"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = boardBackgroundColor.copy(alpha = 0.85f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .pointerInput(boardData) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val cellSize = size.width.toFloat() / gridSize
                            val col = (offset.x / cellSize).toInt().coerceIn(0, gridSize - 1)
                            val row = (offset.y / cellSize).toInt().coerceIn(0, gridSize - 1)
                            val cell = GridPos(row, col)
                            startCell = cell
                            currentPath = listOf(cell)
                            lastHoverCell = cell
                            onDragLetterPassed(1)
                        },
                        onDragEnd = {
                            val path = currentPath
                            if (path.isNotEmpty()) {
                                val letters = path.map { boardData.grid[it.row][it.col] }.joinToString("")
                                onWordSelected(letters, path)
                            }
                            startCell = null
                            currentPath = emptyList()
                            lastHoverCell = null
                        },
                        onDragCancel = {
                            startCell = null
                            currentPath = emptyList()
                            lastHoverCell = null
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            val origin = startCell ?: return@detectDragGestures
                            val cellSize = size.width.toFloat() / gridSize
                            val col = (change.position.x / cellSize).toInt().coerceIn(0, gridSize - 1)
                            val row = (change.position.y / cellSize).toInt().coerceIn(0, gridSize - 1)
                            val hovered = GridPos(row, col)

                            val newPath = WordSearchGenerator.calculateSelectionPath(origin, hovered, gridSize)
                            if (newPath != currentPath) {
                                currentPath = newPath
                                if (hovered != lastHoverCell) {
                                    lastHoverCell = hovered
                                    onDragLetterPassed(newPath.size)
                                }
                            }
                        }
                    )
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cellSize = size.width / gridSize
                val cellCorner = cellSize * 0.22f
                val strokeThickness = cellSize * 0.76f

                // 1. Draw cell tile backdrops as circles
                for (r in 0 until gridSize) {
                    for (c in 0 until gridSize) {
                        val center = Offset((c + 0.5f) * cellSize, (r + 0.5f) * cellSize)
                        val radius = cellSize * 0.44f

                        drawCircle(
                            color = Color(0x1FFFFFFF),
                            radius = radius,
                            center = center
                        )
                    }
                }

                // 2. Draw Found Words (Color highlight capsules)
                for (placed in foundWords) {
                    val color = WordHighlightColors[placed.colorIndex % WordHighlightColors.size]
                    drawWordCapsule(placed.path, cellSize, color.copy(alpha = 0.58f), strokeThickness)
                }

                // 3. Draw Temporary Path Hint (if active)
                if (temporaryPathHint != null && temporaryPathHint.isNotEmpty()) {
                    drawWordCapsule(
                        temporaryPathHint,
                        cellSize,
                        Color(0xFFFFD54F).copy(alpha = 0.7f * pulseScale.coerceAtMost(1f)),
                        strokeThickness * 0.85f
                    )
                }

                // 4. Draw Active Drag Selection Capsule
                if (currentPath.isNotEmpty()) {
                    drawWordCapsule(
                        currentPath,
                        cellSize,
                        Color(0xFF26D9F2).copy(alpha = 0.55f),
                        strokeThickness
                    )
                }

                // 5. Draw Special Hint Rings (revealed single letters or first letters)
                for (pos in revealedLetters) {
                    val center = Offset((pos.col + 0.5f) * cellSize, (pos.row + 0.5f) * cellSize)
                    drawCircle(
                        color = Color(0xFFFFD54F).copy(alpha = 0.45f),
                        radius = (cellSize * 0.42f) * pulseScale,
                        center = center
                    )
                }
                for (pos in firstLetterHints) {
                    val center = Offset((pos.col + 0.5f) * cellSize, (pos.row + 0.5f) * cellSize)
                    drawCircle(
                        color = Color(0xFFFF4FA3).copy(alpha = 0.6f),
                        radius = (cellSize * 0.44f) * pulseScale,
                        center = center
                    )
                }

                // 6. Draw Letters (Native Android Canvas for crisp text rendering)
                val paint = android.graphics.Paint().apply {
                    color = android.graphics.Color.WHITE
                    textSize = cellSize * 0.52f
                    isFakeBoldText = true
                    isAntiAlias = true
                    textAlign = android.graphics.Paint.Align.CENTER
                }

                val fontMetrics = paint.fontMetrics
                val textBaselineOffset = (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent

                for (r in 0 until gridSize) {
                    for (c in 0 until gridSize) {
                        val char = boardData.grid[r][c].toString()
                        val centerX = (c + 0.5f) * cellSize
                        val centerY = (r + 0.5f) * cellSize + textBaselineOffset
                        drawContext.canvas.nativeCanvas.drawText(char, centerX, centerY, paint)
                    }
                }
            }
        }
    }
}

/**
 * Draws circular highlights for each letter cell in the word path.
 */
private fun DrawScope.drawWordCapsule(
    path: List<GridPos>,
    cellSize: Float,
    color: Color,
    strokeThickness: Float
) {
    if (path.isEmpty()) return

    // Draw connecting lines between consecutive cells for smooth flow
    if (path.size > 1) {
        for (i in 0 until path.size - 1) {
            val p1 = Offset((path[i].col + 0.5f) * cellSize, (path[i].row + 0.5f) * cellSize)
            val p2 = Offset((path[i + 1].col + 0.5f) * cellSize, (path[i + 1].row + 0.5f) * cellSize)
            drawLine(
                color = color,
                start = p1,
                end = p2,
                strokeWidth = strokeThickness,
                cap = StrokeCap.Round
            )
        }
    }

    // Draw a circle on each letter cell in the path
    for (pos in path) {
        val center = Offset((pos.col + 0.5f) * cellSize, (pos.row + 0.5f) * cellSize)
        drawCircle(
            color = color,
            radius = strokeThickness / 2,
            center = center
        )
    }
}
