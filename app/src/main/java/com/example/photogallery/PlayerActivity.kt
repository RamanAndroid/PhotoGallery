package com.example.photogallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
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
import com.example.photogallery.screens.favorite.PlayerService
import com.example.photogallery.screens.favorite.PlayerState
import com.example.photogallery.ui.theme.PhotoGalleryTheme

class PlayerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhotoGalleryTheme {
                Player()
            }
        }
        Intent(this, PlayerService::class.java).apply {
            this@PlayerActivity.startService(this)
        }
        Log.d("activity ", "player = onCreate")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("activity ", "player = onNewIntent")
    }

    override fun onStart() {
        super.onStart()
        Log.d("activity ", "player = onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("activity ", "player = onResume")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("activity ", "player = onDestroy")
    }
}

@Composable
fun Player() {
    val context = LocalContext.current

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
                context.startActivity(Intent(context, MainActivity::class.java))
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
                        context.sendCommandToPlayerService(PlayerState.START)
                    },
                painter = painterResource(R.drawable.ic_play_circle_24),
                contentDescription = null,
            )
            Image(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        context.sendCommandToPlayerService(PlayerState.PAUSE)
                    },
                painter = painterResource(R.drawable.ic_pause_circle_24),
                contentDescription = null,
            )
            Image(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        context.sendCommandToPlayerService(PlayerState.STOP)
                    },
                painter = painterResource(R.drawable.ic_stop_circle_24),
                contentDescription = null,
            )
        }
    }
}

private fun Context.sendCommandToPlayerService(command: PlayerState) {
    val intent = Intent()
    intent.action = PlayerService.PLAYER_BROADCAST_RECEIVER
    intent.putExtra(PlayerService.PLAYER_STATE, command.name)
    this.sendBroadcast(intent)
}