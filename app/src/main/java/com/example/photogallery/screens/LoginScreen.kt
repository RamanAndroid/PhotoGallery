package com.example.photogallery.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LoginScreen(openImagesScreen: () -> Unit) {


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
            .clickable { openImagesScreen() },
        contentAlignment = Alignment.Center
    ) {
        Text("Login", color = Color.White)
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen({})
}