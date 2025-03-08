package com.challenge.data.repository

import com.challenge.data.local.MediaFetcher
import com.challenge.data.model.MediaFolderNode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException

class LocalStorageMediaRepository(
    private val mediaFetcher: MediaFetcher,
    private val dispatcher: CoroutineDispatcher
) : MediaRepository {

    override suspend fun getMediaHierarchy(): Result<MediaFolderNode> {
        return withContext(dispatcher) {
            try {
                Result.success(mediaFetcher.fetchMediaHierarchy())
            } catch (e: IOException) {
                Result.failure(e)
            }
        }
    }

    override fun getMediaHierarchyFlow(): Flow<Result<MediaFolderNode>> = flow {
        emit(getMediaHierarchy())
    }.flowOn(dispatcher)
}