package com.challenge.data.local

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.challenge.common.model.MediaItem
import com.challenge.common.model.MediaItemType
import java.io.File
import java.io.IOException

class MediaFetcher(private val context: Context) {

    @Throws(IOException::class)
    fun fetchMedia(): List<MediaItem> {
        return fetchAllMediaByType(context.contentResolver)
    }

    private fun fetchAllMediaByType(contentResolver: ContentResolver): List<MediaItem> {
        val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.MEDIA_TYPE
        )

        val selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE} IN (?, ?)"
        val selectionArgs = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
        )

        contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            projection,
            selection,
            selectionArgs,
            "${MediaStore.MediaColumns.DATE_ADDED} DESC"
        )?.use { cursor ->
            return processCursor(cursor)
        } ?: throw IOException("Failed to query uri")
    }

    private fun processCursor(cursor: Cursor): List<MediaItem> {
        // Hardcoded All Images Folder
        val allImagesFolder = MediaItem(
            name = MediaItemType.Image.dummyName,
            path = MediaItemType.Image.dummyPath,
            type = MediaItemType.Folder,
            mediaItems = mutableListOf()
        )

        // Hardcoded All Videos Folder
        val allVideoFolder = MediaItem(
            name = MediaItemType.Video.dummyName,
            path = MediaItemType.Video.dummyPath,
            type = MediaItemType.Folder,
            mediaItems = mutableListOf()
        )

        // All Other Folder based on user storage
        val mediaFolders = mutableMapOf<String, MediaItem>()

        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
        val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
        val mediaTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)

        while (cursor.moveToNext()) {

            val id = cursor.getLong(idColumn)
            val name = cursor.getString(nameColumn)
            val path = cursor.getString(pathColumn)

            val mediaType = cursor.getLong(mediaTypeColumn)
            val mediaItemType = when (mediaType.toInt()) {
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO -> MediaItemType.Video
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE -> MediaItemType.Image
                else -> continue
            }

            val fileUri = Uri.withAppendedPath(mediaItemType.uri, id.toString())

            val folderPath = File(path).parent ?: continue
            val folderName = File(folderPath).name

            val mediaFile = MediaItem(
                uri = fileUri,
                name = name,
                path = path,
                type = mediaItemType
            )

            if (mediaItemType == MediaItemType.Image) {
                allImagesFolder.mediaItems.add(mediaFile)
            } else {
                allVideoFolder.mediaItems.add(mediaFile)
            }

            if (!mediaFolders.containsKey(folderPath)) {
                mediaFolders[folderPath] = MediaItem(
                    name = folderName,
                    path = folderPath,
                    type = MediaItemType.Folder
                )
            }
            mediaFolders[folderPath]?.mediaItems?.add(mediaFile)
        }

        return listOfNotNull(
            // We should not add hardcoded folder when there is no media.
            allImagesFolder.takeIf { it.mediaItems.isNotEmpty() },
            allVideoFolder.takeIf { it.mediaItems.isNotEmpty() }
        ).plus(mediaFolders.values.toList())
    }
}