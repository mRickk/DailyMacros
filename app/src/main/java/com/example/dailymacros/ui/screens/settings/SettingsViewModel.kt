package com.example.dailymacros.ui.screens.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymacros.data.database.Gender
import com.example.dailymacros.data.database.User
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import com.example.dailymacros.data.repositories.DatastoreRepository
import com.example.dailymacros.data.repositories.ThemeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Theme {
    System,
    Light,
    Dark
}

interface SettingsActions {
    fun UpdatePassword(password: String): Job
}

data class UserState(val user: User?)
data class ThemeState(val theme: Theme)
class SettingsViewModel(
    private val themeRepository: ThemeRepository,
    private val datastoreRepository: DatastoreRepository,
    private val dailyMacrosRepository: DailyMacrosRepository
) : ViewModel() {
    var loggedUser by mutableStateOf(UserState(null))
        private set

    val state = themeRepository.theme.map { ThemeState(it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ThemeState(Theme.System)
    )
    fun changeTheme(theme: Theme) = viewModelScope.launch {
        themeRepository.setTheme(theme)
    }

    val actions = object : SettingsActions {
        override fun UpdatePassword(password: String) = viewModelScope.launch {
            if (loggedUser.user != null) {
                val userCopy = User(
                    email = loggedUser.user!!.email,
                    password = password,
                    username = loggedUser.user!!.username,
                    pictureUrl = loggedUser.user!!.pictureUrl,
                    height = loggedUser.user!!.height,
                    weight = loggedUser.user!!.weight,
                    gender = loggedUser.user!!.gender,
                    age = loggedUser.user!!.age,
                    activity = loggedUser.user!!.activity,
                    goal = loggedUser.user!!.goal,
                    bmr = loggedUser.user!!.bmr,
                    dailyKcal = loggedUser.user!!.dailyKcal,
                    diet = loggedUser.user!!.diet,
                    b1 = loggedUser.user!!.b1,
                    b2 = loggedUser.user!!.b2,
                    b3 = loggedUser.user!!.b3,
                    b4 = loggedUser.user!!.b4,
                    b5 = loggedUser.user!!.b5,
                    b6 = loggedUser.user!!.b6
                )
                dailyMacrosRepository.updateUser(userCopy)
                datastoreRepository.saveUser(userCopy)
                loggedUser = UserState(userCopy)
            }
        }
    }

    init {
        viewModelScope.launch {
            loggedUser = UserState(datastoreRepository.user.first())
        }
    }
}