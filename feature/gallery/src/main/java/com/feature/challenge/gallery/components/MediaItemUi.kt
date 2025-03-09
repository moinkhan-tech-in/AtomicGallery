package com.feature.challenge.gallery.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.challenge.common.model.MediaItem
import com.challenge.common.model.MediaItemType
import com.feature.challenge.gallery.utils.defaultGradient
import com.feature.challenge.gallery.utils.iconRes

@Composable
fun MediaGridItemUi(
    folderItem: MediaItem,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary)
    ) {
        Box(Modifier.fillMaxSize()) {
            MediaThumbnail(
                modifier = Modifier.fillMaxSize(),
                path = folderItem.getFirstMediaPath()
            )

            Row(
                Modifier
                    .background(defaultGradient(MaterialTheme.colorScheme.primaryContainer))
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
                    .padding(top = 64.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(folderItem.type.iconRes()),
                    contentDescription = folderItem.type.toString()
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    text = folderItem.name,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            if (folderItem.type == MediaItemType.Folder && folderItem.mediaItems.size > 0) {
                Text(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = MaterialTheme.colorScheme.secondaryContainer)
                        .padding(horizontal = 8.dp)
                        .padding(vertical = 2.dp),
                    text = folderItem.mediaItems.size.toString(),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun MediaThumbnail(modifier: Modifier, path: String) {
    val context = LocalContext.current
    val request = ImageRequest.Builder(context)
        .data(path)
        .build()

    AsyncImage(
        model = request,
        contentDescription = "Thumbnail",
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

@Composable
fun MediaListItemUi(
    folderItem: MediaItem,
    onClick: () -> Unit = {}
) {
    Card(Modifier.clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MediaThumbnail(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp)
                    ),
                path = folderItem.getFirstMediaPath()
            )

            Icon(
                modifier = Modifier.padding(start = 12.dp),
                painter = painterResource(folderItem.type.iconRes()),
                contentDescription = folderItem.type.toString()
            )

            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                text = folderItem.name,
                maxLines = 2,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            if (folderItem.type == MediaItemType.Folder && folderItem.mediaItems.size > 0) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = folderItem.mediaItems.size.toString(),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
@Preview
fun MediaGridItemUiPreview() {
    MediaGridItemUi(FakeMediaItemWithSubItems)
}

@Composable
@Preview
fun MediaListItemUiPreview() {
    MediaListItemUi(FakeMediaItemWithSubItems)
}