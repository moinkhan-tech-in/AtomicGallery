package com.feature.challenge.gallery.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.feature.challenge.gallery.ui.MediaItemsScreen
import kotlinx.serialization.Serializable

@Serializable
data object GalleryBaseRoute

@Serializable
data class GalleryRoute(
    val folderPath: String? = null,
    val folderName: String? = null
)

fun NavGraphBuilder.gallerySection(navController: NavHostController) {

    navigation<GalleryBaseRoute>(startDestination = GalleryRoute()) {

        composable<GalleryRoute> { route ->
            val args = route.toRoute<GalleryRoute>()
            MediaItemsScreen(
                args = args,
                onBackClick = { navController.popBackStack() },
                onFolderClick = { navController.navigate(GalleryRoute(it.path, it.name)) }
            )
        }
    }
}