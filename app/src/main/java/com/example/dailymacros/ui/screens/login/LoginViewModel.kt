package com.example.dailymacros.ui.screens.login

import android.content.IntentSender.OnFinished
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymacros.data.database.User
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import com.example.dailymacros.data.repositories.DatastoreRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

interface LoginActions {
    fun setUser(user: User): Job
    fun login(email: String, password: String, onFinished: () -> Unit = {}): Job
}

class LoginViewModel(
    private val dailyMacrosRepository: DailyMacrosRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {
    var loggedUser = mutableStateOf<User?>(null)
        private set

    val actions = object : LoginActions {
        override fun setUser(user: User) = viewModelScope.launch {
            datastoreRepository.saveUser(user)
        }

        override fun login(email: String, password: String, onFinished: () -> Unit) = viewModelScope.launch {
            loggedUser = mutableStateOf(dailyMacrosRepository.login(email, password))
            onFinished()
        }
    }

    init {
        viewModelScope.launch {
            loggedUser = mutableStateOf(datastoreRepository.user.first())
        }
    }
}