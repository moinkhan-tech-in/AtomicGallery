package com.feature.challenge.gallery.ui.folderlist

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.challenge.common.LocalViewType
import com.challenge.common.ViewType
import com.challenge.common.model.MediaItem
import com.challenge.common.ui.theme.AtomicGalleryTheme
import com.feature.challenge.gallery.R
import com.feature.challenge.gallery.components.FakeMediaItemList
import com.feature.challenge.gallery.components.MediaItemVerticalGridUi
import com.feature.challenge.gallery.components.MediaItemVerticalListUi
import com.feature.challenge.gallery.components.MessageContent
import com.feature.challenge.gallery.navigation.GalleryRoute
import com.feature.challenge.gallery.ui.folderlist.MediaItemsUiState.Loading
import com.feature.challenge.gallery.ui.folderlist.MediaItemsUiState.NoMediaAvailable
import com.feature.challenge.gallery.ui.folderlist.MediaItemsUiState.PermissionDenied
import com.feature.challenge.gallery.ui.folderlist.MediaItemsUiState.Success
import com.feature.challenge.gallery.utils.AppScreenContent
import com.feature.challenge.gallery.utils.isMediaPermissionGranted
import com.feature.challenge.gallery.utils.openAppSettings
import com.feature.challenge.gallery.utils.permissionDeniedAction
import com.feature.challenge.gallery.utils.permissionDeniedMsg

@Composable
fun MediaItemsScreen(
    viewModel: MediaItemsViewModelContract = hiltViewModel<MediaItemsViewModel>(),
    args: GalleryRoute,
    onFolderClick: (MediaItem) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    // To handle, for the user coming back from setting
    var isRefreshRequired by rememberSaveable { mutableStateOf(false) }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        if (isMediaPermissionGranted(context) && isRefreshRequired) {
            viewModel.fetchAllMedia(args.folderPath)
        }
        isRefreshRequired = false
    }

    // Permission handling code.
    val askPermission = remember { mutableStateOf(false) }
    HandleMediaPermission(
        askPermission = askPermission.value,
        onAccepted = { viewModel.fetchAllMedia(args.folderPath) },
        onDenied = viewModel::onDenied,
        onDismiss = { askPermission.value = false }
    )


    // Main body content of the screenÏÏ
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MediaItemsScreenContent(
        uiState = uiState,
        args = args,
        onAskPermission = { askPermission.value = true },
        navigateToAppSetting = {
//            isRefreshRequired = true
            openAppSettings(context)
        },
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

                is PermissionDenied -> MessageContent(
                    imageRes = R.drawable.ic_empty,
                    buttonText = stringResource(permissionDeniedAction(state)),
                    title = stringResource(permissionDeniedMsg(state)),
                    onButtonClick = when (state.rationalRequired) {
                        true -> navigateToAppSetting
                        false -> onAskPermission
                    }
                )

                is NoMediaAvailable -> MessageContent(
                    imageRes = R.drawable.ic_empty,
                    title = stringResource(R.string.msg_no_media_available)
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

@Composable
@Preview(name = "Gallery Media Not available Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Gallery Media Not available Light theme", uiMode = Configuration.UI_MODE_NIGHT_NO)
fun MediaItemsNoMediaAvailablePreview() {
    AtomicGalleryTheme {
        MediaItemsScreenContent(
            uiState = NoMediaAvailable,
            args = GalleryRoute("Atomic")
        )
    }
}