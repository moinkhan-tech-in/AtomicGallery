package com.challenge.domain

import com.challenge.common.model.MediaFolder
import com.challenge.data.repository.MediaRepository
import javax.inject.Inject

class GetMediaHierarchyUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {

    suspend operator fun invoke(): Result<List<MediaFolder>> {
        return mediaRepository.getMediaHierarchy()
    }
}