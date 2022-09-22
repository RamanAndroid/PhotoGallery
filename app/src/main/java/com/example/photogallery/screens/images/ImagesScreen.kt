package com.example.photogallery.screens.images

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.photogallery.MainActivity
import com.example.photogallery.PlayerActivity

@Composable
fun ImagesScreen(viewModel: ImagesViewModel = hiltViewModel()) {
    viewModel.getImages()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Images", color = Color.White)
        Button(
            onClick = {
                context.startActivity(
                    Intent(
                        context, PlayerActivity::class.java
                    )
                )
            }
        ) {
            Text("Player", color = Color.White)
        }
    }
}