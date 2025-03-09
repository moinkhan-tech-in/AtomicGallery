package com.feature.challenge.gallery.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.challenge.common.model.MediaItem
import com.feature.challenge.gallery.LocalViewType
import com.feature.challenge.gallery.R
import com.feature.challenge.gallery.ViewType
import com.feature.challenge.gallery.components.MediaItemVerticalGridUi
import com.feature.challenge.gallery.components.MediaItemVerticalListUi
import com.feature.challenge.gallery.navigation.GalleryRoute
import com.feature.challenge.gallery.ui.MediaItemsUiState.PermissionDenied
import com.feature.challenge.gallery.ui.MediaItemsUiState.Success
import com.feature.challenge.gallery.utils.AppScreenContent
import com.feature.challenge.gallery.utils.openAppSettings
import com.feature.challenge.gallery.utils.permissionDeniedMsg

@Composable
fun MediaItemsScreen(
    viewModel: MediaItemsViewModel = hiltViewModel<MediaItemsViewModel>(),
    args: GalleryRoute,
    onFolderClick: (MediaItem) -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Permission handling code.
    val askPermission = remember { mutableStateOf(true) }
    HandleMediaPermission(
        askPermission = askPermission.value,
        onAccepted = { viewModel.onAccepted(args.folderPath) },
        onDenied = viewModel::onDenied,
        onDismiss = { askPermission.value = false }
    )

    // Main body content of the screenÏÏ
    val context = LocalContext.current
    MediaItemsScreenContent(
        uiState = uiState,
        args = args,
        onAskPermission = { askPermission.value = true },
        navigateToAppSetting = { openAppSettings(context) },
        onFolderClick = onFolderClick,
        onBackClick = onBackClick
    )
}

@Composable
private fun MediaItemsScreenContent(
    uiState: MediaItemsUiState,
    args: GalleryRoute,
    onAskPermission: () -> Unit,
    onBackClick: () -> Unit,
    navigateToAppSetting: () -> Unit,
    onFolderClick: (MediaItem) -> Unit
) {
    AppScreenContent(
        navigationTitle = args.folderName ?: stringResource(R.string.title_atomic_gallery),
        isLoading = uiState is MediaItemsUiState.Loading,
        isHomeScreen = args.folderName == null,
        onBackClick = onBackClick
    ) {
        Crossfade(uiState) { state ->
            when (state) {

                is Success -> MediaItemsContent(
                    folders = state.mediaItemList,
                    onFolderClick = onFolderClick
                )

                is PermissionDenied -> PermissionDeniedContent(
                    uiState = state,
                    askPermission = onAskPermission,
                    navigateToAppSetting = navigateToAppSetting
                )

                else -> {}
            }
        }
    }
}

@Composable
private fun MediaItemsContent(
    folders: List<MediaItem>,
    onFolderClick: (MediaItem) -> Unit
) {
    val type = LocalViewType.current.value
    Crossfade(targetState = type) {
        if (it == ViewType.Grid) {
            MediaItemVerticalGridUi(
                mediaItems = folders,
                onFolderClick = onFolderClick
            )
        } else {
            MediaItemVerticalListUi(
                mediaItems = folders,
                onFolderClick = onFolderClick
            )
        }
    }
}

@Composable
private fun PermissionDeniedContent(
    uiState: PermissionDenied,
    navigateToAppSetting: () -> Unit,
    askPermission: () -> Unit
) {
    val onClick = when (uiState.rationalRequired) {
        true -> navigateToAppSetting
        false -> askPermission
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(permissionDeniedMsg(uiState)))
        Button(onClick = onClick) {
            Text(stringResource(permissionDeniedMsg(uiState)))
        }
    }
}