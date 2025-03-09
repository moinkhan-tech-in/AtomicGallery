package com.challenge.data.repository

import com.challenge.common.model.MediaItem
import kotlinx.coroutines.flow.Flow

// Repository interface
interface MediaRepository {
    /**
     * Fetches the complete media hierarchy synchronously.
     */
    suspend fun getMediaHierarchy(): Result<List<MediaItem>>

    /**
     * Provides a Flow of media hierarchy for reactive updates.
     */
    fun getMediaHierarchyFlow(): Flow<Result<List<MediaItem>>>
}