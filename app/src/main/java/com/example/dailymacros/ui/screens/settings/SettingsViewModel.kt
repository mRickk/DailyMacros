package com.example.dailymacros.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class Theme {
    System,
    Light,
    Dark
}

data class ThemeState(val theme: Theme)
class SettingsViewModel : ViewModel() {
    private val _state =
        MutableStateFlow(ThemeState(Theme.System))
    val state = _state.asStateFlow()
    fun changeTheme(theme: Theme) {
        _state.value = ThemeState(theme)
    }
}