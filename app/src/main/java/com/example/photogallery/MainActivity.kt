package com.example.photogallery

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.Lifecycle
import com.example.photogallery.screens.favorite.BoundedService
import com.example.photogallery.ui.PhotoGalleryApp
import com.example.photogallery.ui.theme.PhotoGalleryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var boundedService: BoundedService? = null
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

        if (!isBound) {
            val intent = Intent(this, BoundedService::class.java)
            bindService(intent, connectBoundService, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onPause() {
        super.onPause()

        if (isBound) {
            unbindService(connectBoundService)
        }
    }

    private val connectBoundService = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as BoundedService.ServiceBinder
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
            val intent = Intent(this, BoundedService::class.java)
            bindService(intent, connectBoundService, Context.BIND_AUTO_CREATE)
        }
    }
}