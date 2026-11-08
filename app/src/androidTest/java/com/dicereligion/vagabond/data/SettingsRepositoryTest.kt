package com.dicereligion.vagabond.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicereligion.vagabond.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class SettingsRepositoryTest {

    private val TEST_DATASTORE_NAME = "test_settings"
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var dataStore: DataStore<Preferences>
    private val testContext: Context = ApplicationProvider.getApplicationContext()
    private val testCoroutineDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(testCoroutineDispatcher + Job())

    @Before
    fun setup() {
        Dispatchers.setMain(testCoroutineDispatcher)
        dataStore = PreferenceDataStoreFactory.create(
            scope = testCoroutineScope,
            produceFile = { testContext.preferencesDataStoreFile(TEST_DATASTORE_NAME) }
        )
        settingsRepository = SettingsRepository(dataStore)
    }

    @After
    fun teardown() {
        testCoroutineScope.cancel()
        File(testContext.filesDir, "datastore").deleteRecursively()
    }

    @Test
    fun defaultAppThemeIsCosmicTeal() = testCoroutineScope.runTest {
        val theme = settingsRepository.appTheme.first()
        assertEquals(AppTheme.COSMIC_TEAL, theme)
    }

    @Test
    fun saveAppThemePersistsSelection() = testCoroutineScope.runTest {
        settingsRepository.saveAppTheme(AppTheme.RED_STAR)
        val theme = settingsRepository.appTheme.first()
        assertEquals(AppTheme.RED_STAR, theme)

        settingsRepository.saveAppTheme(AppTheme.COSMIC_TEAL)
        val theme2 = settingsRepository.appTheme.first()
        assertEquals(AppTheme.COSMIC_TEAL, theme2)
    }
}
