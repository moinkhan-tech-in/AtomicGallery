package com.challenge.gallery.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.End
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Start
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.feature.challenge.gallery.navigation.GalleryBaseRoute
import com.feature.challenge.gallery.navigation.gallerySection

@Composable
fun AtomicNavHost() {
    val navController = rememberNavController()
    NavHost(
        startDestination = GalleryBaseRoute,
        navController = navController,
        enterTransition = { slideIntoContainer(Start, tween(700)) },
        exitTransition = { slideOutOfContainer(Start, tween(700)) },
        popEnterTransition = { slideIntoContainer(End, tween(700)) },
        popExitTransition = { slideOutOfContainer(End, tween(700)) }
    ) {
        gallerySection(navController)
    }
}