package com.dicereligion.vagabond.data

import android.content.Context

import androidx.datastore.core.DataStore

import androidx.datastore.preferences.core.Preferences

import androidx.datastore.preferences.core.edit

import androidx.datastore.preferences.core.stringPreferencesKey

import androidx.datastore.preferences.preferencesDataStore

import com.dicereligion.vagabond.core.designsystem.AppTheme

import dagger.hilt.android.qualifiers.ApplicationContext

import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.map

import javax.inject.Inject

import javax.inject.Singleton



val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")



@Singleton

class SettingsRepository @Inject constructor(

    private val dataStore: DataStore<Preferences>

) {



    private object PreferencesKeys {

        val APP_THEME = stringPreferencesKey("app_theme")

    }



    val appTheme: Flow<AppTheme> = dataStore.data

        .map { preferences ->

            val themeName = preferences[PreferencesKeys.APP_THEME] ?: AppTheme.COSMIC_TEAL.name

            AppTheme.valueOf(themeName)

        }



    suspend fun saveAppTheme(theme: AppTheme) {

        dataStore.edit { settings ->

            settings[PreferencesKeys.APP_THEME] = theme.name

        }

    }

}
