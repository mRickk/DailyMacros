package com.example.dailymacros.ui.screens.diet

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymacros.data.database.User
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import com.example.dailymacros.data.repositories.DatastoreRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class UserState(val user: User?)

interface EditProfileActions {

}


class DietViewModel (
    private val dailyMacrosRepository: DailyMacrosRepository,
    private val datastoreRepository: DatastoreRepository
): ViewModel() {
    var loggedUser by mutableStateOf(UserState(null))
        private set

    init {
        viewModelScope.launch {
            loggedUser = UserState(datastoreRepository.user.first())
            Log.v("DietScreen", "Sono init: ${loggedUser.user}")
        }
    }

}