package com.dicereligion.vagabond.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicereligion.vagabond.data.SettingsRepository
import com.dicereligion.vagabond.core.designsystem.AppTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val appTheme: StateFlow<AppTheme> = settingsRepository.appTheme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppTheme.COSMIC_TEAL // Default value
        )

    fun saveAppTheme(theme: AppTheme) {
        viewModelScope.launch {
            settingsRepository.saveAppTheme(theme)
        }
    }
}
