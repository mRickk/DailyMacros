package com.example.dailymacros.ui.screens.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.PrimaryKey
import com.example.dailymacros.data.database.ActivityType
import com.example.dailymacros.data.database.Gender
import com.example.dailymacros.data.database.GoalType
import com.example.dailymacros.data.database.User
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import com.example.dailymacros.data.repositories.DatastoreRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class UserState(val user: User?)

interface ProfileActions {
    fun setProfilePicUrl(email: String, profilePicUrl: String): Job
}

class ProfileViewModel(
    private val dailyMacrosRepository: DailyMacrosRepository,
    private val datastoreRepository: DatastoreRepository) : ViewModel() {
    var loggedUser by mutableStateOf(UserState(null))
        private set

    val actions = object : ProfileActions {
        override fun setProfilePicUrl(email: String, profilePicUrl: String) =
            viewModelScope.launch {
                dailyMacrosRepository.setProfilePicUrl(email, profilePicUrl)
                if (loggedUser.user != null) {
                    val userCopy = User(
                        email = loggedUser.user!!.email,
                        password = loggedUser.user!!.password,
                        username = loggedUser.user!!.username,
                        pictureUrl = profilePicUrl,
                        height = loggedUser.user!!.height,
                        weight = loggedUser.user!!.weight,
                        gender = loggedUser.user!!.gender,
                        age = loggedUser.user!!.age,
                        activity = loggedUser.user!!.activity,
                        goal = loggedUser.user!!.goal,
                        bmr = loggedUser.user!!.bmr,
                        dailyKcal = loggedUser.user!!.dailyKcal
                    )
                    datastoreRepository.saveUser(userCopy)
                    loggedUser = UserState(userCopy)
                }
            }
    }
    init {
        viewModelScope.launch {
            loggedUser = UserState(datastoreRepository.user.first())
            Log.v("profileViewModel", "Sono init: ${loggedUser.user}")
        }
    }
}