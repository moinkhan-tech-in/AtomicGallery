package com.challenge.avriocgallery.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.feature.challenge.gallery.navigation.GalleryBaseRoute
import com.feature.challenge.gallery.navigation.gallerySection

@Composable
fun AvriocNavHost() {
    val navController = rememberNavController()
    NavHost(
        startDestination = GalleryBaseRoute,
        navController = navController
    ) {
        gallerySection(navController)
    }
}