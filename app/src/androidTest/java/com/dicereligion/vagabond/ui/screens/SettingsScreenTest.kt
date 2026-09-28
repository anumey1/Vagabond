package com.dicereligion.vagabond.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicereligion.vagabond.data.SettingsRepository
import com.dicereligion.vagabond.ui.theme.AppTheme
import com.dicereligion.vagabond.ui.theme.VagabondTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockNavController: NavController
    private lateinit var mockSettingsViewModel: SettingsViewModel
    private val appThemeFlow = MutableStateFlow(AppTheme.COSMIC_TEAL)

    @Before
    fun setup() {
        mockNavController = mock(NavController::class.java)
        mockSettingsViewModel = mockk(relaxed = true)
        every { mockSettingsViewModel.appTheme } returns appThemeFlow

        composeTestRule.setContent {
            VagabondTheme {
                SettingsScreen(navController = mockNavController, viewModel = mockSettingsViewModel)
            }
        }
    }

    @Test
    fun settingsScreen_displaysTitleAndBackArrow() {
        composeTestRule.onNodeWithText("Settings").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Back to Landing").assertIsDisplayed()
    }

    @Test
    fun settingsScreen_displaysAppThemeToggle() {
        composeTestRule.onNodeWithText("App Theme").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cosmic Teal").assertIsDisplayed()
        composeTestRule.onNodeWithText("Red Star").assertIsDisplayed()
    }

    @Test
    fun settingsScreen_backArrowPopsBackStack() {
        composeTestRule.onNodeWithContentDescription("Back to Landing").performClick()
        verify(mockNavController).popBackStack("landing", inclusive = false)
    }

    @Test
    fun settingsScreen_themeToggleChangesTheme() {
        composeTestRule.onNodeWithText("Red Star").performClick()
        Mockito.verify(mockSettingsViewModel).saveAppTheme(AppTheme.RED_STAR)
    }
}
