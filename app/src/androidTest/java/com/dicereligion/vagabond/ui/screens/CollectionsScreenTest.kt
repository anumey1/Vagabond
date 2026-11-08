package com.dicereligion.vagabond.ui.screens

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicereligion.vagabond.ui.theme.VagabondTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class CollectionsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockNavController: NavController

    @Before
    fun setup() {
        mockNavController = mock(NavController::class.java)
        composeTestRule.setContent {
            VagabondTheme {
                CollectionsScreen(navController = mockNavController)
            }
        }
    }

    @Test
    fun collectionsScreen_displaysTitleAndBackArrow() {
        composeTestRule.onNodeWithText("Collections").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Back to Landing").assertIsDisplayed()
    }

    @Test
    fun collectionsScreen_displaysEightComingSoonTiles() {
        composeTestRule.onAllNodesWithText("Coming Soon").assertCountEquals(8)
    }

    @Test
    fun collectionsScreen_backArrowPopsBackStack() {
        composeTestRule.onNodeWithContentDescription("Back to Landing").performClick()
        verify(mockNavController).popBackStack("landing", inclusive = false)
    }
}
