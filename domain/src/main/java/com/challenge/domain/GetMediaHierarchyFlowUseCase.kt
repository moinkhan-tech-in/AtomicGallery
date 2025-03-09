package com.challenge.domain

import com.challenge.common.model.MediaItem
import com.challenge.data.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMediaHierarchyFlowUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {

    operator fun invoke(): Flow<Result<List<MediaItem>>> {
        return mediaRepository.getMediaHierarchyFlow()
    }
}