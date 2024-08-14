package com.example.dailymacros

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.dailymacros.data.database.DailyMacrosDatabase
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import com.example.dailymacros.data.repositories.DatastoreRepository
import com.example.dailymacros.data.repositories.ThemeRepository
import com.example.dailymacros.ui.screens.diary.DiaryViewModel
import com.example.dailymacros.ui.screens.login.LoginViewModel
import com.example.dailymacros.ui.screens.selectexercise.SelectExerciseViewModel
import com.example.dailymacros.ui.screens.settings.SettingsViewModel
import com.example.dailymacros.ui.screens.signup.SignupViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore
        by preferencesDataStore("theme")
val appModule = module {

    single {
        Room.databaseBuilder(
            get(),
            DailyMacrosDatabase::class.java,
            "dailymacros"
        ).fallbackToDestructiveMigration().build()
    }

    single { get<Context>().dataStore }
    single { ThemeRepository(get()) }
    single { DailyMacrosRepository(
        get<DailyMacrosDatabase>().userDAO(),
        get<DailyMacrosDatabase>().foodDAO(),
        get<DailyMacrosDatabase>().foodInsideMealDAO(),
        get<DailyMacrosDatabase>().mealDAO(),
        get<DailyMacrosDatabase>().exerciseDAO(),
        get<DailyMacrosDatabase>().exerciseInsideDayDAO())
    }
    single { DatastoreRepository(get()) }

    viewModel { SettingsViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { SignupViewModel(get(), get()) }
    viewModel { DiaryViewModel(get()) }
    viewModel { SelectExerciseViewModel(get()) }
}