package com.feature.challenge.gallery.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.challenge.common.model.MediaItemType.Folder
import com.challenge.common.model.MediaItemType.Image
import com.challenge.common.model.MediaItemType.Video
import com.feature.challenge.gallery.ui.folderlist.MediaItemsScreen
import com.feature.challenge.gallery.ui.image.MediaImageItemScreen
import kotlinx.serialization.Serializable

@Serializable
data object GalleryBaseRoute

@Serializable
data class GalleryRoute(
    val folderPath: String? = null,
    val folderName: String? = null
)

@Serializable
data class GalleryImageRoute(
    val filePath: String? = null,
    val fileName: String? = null
)

fun NavGraphBuilder.gallerySection(navController: NavHostController) {

    navigation<GalleryBaseRoute>(startDestination = GalleryRoute()) {

        composable<GalleryRoute> { route ->
            val args = route.toRoute<GalleryRoute>()
            MediaItemsScreen(
                args = args,
                onBackClick = { navController.popBackStack() },
                onFolderClick = {
                    when (it.type) {
                        Folder -> navController.navigate(GalleryRoute(it.path, it.name))
                        Image -> navController.navigate(GalleryImageRoute(it.path, it.name))
                        Video -> navController.navigate(GalleryImageRoute(it.path, it.name))
                    }
                },
            )
        }

        composable<GalleryImageRoute> { route ->
            val args = route.toRoute<GalleryImageRoute>()
            MediaImageItemScreen(
                args = args,
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}