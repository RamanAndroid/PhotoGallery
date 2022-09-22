package com.example.photogallery.screens.favorite

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import com.example.photogallery.R

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
        player?.apply {
            stop()
            release()
        }
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()

        player?.stop()
        player?.reset()
        player?.release()
        player = null
    }

    fun startMusic() {
        if(player == null){
            player = MediaPlayer.create(this,song)
        }
        player?.start()
    }

    fun stopMusic() {
        player?.apply {
            release()
            stop()
        }
    }

    fun pauseMusic() {
        player?.pause()
    }

    fun getCurrentPositionSong() = player?.currentPosition ?: 0
}