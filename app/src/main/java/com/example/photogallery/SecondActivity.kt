package com.example.photogallery

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.photogallery.ui.theme.PhotoGalleryTheme

class SecondActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(LocalActivity provides this) {
                PhotoGalleryTheme {
                    val activity = LocalActivity.current
                    Box(modifier = Modifier.fillMaxSize()) {
                        Button(
                            modifier = Modifier.align(Alignment.Center),
                            onClick = {
                                activity.startActivity(
                                    Intent(
                                        activity, PlayerActivity::class.java
                                    )
                                )
                            }
                        ) {
                            Text(
                                "Player",
                                color = Color.White
                            )
                        }
                    }

                }
            }
        }
    }


}

val LocalActivity = staticCompositionLocalOf<androidx.core.app.ComponentActivity> {
    noLocalProvidedFor("LocalActivity")
}

private fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}