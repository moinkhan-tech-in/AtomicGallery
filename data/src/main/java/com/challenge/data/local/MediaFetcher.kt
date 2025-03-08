package com.challenge.data.local

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.challenge.data.model.MediaFolderNode
import com.challenge.data.model.MediaItem
import com.challenge.data.model.MediaType
import java.io.File
import java.io.IOException
import javax.inject.Singleton
import kotlin.jvm.Throws

class MediaFetcher(private val context: Context) {

    @Throws(IOException::class)
    fun fetchMediaHierarchy(): MediaFolderNode {
        val rootNode = MediaFolderNode(
            name = "Root",
            path = "",
            mediaItems = mutableListOf(),
            subFolders = mutableMapOf(),
            parent = null
        )
        val contentResolver = context.contentResolver

        fetchMedia(contentResolver, MediaType.Image, rootNode)
        fetchMedia(contentResolver, MediaType.Video, rootNode)

        return rootNode
    }


    private fun fetchMedia(
        contentResolver: ContentResolver,
        mediaType: MediaType,
        rootNode: MediaFolderNode
    ) {
        val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.DATE_ADDED,
            MediaStore.MediaColumns.MIME_TYPE
        )

        contentResolver.query(
            mediaType.uri,
            projection,
            null,
            null,
            "${MediaStore.MediaColumns.DATE_ADDED} DESC"
        )?.use { cursor ->
            processCursor(cursor, mediaType, rootNode)
        } ?: throw IOException("Failed to query $mediaType.uri")
    }

    private fun processCursor(cursor: Cursor, mediaType: MediaType, rootNode: MediaFolderNode) {
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
        val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
        val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED)
        val mimeColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)

        while (cursor.moveToNext()) {
            val mediaItem = MediaItem(
                id = cursor.getLong(idColumn),
                path = cursor.getString(pathColumn) ?: continue,
                name = cursor.getString(nameColumn) ?: "Unknown",
                dateAdded = cursor.getLong(dateColumn),
                mimeType = cursor.getString(mimeColumn) ?: "unknown",
                mediaType = mediaType
            )
            addToHierarchy(mediaItem, rootNode)
        }
    }


    private fun addToHierarchy(item: MediaItem, rootNode: MediaFolderNode) {
        val file = File(item.path)
        val parentPath = file.parent ?: return
        val pathParts = parentPath.split(File.separator).filter { it.isNotEmpty() }

        var currentNode = rootNode
        for (part in pathParts) {
            currentNode = currentNode.subFolders.getOrPut(part) {
                MediaFolderNode(
                    name = part,
                    path = if (currentNode.path.isEmpty()) part else "${currentNode.path}${File.separator}$part",
                    mediaItems = mutableListOf(),
                    subFolders = mutableMapOf(),
                    parent = currentNode
                )
            }
        }
        currentNode.mediaItems.add(item)
    }

}