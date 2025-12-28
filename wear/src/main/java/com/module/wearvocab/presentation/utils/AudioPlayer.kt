package com.module.wearvocab.presentation.utils

import android.media.AudioAttributes
import android.media.MediaPlayer

fun playAudio(url: String) {
    if (url.isBlank()) return

    val mediaPlayer = MediaPlayer()
    try {
        mediaPlayer.apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener { start() }
            setOnCompletionListener {
                stop()
                release()
            }
            setOnErrorListener { _, _, _ ->
                release()
                true
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
