package com.feature.challenge.gallery.ui.image

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.feature.challenge.gallery.navigation.GalleryImageRoute
import com.feature.challenge.gallery.utils.AppScreenContent

@Composable
fun MediaImageItemScreen(
    args: GalleryImageRoute,
    onBackClick: () -> Unit,
) {
    MediaImageItemScreenContent(
        title = args.fileName,
        filePath = args.filePath,
        onBackClick = onBackClick
    )
}

@Composable
fun MediaImageItemScreenContent(
    title: String?,
    filePath: String?,
    onBackClick: () -> Unit
) {
    AppScreenContent(
        navigationTitle = title.orEmpty(),
        onBackClick = onBackClick,
        contentAlignment = Alignment.Center
    ) {
        val context = LocalContext.current
        val request = ImageRequest.Builder(context)
            .data(filePath)
            .build()

        AsyncImage(
            model = request,
            contentDescription = title,
            modifier = Modifier.fillMaxWidth()
        )
    }
}