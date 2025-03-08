package com.challenge.data.model

data class MediaItem(
    val id: Long,
    val path: String,
    val name: String,
    val dateAdded: Long,
    val mimeType: String,
    val mediaType: MediaType
)