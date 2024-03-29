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
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.Lifecycle
import com.example.photogallery.screens.favorite.PlayerService
import com.example.photogallery.ui.theme.PhotoGalleryTheme

class PlayerActivity : ComponentActivity() {
    lateinit var playerService: PlayerService
    private var isBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhotoGalleryTheme {
                Player()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        bindService()
    }

    override fun onDestroy() {
        if (isBound) {
            unbindService(connectBoundService)
        }

        super.onDestroy()
    }

    private val connectBoundService = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as PlayerService.ServiceBinder
            playerService = binder.getService()

            Log.d("playerService","on service connected object = ${playerService}")

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
    }
}

@Composable
fun Player() {
    val activity = LocalContext.current as PlayerActivity

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(R.drawable.ic_queue_music_24),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )

        Button(
            onClick = {
                activity.startActivity(
                    Intent(
                        activity, MainActivity::class.java
                    )
                )
            }
        ) {
            Text("Main Activity", color = Color.White)
        }

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        activity.startService(Intent(activity, PlayerService::class.java))
                        activity.playerService.startMusic()
                    },
                painter = painterResource(R.drawable.ic_play_circle_24),
                contentDescription = null,
            )
            Image(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        activity.playerService.pauseMusic()
                    },
                painter = painterResource(R.drawable.ic_pause_circle_24),
                contentDescription = null,
            )
            Image(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        activity.stopService(Intent(activity, PlayerService::class.java))
                        activity.playerService.stopMusic()
                    },
                painter = painterResource(R.drawable.ic_stop_circle_24),
                contentDescription = null,
            )
        }
    }
}