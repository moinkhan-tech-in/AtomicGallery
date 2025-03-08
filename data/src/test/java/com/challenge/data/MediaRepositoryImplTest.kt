package com.challenge.data;

import com.challenge.common.model.MediaFolder
import com.challenge.data.local.MediaFetcher
import com.challenge.data.repository.LocalStorageMediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.given
import java.io.IOException

@ExperimentalCoroutinesApi
class LocalStorageMediaRepositoryTest {

    @Mock
    private lateinit var mediaFetcher: MediaFetcher

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: LocalStorageMediaRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        repository = LocalStorageMediaRepository(mediaFetcher, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset Main dispatcher
    }

    @Test
    fun `getMediaHierarchy returns success when MediaFetcher succeeds`() = runTest {
        // Given
        val expectedNode = listOf(MediaFolder(name = "Root", path = "", mediaFiles = mutableListOf()))
        given(mediaFetcher.fetchMedia()).willReturn(expectedNode)

        // When
        val result = repository.getMediaHierarchy()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedNode, result.getOrNull())
    }

    @Test
    fun `getMediaHierarchy returns failure when MediaFetcher throws IOException`() = runTest {
        // Given
        val exception = IOException("MediaStore query failed")
        given(mediaFetcher.fetchMedia()).willThrow(exception)

        // When
        val result = repository.getMediaHierarchy()

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}