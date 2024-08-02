package com.example.dailymacros.ui.screens.login

import android.content.IntentSender.OnFinished
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.dailymacros.data.database.User
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import kotlinx.coroutines.Job

interface LoginActions {
    fun setUser(user: User): Job
    fun login(email: Email, password: String, onFinished: () -> Unit = {}): Job
}

class LoginViewModel(
    private val dailyMacrosRepository: DailyMacrosRepository
) : ViewModel() {
    // ViewModel implementation
}