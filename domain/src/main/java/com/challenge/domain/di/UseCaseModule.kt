package com.challenge.domain.di

import com.challenge.data.repository.MediaRepository
import com.challenge.domain.GetMediaHierarchyUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetMediaHierarchyUseCase(
        repository: MediaRepository
    ): GetMediaHierarchyUseCase = GetMediaHierarchyUseCase(repository)
}