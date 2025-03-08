package com.challenge.data.di

import com.challenge.data.repository.LocalStorageMediaRepository
import com.challenge.data.repository.MediaRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMediaRepository(
        mediaRepository: LocalStorageMediaRepository
    ): MediaRepository

}