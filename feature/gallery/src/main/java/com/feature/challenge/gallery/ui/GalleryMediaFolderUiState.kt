package com.feature.challenge.gallery.ui

import com.challenge.common.model.MediaFolder

sealed class GalleryMediaFolderUiState {
    data object Loading: GalleryMediaFolderUiState()

    data class Success(
        val folderList: List<MediaFolder>
    ): GalleryMediaFolderUiState()
}