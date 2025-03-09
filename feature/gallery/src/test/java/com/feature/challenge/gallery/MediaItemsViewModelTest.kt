package com.feature.challenge.gallery

import com.challenge.domain.GetMediaHierarchyUseCase
import com.feature.challenge.gallery.components.FakeMediaItem
import com.feature.challenge.gallery.ui.folderlist.MediaItemsUiState
import com.feature.challenge.gallery.ui.folderlist.MediaItemsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.given

@ExperimentalCoroutinesApi
class MediaItemsViewModelTest {

    @Mock
    private lateinit var getMediaHierarchyUseCase: GetMediaHierarchyUseCase

    private lateinit var viewModel: MediaItemsViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = MediaItemsViewModel(getMediaHierarchyUseCase, testDispatcher)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        // Given

        // When
        val initialState = viewModel.uiState.first()

        // Then
        assertEquals(MediaItemsUiState.Loading, initialState)
    }

    @Test
    fun `no media available when hierarchy is empty`() = runTest {
        // Given
        given(getMediaHierarchyUseCase.invoke()).willReturn(Result.success(listOf()))

        // When
        viewModel.fetchAllMedia()

        // Then
        assertEquals(MediaItemsUiState.Loading, viewModel.uiState.first())
         // Drop the loading
        assertEquals(MediaItemsUiState.NoMediaAvailable, viewModel.uiState.drop(1).first())
    }

    @Test
    fun `root folder success with non-empty hierarchy`() = runTest {
        // Given
        val fakeData = listOf(FakeMediaItem)
        given(getMediaHierarchyUseCase.invoke()).willReturn(Result.success(fakeData))

        // When
        viewModel.fetchAllMedia()

        // Then
        assertEquals(MediaItemsUiState.Loading, viewModel.uiState.first())
        // Drop the loading
        assertEquals(
            MediaItemsUiState.Success(mediaItemList = fakeData),
            viewModel.uiState.drop(1).first()
        )
    }

    @Test
    fun `permission denied on failure`() = runTest {
        // Given
        val exception = SecurityException("Permission denied")
        given(getMediaHierarchyUseCase.invoke()).willReturn(Result.failure(exception))

        // When
        viewModel.fetchAllMedia()

        // Then:
        assertEquals(MediaItemsUiState.Loading, viewModel.uiState.first())
    }
}