package com.challenge.common.model

import android.net.Uri

data class MediaFile(
    val uri: Uri,
    val name: String,
    val path: String,
    val mediaType: MediaType
)