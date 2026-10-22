package com.example.audio

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

/**
 * Provides gentle, pleasant tactile haptic feedback for Word Quest.
 */
class HapticsManager(context: Context) {

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        manager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    @Volatile
    var isHapticsEnabled: Boolean = true

    fun vibrateButton() {
        if (!isHapticsEnabled || vibrator == null || !vibrator.hasVibrator()) return
        vibrateOneShot(12, 100)
    }

    fun vibrateLetterSelect() {
        if (!isHapticsEnabled || vibrator == null || !vibrator.hasVibrator()) return
        vibrateOneShot(15, 80)
    }

    fun vibrateWordFound() {
        if (!isHapticsEnabled || vibrator == null || !vibrator.hasVibrator()) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val timings = longArrayOf(0, 35, 40, 55)
            val amplitudes = intArrayOf(0, 180, 0, 255)
            vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(70)
        }
    }

    fun vibrateIncorrect() {
        if (!isHapticsEnabled || vibrator == null || !vibrator.hasVibrator()) return
        vibrateOneShot(45, 120)
    }

    fun vibrateLevelComplete() {
        if (!isHapticsEnabled || vibrator == null || !vibrator.hasVibrator()) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val timings = longArrayOf(0, 40, 50, 40, 50, 80)
            val amplitudes = intArrayOf(0, 150, 0, 200, 0, 255)
            vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(150)
        }
    }

    private fun vibrateOneShot(durationMs: Long, amplitude: Int) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, amplitude))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMs)
            }
        } catch (_: Exception) {
            // Silently ignore if device disables vibration in battery saver
        }
    }
}
