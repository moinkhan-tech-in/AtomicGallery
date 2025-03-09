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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

interface MediaItemsViewModelContract {
    val uiState: StateFlow<MediaItemsUiState>
    fun fetchAllMedia(path: String? = null)
    fun onDenied(deniedState: PermissionDenied)
}

@HiltViewModel
class MediaItemsViewModel @Inject constructor(
    private val getMediaHierarchyUseCase: GetMediaHierarchyUseCase,
    @Dispatcher(AppDispatcher.IO) private val dispatcher: CoroutineDispatcher
): ViewModel(), MediaItemsViewModelContract {

    private val _uiState: MutableStateFlow<MediaItemsUiState> = MutableStateFlow(Loading)
    override val uiState = _uiState.asStateFlow()

    override fun fetchAllMedia(path: String?) {
        viewModelScope.launch(dispatcher) {

            _uiState.update { Loading }

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

    override fun onDenied(deniedState: PermissionDenied) {
        _uiState.update { deniedState }
    }
}