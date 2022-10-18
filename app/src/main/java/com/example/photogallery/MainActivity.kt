package com.example.photogallery

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.Lifecycle
import com.example.photogallery.screens.favorite.PlayerService
import com.example.photogallery.ui.PhotoGalleryApp
import com.example.photogallery.ui.theme.PhotoGalleryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var playerService: PlayerService
    private var isBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PhotoGalleryTheme {
                PhotoGalleryApp()
            }
        }
        Intent(this, PlayerService::class.java).apply {
            startService(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        Intent(this, PlayerService::class.java).apply {
            stopService(this)
        }
    }

    private val connectBoundService = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as PlayerService.ServiceBinder
            playerService = binder.getService()
            isBound = true
            playerService.connectedClient()

            playerService.getCurrentPositionSong()?.let {
                Toast.makeText(
                    this@MainActivity,
                    "Проигранно музыки: ${playerService.timePlayMusic(it)}",
                    Toast.LENGTH_LONG
                ).show()
            }
            Log.d("activityLifecycle", "player = client number ${playerService.numberClients()}")
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
            reconnectToBoundService()
        }
    }

    private fun reconnectToBoundService() {
        if (lifecycle.currentState >= Lifecycle.State.RESUMED) {
            bindService()
        }
    }

    fun bindService() {
        val intent = Intent(this, PlayerService::class.java)
        bindService(intent, connectBoundService, Context.BIND_AUTO_CREATE)
    }

    fun unbindService() {
        if (isBound) {
            unbindService(connectBoundService)
            playerService.disconnectedClient()
        }
    }
}