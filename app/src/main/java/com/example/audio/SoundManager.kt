package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

/**
 * High-performance, low-latency offline audio synthesizer for Word Quest.
 * Generates crisp musical tones, sweet letter chimes, coin dings, and victory fanfare.
 * All synthesized audio uses smooth raised-cosine envelopes to eliminate DC offset clicks.
 */
class SoundManager {

    private val sampleRate = 22050
    private val scope = CoroutineScope(Dispatchers.Default)

    @Volatile
    var isSoundEnabled: Boolean = true

    @Volatile
    var masterVolume: Float = 0.8f

    // Ascending scale for letter selection (C Major scale - sweet glass chimes)
    private val letterScale = listOf(
        523.25,  // C5
        587.33,  // D5
        659.25,  // E5
        698.46,  // F5
        783.99,  // G5
        880.00,  // A5
        987.77,  // B5
        1046.50, // C6
        1174.66, // D6
        1318.51  // E6
    )

    fun playButtonClick() {
        if (!isSoundEnabled) return
        scope.launch {
            // Crisp gentle click (800Hz to 1200Hz sweep for 35ms)
            playSweepTone(800.0, 1200.0, 35, 0.5f)
        }
    }

    fun playLetterSelect(stepIndex: Int = 0) {
        if (!isSoundEnabled) return
        scope.launch {
            val freq = letterScale[stepIndex.coerceIn(0, letterScale.size - 1)]
            // Soft, sweet glass chime tap (35ms duration)
            playPluckTone(freq, 35, 0.35f)
        }
    }

    fun playWordFound() {
        if (!isSoundEnabled) return
        scope.launch {
            // Upward melodic arpeggio chime (C5, E5, G5, C6)
            playArpeggio(listOf(523.25, 659.25, 783.99, 1046.50), 65, 0.65f)
        }
    }

    fun playWordIncorrect() {
        if (!isSoundEnabled) return
        scope.launch {
            // Low soft double thump
            playTone(200.0, 80, 0.35f)
        }
    }

    fun playCoinCollect() {
        if (!isSoundEnabled) return
        scope.launch {
            // High metallic coin ding
            playArpeggio(listOf(987.77, 1318.51), 55, 0.65f)
        }
    }

    fun playHintUsed() {
        if (!isSoundEnabled) return
        scope.launch {
            // Magical shimmer sound
            playArpeggio(listOf(659.25, 830.61, 987.77, 1318.51), 45, 0.55f)
        }
    }

    fun playLevelComplete() {
        if (!isSoundEnabled) return
        scope.launch {
            // Grand victory fanfare notes
            val notes = listOf(523.25, 659.25, 783.99, 1046.50, 1318.51)
            playArpeggio(notes, 90, 0.75f)
        }
    }

    fun playStarReward() {
        if (!isSoundEnabled) return
        scope.launch {
            // Bright sparkling star ding
            playSweepTone(1046.5, 1567.98, 70, 0.60f)
        }
    }

    fun playWorldUnlock() {
        if (!isSoundEnabled) return
        scope.launch {
            // Fanfare
            playArpeggio(listOf(440.0, 554.37, 659.25, 880.0, 1108.73), 110, 0.75f)
        }
    }

    fun playAchievementUnlocked() {
        if (!isSoundEnabled) return
        scope.launch {
            // Celebration chime
            playArpeggio(listOf(587.33, 739.99, 880.0, 1174.66), 75, 0.7f)
        }
    }

    fun playPraisePop() {
        if (!isSoundEnabled) return
        scope.launch {
            // Cheerful bright chime (E5 -> A5 -> E6)
            playArpeggio(listOf(659.25, 880.0, 1318.51), 50, 0.7f)
        }
    }

