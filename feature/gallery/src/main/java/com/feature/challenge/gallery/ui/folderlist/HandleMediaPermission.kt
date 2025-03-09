package com.feature.challenge.gallery.ui.folderlist

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.feature.challenge.gallery.ui.folderlist.MediaItemsUiState.PermissionDenied
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

const val ReadImagesPermission = Manifest.permission.READ_MEDIA_IMAGES
const val ReadVideoPermission = Manifest.permission.READ_MEDIA_VIDEO
const val ReadExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HandleMediaPermission(
    askPermission: Boolean,
    onAccepted: () -> Unit,
    onDismiss: () -> Unit,
    onDenied: (PermissionDenied) -> Unit
) {

    val mediaPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(ReadImagesPermission, ReadVideoPermission)
    } else {
        listOf(ReadExternalStorage)
    }

    val mediaPermissionsState = rememberMultiplePermissionsState(mediaPermissions)

    val mediaPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { result ->

            val readImageAccepted = result[ReadImagesPermission] == true
            val readVideoAccepted = result[ReadVideoPermission] == true
            val readExternalAccepted = result[ReadExternalStorage] == true

            if ((readImageAccepted && readVideoAccepted) || readExternalAccepted) {
                onAccepted()
            } else {
                onDenied(PermissionDenied(rationalRequired = mediaPermissionsState.shouldShowRationale))
            }
            onDismiss()
        }
    )

    LaunchedEffect(mediaPermissionsState, askPermission) {
        if (mediaPermissionsState.allPermissionsGranted) {
            onAccepted()
            onDismiss()
            return@LaunchedEffect
        }

        if (!mediaPermissionsState.allPermissionsGranted && mediaPermissionsState.shouldShowRationale) {
            onDenied(PermissionDenied(rationalRequired = true))
        } else {
            onDenied(PermissionDenied(rationalRequired = false))

            if (askPermission) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    mediaPermissionLauncher.launch(arrayOf(ReadImagesPermission, ReadVideoPermission))
                } else {
                    mediaPermissionLauncher.launch(arrayOf(ReadExternalStorage))
                }
            }
        }
        onDismiss()
    }
}
