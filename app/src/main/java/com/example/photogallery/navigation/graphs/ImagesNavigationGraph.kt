package com.example.photogallery.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.photogallery.navigation.PhotoGalleryNavigationDestination
import com.example.photogallery.screens.images.ImagesScreen

object ImagesDestination : PhotoGalleryNavigationDestination {
    override val route: String = "images_route"
}

object ImagesEntryDestination : PhotoGalleryNavigationDestination {
    override val route: String = "images_entry_route"
}

fun NavGraphBuilder.imagesGraph(navController: NavHostController) {
    navigation(
        route = "${ImagesDestination.route}",
        startDestination = "${ImagesEntryDestination.route}"
    ) {
        composable(route = "${ImagesEntryDestination.route}") {
            ImagesScreen()
        }
    }
}