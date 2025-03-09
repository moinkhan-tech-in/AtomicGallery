package com.feature.challenge.gallery.ui.folderlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.common.AppDispatcher
import com.challenge.common.Dispatcher
import com.challenge.domain.GetMediaHierarchyUseCase
import com.feature.challenge.gallery.ui.folderlist.MediaItemsUiState.Loading
import com.feature.challenge.gallery.ui.folderlist.MediaItemsUiState.PermissionDenied
import com.feature.challenge.gallery.ui.folderlist.MediaItemsUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaItemsViewModel @Inject constructor(
    private val getMediaHierarchyUseCase: GetMediaHierarchyUseCase,
    @Dispatcher(AppDispatcher.IO) private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _uiState: MutableStateFlow<MediaItemsUiState> = MutableStateFlow(Loading)
    val uiState = _uiState.asStateFlow()

    private fun fetchAllMedia(path: String? = null) {
        viewModelScope.launch(dispatcher) {

            val result = getMediaHierarchyUseCase.invoke()
            val mediaFolders = result.getOrNull().orEmpty()

            if (result.isSuccess) {
                when {
                    mediaFolders.isEmpty() -> {
                        // No Media File available
                        _uiState.update { MediaItemsUiState.NoMediaAvailable }
                    }
                    path == null -> {
                        // Root Folder, Listing of All albums
                        _uiState.update { Success(mediaItemList = mediaFolders) }
                    }
                    else -> {
                        // Particular album fetched
                        val files = mediaFolders
                            .find { it.path == path }
                            ?.mediaItems?.toList().orEmpty()
                        _uiState.update { Success(mediaItemList = files) }
                    }
                }
            }
        }
    }

    fun onAccepted(path: String? = null) {
        fetchAllMedia(path)
    }

    fun onDenied(deniedState: PermissionDenied) {
        _uiState.update { deniedState }
    }
}