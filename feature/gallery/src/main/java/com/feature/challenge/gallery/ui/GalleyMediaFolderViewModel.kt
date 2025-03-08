package com.feature.challenge.gallery.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.domain.GetMediaHierarchyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleyMediaFolderViewModel @Inject constructor(
    private val getMediaHierarchyUseCase: GetMediaHierarchyUseCase
): ViewModel() {

    private val _uiState: MutableStateFlow<GalleryMediaFolderUiState> = MutableStateFlow(GalleryMediaFolderUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchAllMedia()
    }

    private fun fetchAllMedia() {
        viewModelScope.launch {
            val result = getMediaHierarchyUseCase.invoke()
            if (result.isSuccess && result.getOrNull() != null) {
                _uiState.update { GalleryMediaFolderUiState.Success(result.getOrThrow()) }
            }
        }
    }
}