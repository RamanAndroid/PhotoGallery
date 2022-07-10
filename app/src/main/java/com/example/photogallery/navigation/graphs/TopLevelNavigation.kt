/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.photogallery.navigation.graphs

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.photogallery.R

class TopLevelNavigation(private val navController: NavHostController) {

    fun navigateTo(destination: TopLevelDestination) {
        navController.navigate(destination.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

data class TopLevelDestination(
    val route: String,
    @DrawableRes
    val iconId: Int,
    @StringRes
    val iconTextId: Int
)

val TOP_LEVEL_DESTINATIONS = listOf(
    TopLevelDestination(
        route = ImagesDestination.route,
        iconId = R.drawable.ic_photo_24,
        iconTextId = R.string.images
    ),
    TopLevelDestination(
        route = FavoriteImagesDestination.route,
        iconId = R.drawable.ic_person_24,
        iconTextId = R.string.favorite_images
    ),
    TopLevelDestination(
        route = SettingsDestination.route,
        iconId = R.drawable.ic_settings_24,
        iconTextId = R.string.settings
    )
)
