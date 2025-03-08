package com.challenge.data.repository

import com.challenge.common.AppDispatcher
import com.challenge.common.Dispatcher
import com.challenge.common.model.MediaFolder
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

    override suspend fun getMediaHierarchy(): Result<List<MediaFolder>> {
        return withContext(dispatcher) {
            try {
                Result.success(mediaFetcher.fetchMedia())
            } catch (e: IOException) {
                Result.failure(e)
            }
        }
    }

    override fun getMediaHierarchyFlow(): Flow<Result<List<MediaFolder>>> = flow {
        emit(getMediaHierarchy())
    }.flowOn(dispatcher)
}