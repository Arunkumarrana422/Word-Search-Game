package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.GameRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read app name from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Word Quest", appName)
    }

    @Test
    fun `verify game repository persistence`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = GameRepository(context)

        val initialCoins = repo.playerProfile.value.coins
        repo.addCoins(100)
        assertEquals(initialCoins + 100, repo.playerProfile.value.coins)

        repo.saveLevelCompletion(1, 3, 45, false)
        assertEquals(3, repo.getLevelStars(1))
        assertTrue(repo.playerProfile.value.highestUnlockedLevel >= 2)
    }
}
