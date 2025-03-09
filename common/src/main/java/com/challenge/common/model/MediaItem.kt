package com.challenge.common.model

import android.net.Uri

data class MediaItem(
    val uri: Uri = Uri.EMPTY,
    val name: String,
    val path: String,
    val type: MediaItemType,
    val mediaItems: MutableList<MediaItem> = mutableListOf()
) {

    fun getFirstMediaPath(): String = mediaItems.firstOrNull()?.path ?: path
}