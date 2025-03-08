package com.challenge.common.model

data class MediaFolder(
    val name: String,
    val path: String,
    val mediaFiles: MutableList<MediaFile>
) {

    fun getTotalMediaCount(): Int = mediaFiles.size

    fun getFirstMediaPath(): String? = mediaFiles.firstOrNull()?.path
}