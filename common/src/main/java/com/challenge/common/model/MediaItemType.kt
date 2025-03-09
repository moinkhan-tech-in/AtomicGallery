package com.challenge.common.model

import android.net.Uri
import android.provider.MediaStore

sealed class MediaItemType {
    data object Image : MediaItemType()
    data object Video : MediaItemType()
    data object Folder : MediaItemType()

    val uri: Uri
        get() = when (this) {
            Image -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            Video -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            Folder -> Uri.EMPTY
        }

    val dummyPath: String
        get() = when (this) {
            Folder -> "/"
            Image -> "/image"
            Video -> "/video"
        }

    val dummyName: String
        get() = when (this) {
            Folder -> "/"
            Image -> "All Images"
            Video -> "All Videos"
        }
}