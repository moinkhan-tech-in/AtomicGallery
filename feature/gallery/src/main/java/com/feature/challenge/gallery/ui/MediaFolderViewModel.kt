package com.feature.challenge.gallery.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.domain.GetMediaHierarchyUseCase
import com.feature.challenge.gallery.ui.MediaFolderUiState.Loading
import com.feature.challenge.gallery.ui.MediaFolderUiState.PermissionDenied
import com.feature.challenge.gallery.ui.MediaFolderUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaFolderViewModel @Inject constructor(
    private val getMediaHierarchyUseCase: GetMediaHierarchyUseCase
): ViewModel() {

    private val _uiState: MutableStateFlow<MediaFolderUiState> = MutableStateFlow(Loading)
    val uiState = _uiState.asStateFlow()

    private fun fetchAllMedia() {
        viewModelScope.launch {
            val result = getMediaHierarchyUseCase.invoke()
            if (result.isSuccess && result.getOrNull() != null) {
                _uiState.update { Success(result.getOrThrow()) }
            }
        }
    }

    fun onAccepted() {
        fetchAllMedia()
    }

    fun onDenied(deniedState: PermissionDenied) {
        _uiState.update { deniedState }
    }
}