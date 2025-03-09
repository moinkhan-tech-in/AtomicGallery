package com.feature.challenge.gallery.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.domain.GetMediaHierarchyUseCase
import com.feature.challenge.gallery.ui.MediaItemsUiState.Loading
import com.feature.challenge.gallery.ui.MediaItemsUiState.PermissionDenied
import com.feature.challenge.gallery.ui.MediaItemsUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaItemsViewModel @Inject constructor(
    private val getMediaHierarchyUseCase: GetMediaHierarchyUseCase
): ViewModel() {

    private val _uiState: MutableStateFlow<MediaItemsUiState> = MutableStateFlow(Loading)
    val uiState = _uiState.asStateFlow()

    private fun fetchAllMedia(path: String? = null) {
        viewModelScope.launch {

            val result = getMediaHierarchyUseCase.invoke()

            if (result.isSuccess && result.getOrNull() != null) {

                if (path == null) {
                    val folders = result.getOrDefault(emptyList())
                    _uiState.update { Success(mediaItemList = folders) }
                } else {
                    val files = result.getOrNull()
                        ?.find { it.path == path }
                        ?.mediaItems?.toList().orEmpty()
                    _uiState.update { Success(mediaItemList = files) }
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