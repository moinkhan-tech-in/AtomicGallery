package com.challenge.data.local

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.challenge.common.model.MediaFile
import com.challenge.common.model.MediaFolder
import com.challenge.common.model.MediaType
import java.io.File
import java.io.IOException

class MediaFetcher(private val context: Context) {

    @Throws(IOException::class)
    fun fetchMedia(): List<MediaFolder> {

        val contentResolver = context.contentResolver

        return fetchMediaByType(contentResolver, MediaType.Image)
            .plus(fetchMediaByType(contentResolver, MediaType.Video))
    }


    private fun fetchMediaByType(contentResolver: ContentResolver, mediaType: MediaType): List<MediaFolder> {
        val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DISPLAY_NAME
        )

        contentResolver.query(
            mediaType.uri,
            projection,
            null,
            null,
            "${MediaStore.MediaColumns.DATE_ADDED} DESC"
        )?.use { cursor ->
            return processCursor(cursor, mediaType)
        } ?: throw IOException("Failed to query $mediaType.uri")
    }

    private fun processCursor(cursor: Cursor, mediaType: MediaType): List<MediaFolder> {
        val mediaFolders = mutableMapOf<String, MediaFolder>()

        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
        val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)

        while (cursor.moveToNext()) {

            val id = cursor.getLong(idColumn)
            val name = cursor.getString(nameColumn)
            val path = cursor.getString(pathColumn)
            val fileUri = Uri.withAppendedPath(mediaType.uri, id.toString())

            val folderPath = File(path).parent ?: continue
            val folderName = File(folderPath).name

            val mediaFile = MediaFile(
                uri = fileUri,
                name = name,
                path = path,
                mediaType = mediaType
            )

            if (!mediaFolders.containsKey(folderPath)) {
                mediaFolders[folderPath] = MediaFolder(folderName, folderPath, mutableListOf())
            }
            mediaFolders[folderPath]?.mediaFiles?.add(mediaFile)
        }

        return mediaFolders.values.toList()
    }
}