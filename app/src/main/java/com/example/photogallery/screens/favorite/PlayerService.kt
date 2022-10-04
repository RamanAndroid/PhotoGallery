package com.example.photogallery.screens.favorite

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.example.photogallery.R
import java.util.concurrent.TimeUnit

class PlayerService : Service() {

    private val serviceBinder = ServiceBinder()
    private var player: MediaPlayer? = null
    private val song = R.raw.slim_shady_instrumental

    companion object {
        const val PLAYER_STATE = "PLAYER_STATE"
    }

    inner class ServiceBinder : Binder() {
        fun getService(): PlayerService {
            return this@PlayerService
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        intent.extras?.let {
            val playerState = it.getString(PLAYER_STATE, PlayerState.NONE.name)

            when (PlayerState.valueOf(playerState)) {
                PlayerState.START -> {
                    startMusic()
                }
                PlayerState.PAUSE -> {
                    pauseMusic()
                }
                PlayerState.STOP -> {
                    stopMusic()
                }
                PlayerState.NONE -> {}
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        return serviceBinder
    }

    override fun onDestroy() {
        player?.apply {
            stop()
            release()
        }
        player = null

        super.onDestroy()
    }

    private fun startMusic() {
        if (player == null) {
            player = MediaPlayer.create(this, song).apply {
                isLooping = true
            }
        }
        player?.start()
    }

    private fun stopMusic() {
        player?.apply {
            stop()
            release()
        }
        player = null
    }

    private fun pauseMusic() {
        player?.pause()
    }

    fun getCurrentPositionSong() = player?.currentPosition?.toLong()

    fun timePlayMusic(durationMusic: Long): String {
        val minutes: Long =
            TimeUnit.MINUTES.convert(durationMusic, TimeUnit.MILLISECONDS)
        val seconds: Long =
            (TimeUnit.SECONDS.convert(durationMusic, TimeUnit.MILLISECONDS)
                    - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
        return String.format("%02d:%02d", minutes, seconds)
    }
}

enum class PlayerState {
    START,
    PAUSE,
    STOP,
    NONE
}