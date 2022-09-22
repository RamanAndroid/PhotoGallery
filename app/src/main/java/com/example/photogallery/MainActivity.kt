package com.example.photogallery

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.Lifecycle
import com.example.photogallery.screens.favorite.PlayerService
import com.example.photogallery.ui.PhotoGalleryApp
import com.example.photogallery.ui.theme.PhotoGalleryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var boundedService: PlayerService
    private var isBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhotoGalleryTheme {
                PhotoGalleryApp()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        bindService()
        if (isBound) {
            Log.d("timeMusic", "${boundedService.getCurrentPositionSong()}")
        }
    }

    override fun onPause() {
        super.onPause()

        if (isBound) {
            unbindService(connectBoundService)
            isBound = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isBound) {
            unbindService(connectBoundService)
            stopService(Intent(this, PlayerService::class.java))
        }

    }

    private val connectBoundService = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as PlayerService.ServiceBinder
            boundedService = binder.getService()
            isBound = true
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

    private fun bindService() {
        val intent = Intent(this, PlayerService::class.java)
        bindService(intent, connectBoundService, Context.BIND_AUTO_CREATE)
        startService(intent)
    }
}