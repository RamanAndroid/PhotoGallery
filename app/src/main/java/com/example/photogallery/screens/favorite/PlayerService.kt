package com.example.photogallery.screens.favorite

import android.app.Service
import android.content.Intent
import android.content.ServiceConnection
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
            Log.d("playerService", "service binder object = ${this@PlayerService}")
            return this@PlayerService
        }
    }

    override fun unbindService(conn: ServiceConnection) {
        Log.d("playerService", "un binder object = $this")
        super.unbindService(conn)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("playerService", "on start command object = $this")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d("playerService", "on bind object = $this")
        return serviceBinder
    }

    override fun onDestroy() {
        Log.d("playerService", "on destroy object = $this")
        player?.apply {
            stop()
            release()
        }
        player = null

        super.onDestroy()
    }

    fun startMusic() {
        Log.d("playerService", "start object = $this")
        if (player == null) {
            player = MediaPlayer.create(this, song).apply {
                isLooping = true
            }
        }
        player?.start()
    }

    fun stopMusic() {
        Log.d("playerService", "stop object = $this")
        player?.apply {
            stop()
            release()
        }
        player = null
    }

    fun pauseMusic() {
        Log.d("playerService", "pause object = $this")
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