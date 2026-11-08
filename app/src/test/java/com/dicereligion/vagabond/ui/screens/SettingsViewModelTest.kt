package com.dicereligion.vagabond.ui.screens

import com.dicereligion.vagabond.data.SettingsRepository
import com.dicereligion.vagabond.ui.theme.AppTheme
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var settingsViewModel: SettingsViewModel
    private val appThemeFlow = MutableStateFlow(AppTheme.COSMIC_TEAL)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        settingsRepository = mockk()
        every { settingsRepository.appTheme } returns appThemeFlow
        coEvery { settingsRepository.saveAppTheme(any()) } answers { newTheme ->
            appThemeFlow.value = newTheme.invocation.args[0] as AppTheme
        }
        settingsViewModel = SettingsViewModel(settingsRepository)
    }

    @Test
    fun initialAppThemeIsCosmicTeal() = runTest {
        assertEquals(AppTheme.COSMIC_TEAL, settingsViewModel.appTheme.first())
    }

    @Test
    fun saveAppThemeUpdatesRepositoryAndState() = runTest {
        val newTheme = AppTheme.RED_STAR
        settingsViewModel.saveAppTheme(newTheme)
        advanceUntilIdle() // Allow coroutines to complete

        coVerify(exactly = 1) { settingsRepository.saveAppTheme(newTheme) }

        // Collect the StateFlow to ensure it emits the updated value
        val collectedThemes = mutableListOf<AppTheme>()
        val job = settingsViewModel.appTheme.onEach { collectedThemes.add(it) }.launchIn(this)
        advanceUntilIdle()
        job.cancel()

        assertEquals(newTheme, collectedThemes.last())
    }
}
