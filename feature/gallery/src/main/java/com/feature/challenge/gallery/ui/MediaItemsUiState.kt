package com.feature.challenge.gallery.ui

import com.challenge.common.model.MediaItem

sealed class MediaItemsUiState {

    data object Loading: MediaItemsUiState()

    data class Success(
        val mediaItemList: List<MediaItem> = emptyList()
    ): MediaItemsUiState()

    data class PermissionDenied(
        val rationalRequired: Boolean
    ): MediaItemsUiState()
}