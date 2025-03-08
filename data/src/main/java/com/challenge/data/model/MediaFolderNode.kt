package com.challenge.data.model

data class MediaFolderNode(
    val name: String,
    val path: String,
    val mediaItems: MutableList<MediaItem>,
    val subFolders: MutableMap<String, MediaFolderNode>,
    val parent: MediaFolderNode?
) {

    fun getTotalMediaCount(): Int = 
        mediaItems.size + subFolders.values.sumOf { it.getTotalMediaCount() }

    fun getFirstMediaPath(): String? = mediaItems.firstOrNull()?.path
}