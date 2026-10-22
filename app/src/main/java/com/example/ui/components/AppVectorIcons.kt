package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.data.model.DifficultyTier

/**
 * Centralized repository for all Material vector icons, ensuring zero raw emojis in the UI.
 */
object AppVectorIcons {
    val Coin: ImageVector = Icons.Default.MonetizationOn
    val Star: ImageVector = Icons.Default.Star
    val StarsSet: ImageVector = Icons.Default.Stars
    val Energy: ImageVector = Icons.Default.Bolt
    val Flame: ImageVector = Icons.Default.LocalFireDepartment
    val Trophy: ImageVector = Icons.Default.EmojiEvents
    val Gift: ImageVector = Icons.Default.CardGiftcard
    val Calendar: ImageVector = Icons.Default.CalendarToday
    val Books: ImageVector = Icons.Default.MenuBook
    val Stories: ImageVector = Icons.Default.AutoStories
    val Gamepad: ImageVector = Icons.Default.SportsEsports
    val Target: ImageVector = Icons.Default.TrackChanges
    val Sparkle: ImageVector = Icons.Default.AutoAwesome
    val Diagonal: ImageVector = Icons.Default.TrendingUp
    val LightbulbIcon: ImageVector = Icons.Default.Lightbulb
    val Touch: ImageVector = Icons.Default.TouchApp
    val Globe: ImageVector = Icons.Default.Public
    val Compass: ImageVector = Icons.Default.Explore
    val WorldMap: ImageVector = Icons.Default.Map
    val Palette: ImageVector = Icons.Default.Palette
    val BarChart: ImageVector = Icons.Default.BarChart

    fun getIconForWorld(worldId: Int): ImageVector = when (worldId) {
        1 -> Icons.Default.Park // Nature
        2 -> Icons.Default.Water // Ocean
        3 -> Icons.Default.RocketLaunch // Space
        4 -> Icons.Default.Cake // Candy
        5 -> Icons.Default.Pets // Animal
        6 -> Icons.Default.Restaurant // Food
        7 -> Icons.Default.Flight // Travel
        8 -> Icons.Default.Science // Science
        9 -> Icons.Default.Psychology // Mystery
        10 -> Icons.Default.WorkspacePremium // Master
        else -> Icons.Default.Park
    }

    fun getIconForTier(tier: DifficultyTier): ImageVector = when (tier) {
        DifficultyTier.NOVICE -> Icons.Default.Spa
        DifficultyTier.EASY -> Icons.Default.Explore
        DifficultyTier.NORMAL -> Icons.Default.Speed
        DifficultyTier.MEDIUM -> Icons.Default.Star
        DifficultyTier.HARD -> Icons.Default.Bolt
        DifficultyTier.EXPERT -> Icons.Default.Psychology
        DifficultyTier.MASTER -> Icons.Default.WorkspacePremium
    }

    fun getIconForAchievement(achievementId: String): ImageVector = when (achievementId) {
        "first_step" -> Icons.Default.AutoAwesome
        "level_5" -> Icons.Default.Explore
        "level_20" -> Icons.Default.Map
        "level_50" -> Icons.Default.Park
        "level_100" -> Icons.Default.Water
        "words_50" -> Icons.Default.MenuBook
        "words_200" -> Icons.Default.AutoStories
        "words_500" -> Icons.Default.Psychology
        "stars_50" -> Icons.Default.Star
        "stars_150" -> Icons.Default.Stars
        "speedy" -> Icons.Default.Bolt
        "hintless_10" -> Icons.Default.TrackChanges
        "streak_3" -> Icons.Default.LocalFireDepartment
        "diagonal_pro" -> Icons.Default.TrendingUp
        else -> Icons.Default.AutoAwesome
    }
}
