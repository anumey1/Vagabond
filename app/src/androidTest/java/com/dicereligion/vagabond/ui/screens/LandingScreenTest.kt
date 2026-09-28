package com.dicereligion.vagabond.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicereligion.vagabond.ui.theme.VagabondTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class LandingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockNavController: NavController

    @Before
    fun setup() {
        mockNavController = mock(NavController::class.java)
        composeTestRule.setContent {
            VagabondTheme {
                LandingScreen(navController = mockNavController)
            }
        }
    }

    @Test
    fun landingScreen_displaysTitleAndButtons() {
        composeTestRule.onNodeWithText("Vagabond").assertIsDisplayed()
        composeTestRule.onNodeWithText("Collections").assertIsDisplayed()
        composeTestRule.onNodeWithText("Settings").assertIsDisplayed()
    }

    @Test
    fun landingScreen_collectionsButtonNavigatesToCollections() {
        composeTestRule.onNodeWithText("Collections").performClick()
        verify(mockNavController).navigate("collections")
    }

    @Test
    fun landingScreen_settingsButtonNavigatesToSettings() {
        composeTestRule.onNodeWithText("Settings").performClick()
        verify(mockNavController).navigate("settings")
    }
}
