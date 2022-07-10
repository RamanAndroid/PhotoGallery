package com.example.photogallery.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.photogallery.navigation.PhotoGalleryNavigationDestination
import com.example.photogallery.screens.SettingsScreen

object SettingsDestination : PhotoGalleryNavigationDestination {
    override val route: String = "settings_route"
}

object SettingsEntryDestination : PhotoGalleryNavigationDestination {
    override val route: String = "settings_entry_route"
}

fun NavGraphBuilder.settingsGraph(navController: NavHostController){
    navigation(
        route = "${SettingsDestination.route}",
        startDestination = "${SettingsEntryDestination.route}"
    ) {
        composable(route = "${SettingsEntryDestination.route}") {
            SettingsScreen()
        }
    }
}

