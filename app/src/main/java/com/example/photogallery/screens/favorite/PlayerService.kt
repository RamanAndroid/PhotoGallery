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
    private var binderClients = 0

    inner class ServiceBinder : Binder() {
        fun getService(): PlayerService {
            return this@PlayerService
        }
    }

    fun connectedClient() {
        binderClients++
    }

    fun disconnectedClient() {
        binderClients--
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("activityLifecycle", "service = onUnbind")
        return super.onUnbind(intent)
    }

    fun numberClients(): Int = binderClients

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
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
        Log.d("activityLifecycle", "service = onDestroy")
        super.onDestroy()
    }

    fun startMusic() {
        if (player == null) {
            player = MediaPlayer.create(this, song).apply {
                isLooping = true
            }
        }
        player?.start()
    }


    fun pauseMusic() {
        player?.pause()
    }

    fun stopMusic() {
        player?.apply {
            stop()
            release()
        }
        player = null

        this.stopSelf()
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