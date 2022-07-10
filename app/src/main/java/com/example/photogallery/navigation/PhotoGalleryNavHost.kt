package com.example.photogallery.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.photogallery.navigation.graphs.*

@Composable
fun PhotoGalleryNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "${ImagesDestination.route}",
        modifier = modifier
    ) {

        //auth navigation
        authNavGraph(navController)

        //top bottom navigation
        imagesGraph(navController)
        favoriteImagesGraph(navController)
        settingsGraph(navController)
    }
}