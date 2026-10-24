package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

/**
 * High-performance, offline audio synthesizer for Word Quest.
 * Generates crisp musical tones, chords, coin dings, and victory fanfare
 * directly via AudioTrack without requiring heavy external asset files.
 */
class SoundManager {

    private val sampleRate = 22050
    private val scope = CoroutineScope(Dispatchers.Default)

    @Volatile
    var isSoundEnabled: Boolean = true

    @Volatile
    var masterVolume: Float = 0.8f

    fun playButtonClick() {
        if (!isSoundEnabled) return
        scope.launch {
            // Crisp gentle click (800Hz to 1200Hz sweep for 35ms)
            playSweepTone(800.0, 1200.0, 35, 0.5f)
        }
    }

    fun playLetterSelect() {
        if (!isSoundEnabled) return
        scope.launch {
            // Soft woodblock tap (440Hz short tap 25ms)
            playTone(520.0, 25, 0.35f)
        }
    }

    fun playWordFound() {
        if (!isSoundEnabled) return
        scope.launch {
            // Upward melodic arpeggio chime (C5, E5, G5, C6)
            playArpeggio(listOf(523.25, 659.25, 783.99, 1046.50), 70, 0.7f)
        }
    }

    fun playWordIncorrect() {
        if (!isSoundEnabled) return
        scope.launch {
            // Low soft error buzz (220Hz to 180Hz)
            playSweepTone(220.0, 160.0, 110, 0.45f)
        }
    }

    fun playCoinCollect() {
        if (!isSoundEnabled) return
        scope.launch {
            // High metallic coin ding (987.77Hz to 1318.51Hz)
            playArpeggio(listOf(987.77, 1318.51), 60, 0.7f)
        }
    }

    fun playHintUsed() {
        if (!isSoundEnabled) return
        scope.launch {
            // Magical shimmer sound
            playArpeggio(listOf(659.25, 830.61, 987.77, 1318.51), 50, 0.6f)
        }
    }

    fun playLevelComplete() {
        if (!isSoundEnabled) return
        scope.launch {
            // Grand victory fanfare chords
            val notes = listOf(523.25, 659.25, 783.99, 1046.50, 1318.51)
            playArpeggio(notes, 100, 0.85f)
        }
    }

    fun playStarReward() {
        if (!isSoundEnabled) return
        scope.launch {
            // Bright sparkling star ding
            playSweepTone(1046.5, 1567.98, 80, 0.65f)
        }
    }

    fun playWorldUnlock() {
        if (!isSoundEnabled) return
        scope.launch {
            // Epic brass-like fanfare
            playArpeggio(listOf(440.0, 554.37, 659.25, 880.0, 1108.73), 120, 0.85f)
        }
    }

    fun playAchievementUnlocked() {
        if (!isSoundEnabled) return
        scope.launch {
            // Celebration triumph chime
            playArpeggio(listOf(587.33, 739.99, 880.0, 1174.66), 80, 0.8f)
        }
    }

    fun playPraisePop() {
        if (!isSoundEnabled) return
        scope.launch {
            // Cheerful bright double-ding praise chime (E5 -> A5 -> E6)
            playArpeggio(listOf(659.25, 880.0, 1318.51), 55, 0.75f)
        }
    }

    private fun playTone(freqHz: Double, durationMs: Int, volume: Float) {
        val numSamples = (durationMs * sampleRate) / 1000
        val buffer = ShortArray(numSamples)
        val vol = (volume * masterVolume).coerceIn(0f, 1f)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val decay = 1.0 - (i.toDouble() / numSamples)
            val sample = (sin(2.0 * Math.PI * freqHz * t) * decay * 32767.0 * vol).toInt()
            buffer[i] = sample.toShort()
        }
        writeAudioBuffer(buffer)
    }

    private fun playSweepTone(startFreq: Double, endFreq: Double, durationMs: Int, volume: Float) {
        val numSamples = (durationMs * sampleRate) / 1000
        val buffer = ShortArray(numSamples)
        val vol = (volume * masterVolume).coerceIn(0f, 1f)

        for (i in 0 until numSamples) {
            val progress = i.toDouble() / numSamples
            val currentFreq = startFreq + (endFreq - startFreq) * progress
            val t = i.toDouble() / sampleRate
            val decay = 1.0 - progress
            val sample = (sin(2.0 * Math.PI * currentFreq * t) * decay * 32767.0 * vol).toInt()
            buffer[i] = sample.toShort()
        }
        writeAudioBuffer(buffer)
    }

    private fun playArpeggio(frequencies: List<Double>, noteDurationMs: Int, volume: Float) {
        val samplesPerNote = (noteDurationMs * sampleRate) / 1000
        val totalSamples = samplesPerNote * frequencies.size
        val buffer = ShortArray(totalSamples)
        val vol = (volume * masterVolume).coerceIn(0f, 1f)

        var offset = 0
        for (freq in frequencies) {
            for (i in 0 until samplesPerNote) {
                val t = i.toDouble() / sampleRate
                val decay = 1.0 - (i.toDouble() / samplesPerNote)
                val sample = (sin(2.0 * Math.PI * freq * t) * decay * 32767.0 * vol).toInt()
                buffer[offset + i] = sample.toShort()
            }
            offset += samplesPerNote
        }
        writeAudioBuffer(buffer)
    }

    private fun writeAudioBuffer(buffer: ShortArray) {
        try {
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(buffer.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(buffer, 0, buffer.size)
            audioTrack.play()
            scope.launch {
                kotlinx.coroutines.delay(buffer.size * 1000L / sampleRate + 50)
                audioTrack.release()
            }
        } catch (_: Exception) {
            // AudioTrack error ignored safely
        }
    }
}
