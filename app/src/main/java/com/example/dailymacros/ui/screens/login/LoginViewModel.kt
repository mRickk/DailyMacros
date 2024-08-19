package com.example.dailymacros.ui.screens.login

import android.content.IntentSender.OnFinished
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymacros.data.database.User
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import com.example.dailymacros.data.repositories.DatastoreRepository
import com.example.dailymacros.ui.screens.profile.UserState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.log

data class UserState(val user: User?)

interface LoginActions {
    fun setUser(user: User): Job
    fun login(email: String, password: String, onFinished: () -> Unit = {}): Job
}

class LoginViewModel(
    private val dailyMacrosRepository: DailyMacrosRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {
    var loggedUser by mutableStateOf(UserState(null))
        private set

    val actions = object : LoginActions {
        override fun setUser(user: User) = viewModelScope.launch {
            Log.v("LoginViewModel", "Setting user: $user")
            datastoreRepository.saveUser(user)

        }

        override fun login(email: String, password: String, onFinished: () -> Unit) = viewModelScope.launch {
            loggedUser = UserState(dailyMacrosRepository.login(email, password))
            onFinished()
        }
    }

    init {
        viewModelScope.launch {
            loggedUser = UserState(datastoreRepository.user.first())
            Log.v("LoginViewModel", "Porcodio sono init: ${loggedUser.user}")
        }
    }
}