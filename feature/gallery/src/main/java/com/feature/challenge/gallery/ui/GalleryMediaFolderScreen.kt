package com.feature.challenge.gallery.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.challenge.common.model.MediaFolder
import com.feature.challenge.gallery.ui.GalleryMediaFolderUiState.Loading
import com.feature.challenge.gallery.ui.GalleryMediaFolderUiState.Success

@Composable
fun GalleyMediaFolderScreen(
    viewModel: GalleyMediaFolderViewModel = hiltViewModel<GalleyMediaFolderViewModel>()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    GalleyMediaFolderScreenContent(uiState = uiState)
}


@Composable
private fun GalleyMediaFolderScreenContent(
    uiState: GalleryMediaFolderUiState
) {
    Scaffold { padding ->
        Crossfade(uiState) { state ->
            when (state) {
                is Success -> GalleyMediaFolderSuccessContent(state.folderList)
                Loading -> GalleyMediaFolderLoadingScreenContent()
            }
        }
    }
}

@Composable
private fun GalleyMediaFolderSuccessContent(folder: List<MediaFolder>) {
    LazyColumn {
        items(folder) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "${it.name} ${it.path}",
                color = Color.White
            )
        }
    }
}

@Composable
private fun GalleyMediaFolderLoadingScreenContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LinearProgressIndicator()
    }
}