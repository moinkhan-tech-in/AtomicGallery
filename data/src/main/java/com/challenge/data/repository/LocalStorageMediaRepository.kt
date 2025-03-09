package com.challenge.data.repository

import com.challenge.common.AppDispatcher
import com.challenge.common.Dispatcher
import com.challenge.common.model.MediaItem
import com.challenge.data.local.MediaFetcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class LocalStorageMediaRepository @Inject constructor(
    private val mediaFetcher: MediaFetcher,
    @Dispatcher(AppDispatcher.IO) private val dispatcher: CoroutineDispatcher
) : MediaRepository {

    // TODO Can be done with separate cached data source
    private var cachedMediaItems: List<MediaItem>? = null

    override suspend fun getMediaHierarchy(): Result<List<MediaItem>> {
        return withContext(dispatcher) {
            try {

                if (cachedMediaItems.isNullOrEmpty().not()) {
                    return@withContext Result.success(cachedMediaItems.orEmpty())
                }

                val mediaItems = mediaFetcher.fetchMedia()
                cachedMediaItems = mediaItems
                Result.success(mediaItems)
            } catch (e: IOException) {
                Result.failure(e)
            }
        }
    }

    override fun getMediaHierarchyFlow(): Flow<Result<List<MediaItem>>> = flow {
        emit(getMediaHierarchy())
    }.flowOn(dispatcher)
}