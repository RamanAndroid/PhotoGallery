package com.example.photogallery.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.photogallery.navigation.PhotoGalleryNavigationDestination
import com.example.photogallery.screens.favorite.FavoriteImagesScreen

object FavoriteImagesDestination : PhotoGalleryNavigationDestination {
    override val route: String = "favorite_route"
}

object FavoriteImagesEntryDestination : PhotoGalleryNavigationDestination {
    override val route: String = "favorite_entry_route"
}

fun NavGraphBuilder.favoriteImagesGraph(navController: NavHostController) {
    navigation(
        route = "${FavoriteImagesDestination.route}",
        startDestination = "${FavoriteImagesEntryDestination.route}"
    ) {
        composable(route = "${FavoriteImagesEntryDestination.route}") {
            FavoriteImagesScreen()
        }
    }
}