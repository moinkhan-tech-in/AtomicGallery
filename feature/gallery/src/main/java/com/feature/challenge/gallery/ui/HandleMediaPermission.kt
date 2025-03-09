package com.feature.challenge.gallery.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.feature.challenge.gallery.ui.MediaItemsUiState.PermissionDenied
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HandleMediaPermission(
    askPermission: Boolean,
    onAccepted: () -> Unit,
    onDismiss: () -> Unit,
    onDenied: (PermissionDenied) -> Unit
) {
    val mediaPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
        )
    )

    val mediaPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { result ->
            if (result[Manifest.permission.READ_MEDIA_IMAGES] == true
                && result[Manifest.permission.READ_MEDIA_VIDEO] == true) {
                onAccepted()
            } else {
                onDenied(PermissionDenied(rationalRequired = false))
            }
            onDismiss()
        }
    )

    LaunchedEffect(askPermission) {
        if (mediaPermissionsState.allPermissionsGranted) {
            onAccepted()
            onDismiss()
            return@LaunchedEffect
        }

        if (!mediaPermissionsState.allPermissionsGranted && mediaPermissionsState.shouldShowRationale) {
            onDenied(PermissionDenied(rationalRequired = true))
        } else {
            onDenied(PermissionDenied(rationalRequired = false))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                mediaPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO
                    )
                )
            }
        }
        onDismiss()
    }
}
