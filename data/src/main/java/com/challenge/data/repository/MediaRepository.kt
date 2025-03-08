package com.challenge.data.repository

import com.challenge.data.model.MediaFolderNode
import kotlinx.coroutines.flow.Flow

// Repository interface
interface MediaRepository {
    /**
     * Fetches the complete media hierarchy synchronously.
     */
    suspend fun getMediaHierarchy(): Result<MediaFolderNode>

    /**
     * Provides a Flow of media hierarchy for reactive updates.
     */
    fun getMediaHierarchyFlow(): Flow<Result<MediaFolderNode>>
}