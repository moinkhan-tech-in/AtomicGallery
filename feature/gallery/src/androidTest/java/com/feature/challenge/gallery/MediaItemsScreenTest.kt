package com.feature.challenge.gallery

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.challenge.common.model.MediaItem
import com.feature.challenge.gallery.navigation.GalleryRoute
import com.feature.challenge.gallery.ui.folderlist.MediaItemsScreen
import com.feature.challenge.gallery.ui.folderlist.MediaItemsUiState
import com.feature.challenge.gallery.ui.folderlist.MediaItemsViewModelContract
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.given

@RunWith(AndroidJUnit4::class)
class MediaItemsScreenTest {

    @get:Rule(order = 0)
    val composeTestRule = createAndroidComposeRule<TestActivity>()

    private lateinit var viewModel: MediaItemsViewModelContract
    private lateinit var uiStateFlow: MutableStateFlow<MediaItemsUiState>
    private val mockOnFolderClick: (MediaItem) -> Unit = mock()
    private val mockOnBackClick: () -> Unit = mock()

    @Before
    fun setUp() {
        viewModel = mock()
        uiStateFlow = MutableStateFlow(MediaItemsUiState.Loading)
        given(viewModel.uiState).willReturn(uiStateFlow)
    }

    @Test
    fun displaysLoadingState() {


        // When: The MediaItemsScreen is composed
        composeTestRule.setContent {
            MediaItemsScreen(
                viewModel = viewModel,
                args = GalleryRoute(null),
                onFolderClick = mockOnFolderClick,
                onBackClick = mockOnBackClick
            )
        }

        // Given: The uiState is Loading
        uiStateFlow.value = MediaItemsUiState.Loading

        // Then: The loading indicator is displayed
        composeTestRule.onNodeWithContentDescription("Loading").assertIsDisplayed()
    }
}