    private fun playTone(freqHz: Double, durationMs: Int, volume: Float) {
        val numSamples = (durationMs * sampleRate) / 1000
        val buffer = ShortArray(numSamples)
        val vol = (volume * masterVolume).coerceIn(0f, 1f)

        val fadeSamples = (sampleRate * 0.004).toInt().coerceAtLeast(1) // 4ms fade in/out
        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val progress = i.toDouble() / numSamples
            val fade = when {
                i < fadeSamples -> 0.5 * (1.0 - cos(Math.PI * i / fadeSamples))
                i > numSamples - fadeSamples -> 0.5 * (1.0 - cos(Math.PI * (numSamples - i) / fadeSamples))
                else -> 1.0
            }
            val decay = (1.0 - progress)
            val sample = (sin(2.0 * Math.PI * freqHz * t) * fade * decay * 32767.0 * vol).toInt()
            buffer[i] = sample.coerceIn(-32768, 32767).toShort()
        }
        writeAudioBuffer(buffer)
    }

    private fun playPluckTone(freqHz: Double, durationMs: Int, volume: Float) {
        val numSamples = (durationMs * sampleRate) / 1000
        val buffer = ShortArray(numSamples)
        val vol = (volume * masterVolume).coerceIn(0f, 1f)

        val fadeSamples = (sampleRate * 0.003).toInt().coerceAtLeast(1)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val progress = i.toDouble() / numSamples
            val fade = when {
                i < fadeSamples -> 0.5 * (1.0 - cos(Math.PI * i / fadeSamples))
                i > numSamples - fadeSamples -> 0.5 * (1.0 - cos(Math.PI * (numSamples - i) / fadeSamples))
                else -> 1.0
            }
            // Sweet marimba chime (75% fundamental + 25% harmonic)
            val wave = 0.75 * sin(2.0 * Math.PI * freqHz * t) + 0.25 * sin(4.0 * Math.PI * freqHz * t)
            val decay = (1.0 - progress) * (1.0 - progress)
            val sample = (wave * fade * decay * 32767.0 * vol).toInt()
            buffer[i] = sample.coerceIn(-32768, 32767).toShort()
        }
        writeAudioBuffer(buffer)
    }

    private fun playSweepTone(startFreq: Double, endFreq: Double, durationMs: Int, volume: Float) {
        val numSamples = (durationMs * sampleRate) / 1000
        val buffer = ShortArray(numSamples)
        val vol = (volume * masterVolume).coerceIn(0f, 1f)

        val fadeSamples = (sampleRate * 0.004).toInt().coerceAtLeast(1)
        for (i in 0 until numSamples) {
            val progress = i.toDouble() / numSamples
            val currentFreq = startFreq + (endFreq - startFreq) * progress
            val t = i.toDouble() / sampleRate
            val fade = when {
                i < fadeSamples -> 0.5 * (1.0 - cos(Math.PI * i / fadeSamples))
                i > numSamples - fadeSamples -> 0.5 * (1.0 - cos(Math.PI * (numSamples - i) / fadeSamples))
                else -> 1.0
            }
            val decay = 1.0 - progress
            val sample = (sin(2.0 * Math.PI * currentFreq * t) * fade * decay * 32767.0 * vol).toInt()
            buffer[i] = sample.coerceIn(-32768, 32767).toShort()
        }
        writeAudioBuffer(buffer)
    }

    private fun playArpeggio(frequencies: List<Double>, noteDurationMs: Int, volume: Float) {
        val samplesPerNote = (noteDurationMs * sampleRate) / 1000
        val totalSamples = samplesPerNote * frequencies.size
        val buffer = ShortArray(totalSamples)
        val vol = (volume * masterVolume).coerceIn(0f, 1f)

        val fadeSamples = (sampleRate * 0.003).toInt().coerceAtLeast(1)
        var offset = 0
        for (freq in frequencies) {
            for (i in 0 until samplesPerNote) {
                val t = i.toDouble() / sampleRate
                val progress = i.toDouble() / samplesPerNote
                val fade = when {
                    i < fadeSamples -> 0.5 * (1.0 - cos(Math.PI * i / fadeSamples))
                    i > samplesPerNote - fadeSamples -> 0.5 * (1.0 - cos(Math.PI * (samplesPerNote - i) / fadeSamples))
                    else -> 1.0
                }
                val decay = 1.0 - progress
                val sample = (sin(2.0 * Math.PI * freq * t) * fade * decay * 32767.0 * vol).toInt()
                buffer[offset + i] = sample.coerceIn(-32768, 32767).toShort()
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
                kotlinx.coroutines.delay(buffer.size * 1000L / sampleRate + 30)
                try { audioTrack.release() } catch (_: Exception) {}
            }
        } catch (_: Exception) {
            // AudioTrack error ignored safely
        }
    }
}
