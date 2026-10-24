package com.example.data.model

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Definition of the 10 themed worlds in Word Quest.
 */
data class WordWorld(
    val id: Int,
    val name: String,
    val subtitle: String,
    val description: String,
    val startLevel: Int,
    val endLevel: Int,
    val requiredStars: Int,
    val primaryColor: Color,
    val secondaryColor: Color,
    val accentColor: Color,
    val backgroundGradient: List<Color>,
    val cardBackground: Color
) {
    val totalLevels: Int get() = (endLevel - startLevel + 1)
    
    val brush: Brush get() = Brush.verticalGradient(backgroundGradient)
}

object WorldsRepository {
    val ALL_WORLDS = listOf(
        WordWorld(
            id = 1,
            name = "Nature World",
            subtitle = "Whispering Woods",
            description = "Explore lush green forests, blooming flora, and serene wilderness.",
            startLevel = 1,
            endLevel = 100,
            requiredStars = 0,
            primaryColor = Color(0xFF2E7D32),
            secondaryColor = Color(0xFF43D17A),
            accentColor = Color(0xFFAEEA00),
            backgroundGradient = listOf(Color(0xFF0F2B18), Color(0xFF1B4D2E), Color(0xFF276A40)),
            cardBackground = Color(0xFF1F4A2C)
        ),
        WordWorld(
            id = 2,
            name = "Ocean World",
            subtitle = "Azure Deep",
            description = "Dive into coral reefs, sapphire waves, and magnificent sea life.",
            startLevel = 101,
            endLevel = 200,
            requiredStars = 120,
            primaryColor = Color(0xFF0288D1),
            secondaryColor = Color(0xFF26D9F2),
            accentColor = Color(0xFF80D8FF),
            backgroundGradient = listOf(Color(0xFF041E34), Color(0xFF073B64), Color(0xFF0D5489)),
            cardBackground = Color(0xFF0A3C63)
        ),
        WordWorld(
            id = 3,
            name = "Space World",
            subtitle = "Cosmic Odyssey",
            description = "Journey through glowing nebulas, distant galaxies, and celestial orbits.",
            startLevel = 201,
            endLevel = 300,
            requiredStars = 280,
            primaryColor = Color(0xFF512DA8),
            secondaryColor = Color(0xFF7C4DFF),
            accentColor = Color(0xFFFFD54F),
            backgroundGradient = listOf(Color(0xFF0D0628), Color(0xFF1D0E47), Color(0xFF31156E)),
            cardBackground = Color(0xFF23144C)
        ),
        WordWorld(
            id = 4,
            name = "Candy World",
            subtitle = "Sugar Paradise",
            description = "Satisfy your sweet tooth with lollipops, jellybeans, and pastel confection.",
            startLevel = 301,
            endLevel = 400,
            requiredStars = 460,
            primaryColor = Color(0xFFD81B60),
            secondaryColor = Color(0xFFFF4FA3),
            accentColor = Color(0xFFFF80AB),
            backgroundGradient = listOf(Color(0xFF33081F), Color(0xFF590E38), Color(0xFF7A1950)),
            cardBackground = Color(0xFF531737)
        ),
        WordWorld(
            id = 5,
            name = "Animal World",
            subtitle = "Wild Safari",
            description = "Meet the creatures of the savanna, arctic, and tropical rainforest.",
            startLevel = 401,
            endLevel = 500,
            requiredStars = 660,
            primaryColor = Color(0xFFE65100),
            secondaryColor = Color(0xFFFF9800),
            accentColor = Color(0xFFFFD54F),
            backgroundGradient = listOf(Color(0xFF2B1403), Color(0xFF532808), Color(0xFF753B0E)),
            cardBackground = Color(0xFF4B270C)
        ),
        WordWorld(
            id = 6,
            name = "Food World",
            subtitle = "Culinary Feast",
            description = "Savor world cuisines, savory spices, bakery treats, and delicacies.",
            startLevel = 501,
            endLevel = 600,
            requiredStars = 880,
            primaryColor = Color(0xFFD32F2F),
            secondaryColor = Color(0xFFFF7043),
            accentColor = Color(0xFFFFCA28),
            backgroundGradient = listOf(Color(0xFF2C0B0B), Color(0xFF531919), Color(0xFF752424)),
            cardBackground = Color(0xFF4B1818)
        ),
        WordWorld(
            id = 7,
            name = "Travel World",
            subtitle = "Global Explorer",
            description = "Travel across world landmarks, ancient wonders, and scenic routes.",
            startLevel = 601,
            endLevel = 700,
            requiredStars = 1120,
            primaryColor = Color(0xFF00796B),
            secondaryColor = Color(0xFF00BFA5),
            accentColor = Color(0xFFFFD54F),
            backgroundGradient = listOf(Color(0xFF04221E), Color(0xFF09433B), Color(0xFF106054)),
            cardBackground = Color(0xFF0B4139)
        ),
        WordWorld(
            id = 8,
            name = "Science World",
            subtitle = "Atomic Frontier",
            description = "Unlock the secrets of physics, quantum energy, chemistry, and cyber tech.",
            startLevel = 701,
            endLevel = 800,
            requiredStars = 1380,
            primaryColor = Color(0xFF0091EA),
            secondaryColor = Color(0xFF00E5FF),
            accentColor = Color(0xFF76FF03),
            backgroundGradient = listOf(Color(0xFF031C2D), Color(0xFF073656), Color(0xFF0C5182)),
            cardBackground = Color(0xFF083756)
        ),
        WordWorld(
            id = 9,
            name = "Mystery World",
            subtitle = "Arcane Enigma",
            description = "Unravel ancient crypts, glowing runes, shadows, and occult wonders.",
            startLevel = 801,
            endLevel = 900,
            requiredStars = 1660,
            primaryColor = Color(0xFF6A1B9A),
            secondaryColor = Color(0xFFBA68C8),
            accentColor = Color(0xFF26D9F2),
            backgroundGradient = listOf(Color(0xFF1B0728), Color(0xFF330E4D), Color(0xFF4C1571)),
            cardBackground = Color(0xFF2F0F46)
        ),
        WordWorld(
            id = 10,
            name = "Master World",
            subtitle = "Crown of Legends",
            description = "The ultimate pinnacle of word puzzles. Only true masters reach here.",
            startLevel = 901,
            endLevel = 1000,
            requiredStars = 1960,
            primaryColor = Color(0xFFFF8F00),
            secondaryColor = Color(0xFFFFD54F),
            accentColor = Color(0xFFE040FB),
            backgroundGradient = listOf(Color(0xFF281A02), Color(0xFF4E3306), Color(0xFF704A0A)),
            cardBackground = Color(0xFF422E0B)
        )
    )

    fun getWorldForLevel(level: Int): WordWorld {
        return ALL_WORLDS.firstOrNull { level in it.startLevel..it.endLevel }
            ?: ALL_WORLDS.last()
    }

    fun getWorldById(id: Int): WordWorld {
        return ALL_WORLDS.firstOrNull { it.id == id } ?: ALL_WORLDS.first()
    }
}
