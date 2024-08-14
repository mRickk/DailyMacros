package com.example.dailymacros.ui.screens.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymacros.data.database.User
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import com.example.dailymacros.data.repositories.DatastoreRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class UserState(val users: List<User>)

interface SignupActions {
    fun setUser(user: User): Job
    fun signupUser(user: User, onFinished: () -> Unit = {}): Job
}

class SignupViewModel(
    private val databaseRepository: DailyMacrosRepository,
    private val repository: DatastoreRepository
) : ViewModel() {

    var loggedUser = mutableStateOf<User?>(null)
        private  set

    var userState by mutableStateOf(UserState(listOf()))
        private set

    val actions = object : SignupActions {
        override fun setUser(user: User) = viewModelScope.launch {
            repository.saveUser(user)
        }

        override fun signupUser(user: User, onFinished: () -> Unit) = viewModelScope.launch {
            loggedUser = mutableStateOf(user)
            databaseRepository.insertUser(user)
            repository.saveUser(user)
            onFinished()
        }
    }

    init {
        viewModelScope.launch {
            loggedUser = mutableStateOf(repository.user.first())
            userState = UserState(databaseRepository.users.first())
        }
    }
}