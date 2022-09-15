package com.example.photogallery.screens.favorite

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.provider.Settings

class BoundedService : Service() {

    private val serviceBinder = ServiceBinder()
    private lateinit var player: MediaPlayer

    inner class ServiceBinder : Binder() {
        fun getService(): BoundedService {
            return this@BoundedService
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return serviceBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }

    override fun onCreate() {
        super.onCreate()

        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)
        player.isLooping = true
        player.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        player.stop()
    }
}