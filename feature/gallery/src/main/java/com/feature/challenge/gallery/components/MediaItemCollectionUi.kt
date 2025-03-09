package com.feature.challenge.gallery.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.challenge.common.model.MediaItem

@Composable
fun MediaItemVerticalGridUi(
    mediaItems: List<MediaItem>,
    onFolderClick: (MediaItem) -> Unit = {}
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(mediaItems) {
            MediaGridItemUi(folderItem = it, onClick = { onFolderClick(it) })
        }
    }
}


@Composable
fun MediaItemVerticalListUi(
    mediaItems: List<MediaItem>,
    onFolderClick: (MediaItem) -> Unit = {}
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(mediaItems) {
            MediaListItemUi(folderItem = it, onClick = { onFolderClick(it) })
        }
    }
}

@Composable
@Preview
private fun MediaFolderVerticalGridUiPreview() {
    MediaItemVerticalGridUi(FakeMediaItemList)
}

@Composable
@Preview
private fun MediaItemVerticalListUiPreview() {
    MediaItemVerticalListUi(FakeMediaItemList)
}