package com.challenge.data.di;

import android.content.Context
import com.challenge.common.AppDispatcher
import com.challenge.common.Dispatcher
import com.challenge.data.local.MediaFetcher
import com.challenge.data.repository.LocalStorageMediaRepository
import com.challenge.data.repository.MediaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideMediaFetcher(
        @ApplicationContext context: Context
    ): MediaFetcher = MediaFetcher(context)

    @Provides
    @Singleton
    fun provideMediaRepository(
        mediaFetcher: MediaFetcher,
        @Dispatcher(AppDispatcher.IO) ioDispatcher: CoroutineDispatcher
    ): MediaRepository = LocalStorageMediaRepository(mediaFetcher, ioDispatcher)
}