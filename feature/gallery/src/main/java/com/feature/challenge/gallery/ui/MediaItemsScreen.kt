package com.feature.challenge.gallery.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.challenge.common.LocalViewType
import com.challenge.common.ViewType
import com.challenge.common.model.MediaItem
import com.challenge.common.ui.theme.AtomicGalleryTheme
import com.feature.challenge.gallery.R
import com.feature.challenge.gallery.components.FakeMediaItemList
import com.feature.challenge.gallery.components.MediaItemVerticalGridUi
import com.feature.challenge.gallery.components.MediaItemVerticalListUi
import com.feature.challenge.gallery.navigation.GalleryRoute
import com.feature.challenge.gallery.ui.MediaItemsUiState.Loading
import com.feature.challenge.gallery.ui.MediaItemsUiState.PermissionDenied
import com.feature.challenge.gallery.ui.MediaItemsUiState.Success
import com.feature.challenge.gallery.utils.AppScreenContent
import com.feature.challenge.gallery.utils.openAppSettings
import com.feature.challenge.gallery.utils.permissionDeniedAction
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
    onAskPermission: () -> Unit = {},
    onBackClick: () -> Unit = {},
    navigateToAppSetting: () -> Unit = {},
    onFolderClick: (MediaItem) -> Unit = {}
) {
    AppScreenContent(
        navigationTitle = args.folderName ?: stringResource(R.string.title_atomic_gallery),
        isLoading = uiState is Loading,
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
        Image(painterResource(R.drawable.ic_empty), contentDescription = null, colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary))
        Text(text = stringResource(permissionDeniedMsg(uiState)), textAlign = TextAlign.Center)
        Button(onClick = onClick) {
            Text(stringResource(permissionDeniedAction(uiState)))
        }
    }
}


@Composable
@Preview(name = "Gallery Grid Mode Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Gallery Grid Mode Light theme", uiMode = Configuration.UI_MODE_NIGHT_NO)
fun MediaItemsScreenContentGridPreview() {
    AtomicGalleryTheme {
        MediaItemsScreenContent(
            uiState = Success(FakeMediaItemList),
            args = GalleryRoute("Atomic")
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview(name = "Gallery List Mode Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Gallery List Mode Light theme", uiMode = Configuration.UI_MODE_NIGHT_NO)
fun MediaItemsScreenContentListPreview() {
    AtomicGalleryTheme {
        CompositionLocalProvider(LocalViewType provides mutableStateOf(ViewType.List)) {
            MediaItemsScreenContent(
                uiState = Success(FakeMediaItemList),
                args = GalleryRoute("Atomic"),
            )
        }
    }
}

@Composable
@Preview(name = "Gallery Loading Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Gallery Loading Light theme", uiMode = Configuration.UI_MODE_NIGHT_NO)
fun MediaItemsScreenContentLoadingPreview() {
    AtomicGalleryTheme {
        MediaItemsScreenContent(
            uiState = Loading,
            args = GalleryRoute("Atomic")
        )
    }
}


@Composable
@Preview(name = "Gallery Permission Denied Rationale Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Gallery Permission Denied Rationale Light theme", uiMode = Configuration.UI_MODE_NIGHT_NO)
fun MediaItemsScreenPermissionDeniedRationalePreview() {
    AtomicGalleryTheme {
        MediaItemsScreenContent(
            uiState = PermissionDenied(rationalRequired = true),
            args = GalleryRoute("Atomic")
        )
    }
}

@Composable
@Preview(name = "Gallery Permission Denied Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Gallery Permission Denied Light theme", uiMode = Configuration.UI_MODE_NIGHT_NO)
fun MediaItemsScreenPermissionDeniedPreview() {
    AtomicGalleryTheme {
        MediaItemsScreenContent(
            uiState = PermissionDenied(rationalRequired = false),
            args = GalleryRoute("Atomic")
        )
    }
}