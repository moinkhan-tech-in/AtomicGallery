package com.feature.challenge.gallery.ui

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.challenge.common.model.MediaFolder
import com.feature.challenge.gallery.R
import com.feature.challenge.gallery.ui.MediaFolderUiState.PermissionDenied
import com.feature.challenge.gallery.ui.MediaFolderUiState.Success
import com.feature.challenge.gallery.utils.AppScreenContent
import com.feature.challenge.gallery.utils.openAppSettings

@Composable
fun MediaFolderScreen(
    viewModel: MediaFolderViewModel = hiltViewModel<MediaFolderViewModel>()
) {


    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        Log.d("LifecycleEventEffect", "Resume")
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val askPermission = remember { mutableStateOf(true) }
    HandleMediaPermission(
        askPermission = askPermission.value,
        onAccepted = viewModel::onAccepted,
        onDenied = viewModel::onDenied,
        onDismiss = { askPermission.value = false }
    )

    val context = LocalContext.current
    MediaFolderScreenContent(
        uiState = uiState,
        onAskPermission = { askPermission.value = true },
        navigateToAppSetting = { openAppSettings(context) }
    )
}

@Composable
private fun MediaFolderScreenContent(
    uiState: MediaFolderUiState,
    onAskPermission: () -> Unit,
    navigateToAppSetting: () -> Unit
) {
    AppScreenContent(
        navigationTitle = "Avrioc Gallery",
        isLoading = uiState is MediaFolderUiState.Loading
    ) {
        Crossfade(uiState) { state ->
            when (state) {
                is Success -> MediaFolderSuccessContent(state.folderList)
                is PermissionDenied -> MediaFolderNoPermissionContent(
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
private fun MediaFolderSuccessContent(folder: List<MediaFolder>) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(folder) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "${it.name} ${it.path}"
            )
        }
    }
}

@Composable
private fun MediaFolderNoPermissionContent(
    uiState: PermissionDenied,
    navigateToAppSetting: () -> Unit,
    askPermission: () -> Unit
) {
    val message = when (uiState.rationalRequired) {
        true -> stringResource(R.string.msg_media_rationale_message)
        false -> stringResource(R.string.msg_denied_media_message)
    }

    val buttonText = when (uiState.rationalRequired) {
        true -> stringResource(R.string.action_rationale)
        false -> stringResource(R.string.action_denied)
    }

    val onClick = when (uiState.rationalRequired) {
        true -> navigateToAppSetting
        false -> askPermission
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message)
        Button(onClick = onClick) {
            Text(buttonText)
        }
    }
}