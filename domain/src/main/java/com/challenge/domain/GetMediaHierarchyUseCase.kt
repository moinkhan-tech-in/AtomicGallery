package com.challenge.domain

import com.challenge.common.model.MediaItem
import com.challenge.data.repository.MediaRepository
import javax.inject.Inject

class GetMediaHierarchyUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {

    suspend operator fun invoke(): Result<List<MediaItem>> {
        return mediaRepository.getMediaHierarchy()
    }
}