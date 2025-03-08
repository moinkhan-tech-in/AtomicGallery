package com.challenge.data.model

import android.net.Uri
import android.provider.MediaStore

sealed class MediaType {
    data object Image : MediaType()
    data object Video : MediaType()

    val uri: Uri
        get() = when (this) {
            Image -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            Video -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        }
}