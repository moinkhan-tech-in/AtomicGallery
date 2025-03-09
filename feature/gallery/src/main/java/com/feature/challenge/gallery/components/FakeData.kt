package com.feature.challenge.gallery.components

import android.net.Uri
import com.challenge.common.model.MediaItem
import com.challenge.common.model.MediaItemType

// TODO Better place for FakeData
val FakeMediaItem = MediaItem(
    uri = Uri.EMPTY,
    name = "Test",
    path = "",
    type = MediaItemType.Image
)

val FakeMediaItemWithSubItems = MediaItem(
    uri = Uri.EMPTY,
    name = "Folder Name",
    path = "",
    type = MediaItemType.Folder,
    mediaItems = mutableListOf<MediaItem>().apply {
        repeat(5) {
            add(FakeMediaItem)
        }
    }
)

val FakeMediaItemList = mutableListOf<MediaItem>().apply {
    repeat(5) {
        add(FakeMediaItemWithSubItems)
    }
}.toList()

