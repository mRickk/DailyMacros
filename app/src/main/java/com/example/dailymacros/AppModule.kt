package com.example.dailymacros

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.dailymacros.data.repositories.ThemeRepository
import com.example.dailymacros.ui.screens.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore
        by preferencesDataStore("theme")
val appModule = module {
    single { get<Context>().dataStore }
    single { ThemeRepository(get()) }
    viewModel { SettingsViewModel(get()) }
}