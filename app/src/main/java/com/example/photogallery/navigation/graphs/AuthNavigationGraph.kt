package com.example.photogallery.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.photogallery.navigation.PhotoGalleryNavigationDestination
import com.example.photogallery.screens.LoginScreen

object AuthenticationDestination : PhotoGalleryNavigationDestination {
    override val route: String = "authentication_route"
}

object LoginDestination : PhotoGalleryNavigationDestination {
    override val route: String = "login_route"
}

object SignUpDestination : PhotoGalleryNavigationDestination {
    override val route: String = "sign_up_route"
}

object ForgotDestination : PhotoGalleryNavigationDestination {
    override val route: String = "forgot_route"
}

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = "${AuthenticationDestination.route}",
        startDestination = "${LoginDestination.route}"
    ) {
        composable(
            route = "${LoginDestination.route}",
        ) {
            LoginScreen(openImagesScreen = { navController.navigate("${ImagesDestination.route}") })
        }
    }
}