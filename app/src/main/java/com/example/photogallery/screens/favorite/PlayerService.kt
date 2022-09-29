package com.example.photogallery.screens.favorite

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.photogallery.R
import java.util.concurrent.TimeUnit

class PlayerService : Service() {

    private val serviceBinder = ServiceBinder()
    private var player: MediaPlayer? = null
    private val song = R.raw.slim_shady_instrumental

    inner class ServiceBinder : Binder() {
        fun getService(): PlayerService {
            return this@PlayerService
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return serviceBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("lifecycle", "Service onUnbind")
        player?.apply {
            pause()
        }

        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        Log.d("lifecycle", "Service onDestroy")
        player?.stop()
        player?.reset()
        player?.release()
        player = null

        super.onDestroy()
    }

    fun startMusic() {
        if (player == null) {
            player = MediaPlayer.create(this, song)
        }
        player?.start()
    }

    fun stopMusic() {
        player?.apply {
            stop()
        }
    }

    fun pauseMusic() {
        player?.pause()
    }

    private fun getCurrentPositionSong() = player?.currentPosition?.toLong() ?: 0L

    fun timePlayMusic(): String {
        val minutes: Long =
            TimeUnit.MINUTES.convert(getCurrentPositionSong(), TimeUnit.MILLISECONDS)
        val seconds: Long =
            (TimeUnit.SECONDS.convert(getCurrentPositionSong(), TimeUnit.MILLISECONDS)
                    - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
        return String.format("%02d:%02d", minutes, seconds)
    }
}