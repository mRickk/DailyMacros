package com.example.dailymacros.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.dailymacros.ui.screens.settings.Theme
import kotlinx.coroutines.flow.map

class ThemeRepository(private val dataStore: DataStore<Preferences>) {
    companion object {
        private val THEME_KEY = stringPreferencesKey("theme")
    }
    val theme = dataStore.data
        .map { preferences ->
            try {
                Theme.valueOf(preferences[THEME_KEY] ?: "System")
            } catch (_: Exception) {
                com.example.dailymacros.ui.screens.settings.Theme.System
            }
        }
    suspend fun setTheme(theme: Theme) =
        dataStore.edit { it[THEME_KEY] = theme.toString() }
}