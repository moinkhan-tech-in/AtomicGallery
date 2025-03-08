package com.feature.challenge.gallery.ui

import com.challenge.common.model.MediaFolder

sealed class MediaFolderUiState {

    data object Loading: MediaFolderUiState()

    data class Success(
        val folderList: List<MediaFolder>
    ): MediaFolderUiState()

    data class PermissionDenied(
        val rationalRequired: Boolean
    ): MediaFolderUiState()
}