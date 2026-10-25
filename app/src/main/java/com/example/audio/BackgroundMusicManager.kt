package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

/**
 * Cheerful, upbeat, and bright casual puzzle game music synthesizer for Word Quest.
 * Generates lively marimba/toy-piano style melody loops in C Major without scary drones.
 */
class BackgroundMusicManager {

    private val sampleRate = 22050
    private val scope = CoroutineScope(Dispatchers.Default)
    private var musicJob: Job? = null

    @Volatile
    var isMusicEnabled: Boolean = true
        set(value) {
            val changed = field != value
            field = value
            if (value) {
                startMusic()
            } else {
                stopMusic()
            }
        }

    fun startMusic() {
        if (!isMusicEnabled || musicJob?.isActive == true) return

        musicJob = scope.launch {
            // Note frequencies in C Major (cheerful, happy tones)
            val C4 = 261.63
            val D4 = 293.66
            val E4 = 329.63
            val F4 = 349.23
            val G4 = 392.00
            val A4 = 440.00
            val B4 = 493.88
            val C5 = 523.25
            val D5 = 587.33
            val E5 = 659.25
            val F5 = 698.46
            val G5 = 783.99
            val A5 = 880.00
            val B5 = 987.77
            val C6 = 1046.50

            // Upbeat, cute puzzle game melody (Melody note, Bass note)
            val sequence = listOf(
                Pair(E5, C4), Pair(G5, C4), Pair(C6, E4), Pair(E5, E4),
                Pair(D5, G4), Pair(G5, G4), Pair(B5, G4), Pair(D5, G4),

                Pair(C5, C4), Pair(E5, C4), Pair(G5, E4), Pair(C6, E4),
                Pair(A5, F4), Pair(F5, F4), Pair(D5, F4), Pair(B4, G4),

                Pair(E5, C4), Pair(C5, C4), Pair(G5, E4), Pair(E5, E4),
                Pair(F5, F4), Pair(A5, F4), Pair(D5, F4), Pair(F5, F4),

                Pair(G5, G4), Pair(E5, G4), Pair(D5, G4), Pair(B4, G4),
                Pair(C5, C4), Pair(G4, C4), Pair(C5, C4), Pair(REST, REST)
            )

            var step = 0
            while (isActive && isMusicEnabled) {
                val (melodyFreq, bassFreq) = sequence[step % sequence.size]
                if (melodyFreq != REST) {
                    playPluckTone(melodyFreq, bassFreq, noteDurationMs = 180)
                }
                step++
                delay(190) // Fast 120+ BPM upbeat tempo
            }
        }
    }

    fun stopMusic() {
        musicJob?.cancel()
        musicJob = null
    }

    private fun playPluckTone(melodyFreq: Double, bassFreq: Double, noteDurationMs: Int) {
        if (!isMusicEnabled) return
        val numSamples = (noteDurationMs * sampleRate) / 1000
        val buffer = ShortArray(numSamples)
        val vol = 0.22f // Bright, friendly volume

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val progress = i.toDouble() / numSamples

            // Crisp staccato pluck envelope (fast attack, exponential decay)
            val envelope = (1.0 - progress) * (1.0 - progress)

            // Melody tone (marimba harmonics: 70% fundamental + 30% second harmonic)
            val melodyWave = 0.75 * sin(2.0 * Math.PI * melodyFreq * t) +
                             0.25 * sin(4.0 * Math.PI * melodyFreq * t)

            // Soft bass tone companion
            val bassWave = if (bassFreq > 0) 0.35 * sin(2.0 * Math.PI * bassFreq * t) else 0.0

            val mixed = (melodyWave * 0.7 + bassWave * 0.3) * envelope * 32767.0 * vol
            buffer[i] = mixed.toInt().coerceIn(-32768, 32767).toShort()
        }

        try {
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
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
                delay(noteDurationMs.toLong() + 30)
                try { audioTrack.release() } catch (_: Exception) {}
            }
        } catch (_: Exception) {
            // AudioTrack error handled safely
        }
    }

    companion object {
        private const val REST = 0.0
    }
}
