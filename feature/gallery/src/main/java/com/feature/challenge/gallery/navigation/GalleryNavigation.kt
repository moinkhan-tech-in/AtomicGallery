package com.feature.challenge.gallery.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.feature.challenge.gallery.ui.GalleyMediaFolderScreen
import kotlinx.serialization.Serializable

@Serializable
data object GalleryBaseRoute

@Serializable
data object GalleryRoute

fun NavGraphBuilder.gallerySection() {

    navigation<GalleryBaseRoute>(startDestination = GalleryRoute) {

        composable<GalleryRoute>() {
            GalleyMediaFolderScreen()
        }
    }
}