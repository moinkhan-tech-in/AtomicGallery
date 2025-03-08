package com.challenge.avriocgallery.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.feature.challenge.gallery.navigation.GalleryBaseRoute
import com.feature.challenge.gallery.navigation.gallerySection

@Composable
fun AvriocNavHost() {
    NavHost(
        startDestination = GalleryBaseRoute,
        navController = rememberNavController()
    ) {
        gallerySection()
    }
}