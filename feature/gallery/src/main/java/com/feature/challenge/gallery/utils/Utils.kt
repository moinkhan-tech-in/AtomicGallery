package com.feature.challenge.gallery.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.core.net.toUri
import com.challenge.common.model.MediaItemType
import com.feature.challenge.gallery.R
import com.feature.challenge.gallery.ui.MediaItemsUiState

fun openAppSettings(context: Context) {
    val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = "package:${context.packageName}".toUri()
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}

fun defaultGradient(color: Color): Brush {
    return Brush.verticalGradient(listOf(Color.Transparent, color))
}

fun MediaItemType.iconRes() = when (this) {
    MediaItemType.Folder -> R.drawable.ic_folder_type
    MediaItemType.Image -> R.drawable.ic_image_type
    MediaItemType.Video -> R.drawable.ic_video_type
}

fun permissionDeniedMsg(denied: MediaItemsUiState.PermissionDenied): Int {
    return when (denied.rationalRequired) {
        true -> R.string.msg_media_rationale_message
        false -> R.string.msg_denied_media_message
    }
}

fun permissionDeniedAction(denied: MediaItemsUiState.PermissionDenied): Int {
    return when (denied.rationalRequired) {
        true -> R.string.action_rationale
        false -> R.string.action_denied
    }
}